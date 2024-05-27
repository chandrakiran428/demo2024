package com.assesment.ms.takeupassessmentandsubmit.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.assesment.ms.takeupassessmentandsubmit.DTO.AssessmentDetailsInputDTO;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentDetails;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentKey;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentType;
import com.assesment.ms.takeupassessmentandsubmit.entity.Result;
import com.assesment.ms.takeupassessmentandsubmit.entity.SubmissionType;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.InvalidAssessmentDetailsException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.InvalidDataException;
import com.assesment.ms.takeupassessmentandsubmit.repository.AssessmentDetailsRepository;



@Service
public class AssessmentDetailsService {
//	@Autowired
//	private AssessmentDetailsRepository assessmentDetailsRepository;

	@Autowired
	private RestTemplate restTemplate;

	//**** CHANGED here 
	 private final AssessmentDetailsRepository assessmentDetailsRepository;
	    private final ResultServiceImp resultService;

	    @Autowired
	    public AssessmentDetailsService(AssessmentDetailsRepository assessmentDetailsRepository, @Lazy ResultServiceImp resultService) {
	        this.assessmentDetailsRepository = assessmentDetailsRepository;
	        this.resultService = resultService;
	    }
    // below from chatGPT
    
	
	//save assessment into database
	public AssessmentDetails saveAssessment(AssessmentDetails assessment) {
		try {
			return assessmentDetailsRepository.save(assessment);
		} catch (Exception e) {
			throw new InvalidAssessmentDetailsException("Failed to save assessment.");
		}
	}
	
	
	//get all assessments saved in the database 
	public List<AssessmentDetails> getAllAssessments() {
		try {
			return assessmentDetailsRepository.findAll();
		} catch (Exception e) {
			throw new InvalidAssessmentDetailsException("Failed to retrieve assessments.");
		}
	}

	
	
	//method to get assessment details from frontend input DTO, process it and save it into database
	
	public AssessmentDetails processAssessment(AssessmentDetailsInputDTO inputDTO) throws InvalidDataException {
		validateUser(inputDTO);
		validateInputData(inputDTO);

		try {
			Integer score = calculateScore(inputDTO.getEmployeeResponses());
			Result result = determineResult(score, inputDTO.getPassingPercentage(), inputDTO.getNumberOfQuestions());

			AssessmentDetails assessment = createAssessmentFromInput(inputDTO, score, result);
			AssessmentDetails savedAssessment = assessmentDetailsRepository.save(assessment);

			/*
			 *
			 * space for result analysis methods
			 */
			resultService.getData(savedAssessment.getId().getEmpId() ,savedAssessment.getId().getAssessmentId());
			
			return savedAssessment;
		} catch (Exception e) {
			throw new InvalidAssessmentDetailsException("Failed to process assessment.");
		}
	}

	private void validateInputData(AssessmentDetailsInputDTO inputDTO) throws InvalidDataException {
		if (inputDTO.getEmployeeResponses().size() != inputDTO.getNumberOfQuestions()) {
			throw new InvalidDataException("Number of questions in employeeResponses does not match numberOfQuestions");
		}
	}
	
	private void validateUser(AssessmentDetailsInputDTO inputDTO) throws InvalidAssessmentDetailsException{
		AssessmentKey tempkey = new AssessmentKey(inputDTO.getAssessmentId(), inputDTO.getEmpId());
		if(getAssessmentById(tempkey)!=null)
			throw new InvalidAssessmentDetailsException("Assessment already submitted.");
	}

	private AssessmentDetails createAssessmentFromInput(AssessmentDetailsInputDTO inputDTO, Integer score,
			Result result) {
		AssessmentDetails assessment = new AssessmentDetails();
		AssessmentKey key = new AssessmentKey(inputDTO.getAssessmentId(), inputDTO.getEmpId());

		assessment.setId(key);
		assessment.setAssessmentName(inputDTO.getAssessmentName());
		assessment.setAssessmentDate(inputDTO.getAssessmentDate().toLocalDate());
		assessment.setCourseName(inputDTO.getCourseName());
		assessment.setAssessmentType(AssessmentType.valueOf(inputDTO.getAssessmentType().toUpperCase()));
		assessment.setTargetBatch(inputDTO.getTargetBatch());
		assessment.setNumberOfQuestions(inputDTO.getNumberOfQuestions());
		assessment.setPassingPercentage(inputDTO.getPassingPercentage());
		assessment.setSubmissionType(SubmissionType.valueOf(inputDTO.getSubmissionType().toUpperCase()));
		assessment.setNumberOfViolations(inputDTO.getNumberOfViolations());
		assessment.setQuestionPaperId(inputDTO.getQuestionPaperId());
		assessment.setEmployeeResponses(inputDTO.getEmployeeResponses());
		assessment.setScore(score);
		assessment.setResult(result);

		return assessment;
	}

	
	
	//method to calculate score
	public Integer calculateScore(Map<Integer, List<String>> employeeResponses) {
		// TODO Auto-generated method stub

		
		//create a list of questionIds from the employeeResponses dictionary keys
		List<Integer> questionIds = new ArrayList<>(employeeResponses.keySet());

		// Convert question IDs to comma-separated string
		String questionIdsString = questionIds.stream().map(String::valueOf).collect(Collectors.joining(","));

		
		String fetchUrl = "http://172.18.4.37:9094/questions/getQuestions?questionNumbers=" + questionIdsString;

		//fetch question details from question bank for the required question Ids
		List<Map<String, Object>> questions = restTemplate.getForObject(fetchUrl, List.class);

		
		//a list of maps containing question ids and their details
		List<Map<String, Object>> extractedQuestions = new ArrayList<>();
		for (Map<String, Object> question : questions) {
			Map<String, Object> extractedQuestion = new HashMap<>();
			extractedQuestion.put("questionId", question.get("questionId"));
			List<?> answers = (List<?>) question.get("answers");
			extractedQuestion.put("answers", answers);
			extractedQuestions.add(extractedQuestion);
		}

		System.out.println(extractedQuestions);

		
		//comparing user responses and actual correct responses to calculate score
		int score = 0;
		for (Map.Entry<Integer, List<String>> entry : employeeResponses.entrySet()) {
			Integer questionId = entry.getKey();
			List<String> employeeAnswer = entry.getValue();
			Collections.sort(employeeAnswer); //sorting the list of employeee response for each question
			for (Map<String, Object> extractedQuestion : extractedQuestions) {
				if (questionId.equals(extractedQuestion.get("questionId"))) {

					if (employeeAnswer.equals(extractedQuestion.get("answers"))) {
						score++;
					}
				}
			}
		}
		return score;

	}

	
	//calculating the result PASS/FAIL based on score
	Result determineResult(Integer score, Integer passingPercentage, Integer totalMarks) {
		// TODO Auto-generated method stub
		AssessmentDetails assessment = new AssessmentDetails();

		Result result;
		double passingThreshold = totalMarks * (passingPercentage * 0.01);
		if (score >= passingThreshold) {
			result = assessment.getResult().PASS;
			return result;

		} else {
			result = assessment.getResult().FAIL;
			return result;

		}
	}

	//get assessment details based on key
	public AssessmentDetails getAssessmentById(AssessmentKey key) {
		// TODO Auto-generated method stub
		return assessmentDetailsRepository.findById(key).orElse(null);
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
		assessmentDetailsRepository.deleteAll();

	}







}
