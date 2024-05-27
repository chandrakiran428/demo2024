package com.assesment.ms.takeupassessmentandsubmit.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.assesment.ms.takeupassessmentandsubmit.DTO.AssessmentsListDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.BatchResultStatsDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.CompletedAssessments;
import com.assesment.ms.takeupassessmentandsubmit.DTO.QuestionPaperSubtopics;
import com.assesment.ms.takeupassessmentandsubmit.DTO.QuestionTestdto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.ResultStatusDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.TraineeResultDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.UserIdandSubjectDTOresult;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentDetails;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentKey;
import com.assesment.ms.takeupassessmentandsubmit.entity.Result;
import com.assesment.ms.takeupassessmentandsubmit.entity.ResultAndAnalysis;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.AssessmentNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.BatchNoNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.QuestionsFetchFailedException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.RecordNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.ResultServiceException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.SubjectNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.SubtopicsAndDifficultyLevelCountNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.UserNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.repository.ResultRepository;
import com.assesment.ms.takeupassessmentandsubmit.resultAndAnalysisConverter.ResultAndAnalysisConverter;


import jakarta.validation.Validator;



@Service("resultservImp")
public class ResultServiceImp implements ResultService{

	@Autowired
	private ResultRepository repository;
   
	@Autowired
	private Validator validator;
	@Autowired
	private ResultAndAnalysisConverter resultAndAnalysisConveter;
	
	 private final RestTemplate restTemplate;
	    private final AssessmentDetailsService calculatedRepo;
	    private final SequenceGeneratorService sequenceGeneratorService;

	    @Autowired
	    public ResultServiceImp(SequenceGeneratorService sequenceGeneratorService, RestTemplate restTemplate, @Lazy AssessmentDetailsService calculatedRepo) {
	        this.restTemplate = restTemplate;
	        this.calculatedRepo = calculatedRepo;
	        this.sequenceGeneratorService = sequenceGeneratorService;
	    }
	

	Map<Long, List<String>> questionIdsWithAnswers = new HashMap<>();
	//***************************************************************************************************



	@Override
	public void saveResult(ResultAndAnalysis resultAndAnalysis) {

		repository.save(resultAndAnalysis);
	}

	//****************************** Method for SubtopicsAndDifficultyLevelCount *****************************
	
	@Override
	public QuestionPaperSubtopics getSubtopicsAndDifficultyLevelCount(Long assessmentId) {
	    try {
	        // Construct the URL with the assessmentId as a path parameter
	        String url = "http://172.18.4.37:9094/questionPaper/counts/" + assessmentId;

	        // Make HTTP GET request to the provided URL and retrieve the response entity
	        ResponseEntity<QuestionPaperSubtopics> responseEntity = restTemplate.getForEntity(url, QuestionPaperSubtopics.class);

	        // Get the body of the response entity
	        QuestionPaperSubtopics subtopicsAndDiffCount = responseEntity.getBody();
	        return subtopicsAndDiffCount;
	    }catch (ResourceAccessException e) { 
			// Handling RestClientExceptions
			String errorMessage = "Unable to fetch difficulty and subtopic level counts from the endpoint: " + e.getMessage();
			System.err.println(errorMessage); // Log the error message to console
			throw new SubtopicsAndDifficultyLevelCountNotFoundException(errorMessage);
		}
	}

	//*************************************************************************//
	private static int calculateMarksForQuestion(List<String> userAnswerList, List<String> correctAnswerList) {
		if (userAnswerList == null || correctAnswerList == null) {
			return 0; // Handle null case
		}
		if (userAnswerList.equals(correctAnswerList)) {
			return 1; // All correct
		}
		return 0; // Incorrect
	}

	public static QuestionTestdto getQuestionById(List<QuestionTestdto> questions, int questionId) {
		for (QuestionTestdto question : questions) {
			if (question.getQuestionId() == questionId) {
				return question;
			}
		}
		return null; 
	}
	public Map<Long,List<String>> extractQuestionIdsWithAnswers(List<QuestionTestdto> questions) {
		for (QuestionTestdto question : questions) {
			questionIdsWithAnswers.put(question.getQuestionId(), question.getAnswers());
		}
		return questionIdsWithAnswers;
	}

	//******************************Method for getQuestionsByIds*******************************************//
	
	
	public List<QuestionTestdto> getQuestionsByIds(List<Long> questionNumbers) {
		// Convert questionNumbers to a comma-separated string
		String questionNumbersString = questionNumbers.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(","));

		// Define the URL with the query parameter to get question list from QuestionBank
		String url = "http://172.18.4.37:9094/questions/getQuestions?questionNumbers=" + questionNumbersString;

		try {
			// Make HTTP GET request to the provided URL and retrieve the response entity
			ResponseEntity<List<QuestionTestdto>> responseEntity = restTemplate.exchange(
					url,
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<List<QuestionTestdto>>() {}
					);

			// Check if the response entity has a successful status code
			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				// Extract the list of QuestionTestdto objects from the response body
				return responseEntity.getBody();
			} else {
				// Handle unsuccessful response
				//System.out.println("Failed to retrieve questions. Status code: " + responseEntity.getStatusCodeValue());
				return new ArrayList<>();
			}
		} catch (ResourceAccessException e) {
			// Log the error
			String errorMessage = "Failed to fetch questions: " + e.getMessage();
			System.err.println(errorMessage);
			// Throw a custom exception indicating failure to fetch questions
			throw new QuestionsFetchFailedException(errorMessage);
		}
	}


	//****************************** Method for getByAssessmentKey (Main code)***************************//
	public void getByAssessmentKey(Long assessmentId, Long empId) {

		AssessmentKey assessmentKey = new AssessmentKey();
		assessmentKey.setAssessmentId(assessmentId);
		assessmentKey.setEmpId(empId);
		AssessmentDetails response = calculatedRepo.getAssessmentById(assessmentKey);
	//	System.out.println(response);

        //************** Assigning values for Entity **************//
		
		ResultAndAnalysis resultAndAnalysis = new ResultAndAnalysis();
		resultAndAnalysis.setAssessmentId(response.getId().getAssessmentId());
		resultAndAnalysis.setUserId(assessmentKey.getEmpId());
		resultAndAnalysis.setAssessmentName(response.getAssessmentName());
		resultAndAnalysis.setResultId(sequenceGeneratorService.generateSequence("result_and_analysis"));
		
		//////////////// Below values are Enums in Exact code //////////
		
        resultAndAnalysis.setTypeOfAssessment(response.getAssessmentType());
        resultAndAnalysis.setTypeOfSubmission(response.getSubmissionType());
        resultAndAnalysis.setStatus(response.getResult());
        
        /////////////////////////////////////////////////////////////////
        
		resultAndAnalysis.setBatchNo(response.getTargetBatch());
		resultAndAnalysis.setSubject(response.getCourseName());
		resultAndAnalysis.setTotalQuestions(response.getNumberOfQuestions());
		resultAndAnalysis.setClearingPercentage((long)response.getPassingPercentage());
		resultAndAnalysis.setNumberOfViolations(response.getNumberOfViolations());

		Map<Integer, List<String>> userResponsesQuestionIdWithAnswers = response.getEmployeeResponses();

		//System.out.println("user QuestionId with answers " + userResponsesQuestionIdWithAnswers);

		resultAndAnalysis.setTotalMarks((long)response.getScore());

		List<Long> questionIds = new ArrayList<>();

		for (Integer questionNumber : userResponsesQuestionIdWithAnswers.keySet()) {
			questionIds.add((long) questionNumber);
		}

		//System.out.println("userIdWithAnswers extractions of QuestionIds: "+questionIds);   //QuestionIds

		List<QuestionTestdto> questions = getQuestionsByIds(questionIds);



		Map<Long,List<String>> actualQuestionIdsWithAnswers=extractQuestionIdsWithAnswers(questions);  //changed here

		Map<String, Integer> questionAttempted = new HashMap<>(); //correct and Wrong
		Integer questionNotAttempted = 0;
        Integer totalQuestions = questions.size();


		Map<String, Map<String, Integer>> calculatedMarksOfSubtopics = new HashMap<>();
		Map<String, Integer> difficultyLevelCorrectlyAnswered = new HashMap<>();

		for (Long questionId : actualQuestionIdsWithAnswers.keySet()) {
			List<String> userAnswerList = userResponsesQuestionIdWithAnswers.getOrDefault(questionId.intValue(), Collections.emptyList());
			List<String> correctAnswerList = actualQuestionIdsWithAnswers.getOrDefault(questionId.longValue(), Collections.emptyList());

			QuestionTestdto question = getQuestionById(questions, questionId.intValue());

			int marksForQuestion = calculateMarksForQuestion(userAnswerList, correctAnswerList);
			if (question != null) {
				String subTopic = question.getSubTopic(); 
				String difficultyLevel = question.getLevel();

				// Update calculatedMarksOfSubtopics
				calculatedMarksOfSubtopics.computeIfAbsent(subTopic, k -> new HashMap<>()).merge(difficultyLevel,
						marksForQuestion, Integer::sum);

				// Update difficultyLevelCorrectlyAnswered
				difficultyLevelCorrectlyAnswered.merge(difficultyLevel, marksForQuestion, Integer::sum);
			}

			// Update questionAttempted and questionNotAttempted
			if (!userAnswerList.isEmpty()) {
				// Check if the user's answer is correct
				if (marksForQuestion > 0) {
					questionAttempted.put("correct", questionAttempted.getOrDefault("correct", 0) + 1);
				} else {
					questionAttempted.put("wrong", questionAttempted.getOrDefault("wrong", 0) + 1);
				}
			} 

		}

		questionNotAttempted = totalQuestions -(questionAttempted.getOrDefault("correct", 0) + questionAttempted.getOrDefault("wrong", 0));
  
		resultAndAnalysis.setQuestionNotAttempted(questionNotAttempted);
		resultAndAnalysis.setQuestionAttempted(questionAttempted);
		resultAndAnalysis.setDifficultyLevelCorrectlyAnswered(difficultyLevelCorrectlyAnswered);
		resultAndAnalysis.setCalculatedMarksOfSubtopics(calculatedMarksOfSubtopics);


		try {
			QuestionPaperSubtopics questionPaperSubtopics = getSubtopicsAndDifficultyLevelCount(resultAndAnalysis.getAssessmentId());
			resultAndAnalysis.setQuestionPapersubtopics(questionPaperSubtopics.getQuestionSplitCount());
			resultAndAnalysis.setDifficultyLevelSplit(questionPaperSubtopics.getDifficultyLevelSplit());
		} catch (Exception e) {
			// Handle case where subtopics couldn't be fetched
			System.out.println("Failed to fetch question paper subtopics: " + e.getMessage());
		}
        
		//String the Data in Database
		repository.save(resultAndAnalysis);
	}

	
	public void getData(Long empId,  Long assessmentId) {
        
		getByAssessmentKey(assessmentId, empId);
	}	
	
	//---------------Trainee service method implementations---------------
	
	public List<?> getAssessmentsById(Long userId) {
		List<String> results = repository.findDistinctSubjectByUserId(userId);
		
		return results;
		
	}
	
//	public List<String> findDistinctSubjectByUserId(Long userId) {
//	    List<ResultAndAnalysis> users = repository.findByUserId(userId);
//	    Set<String> distinctSubjects = new HashSet<>();
//	    for (ResultAndAnalysis user : users) {
//	        distinctSubjects.add(user.getSubject());
//	    }
//	    return new ArrayList<>(distinctSubjects);
//	}
//
	
	public List<String> findDistinctSubjectByUserId(Long userId) {
	    List<ResultAndAnalysis> users = repository.findByUserId(userId);
	    
	    if(users.isEmpty()) {
	        throw new UserNotFoundException("User with ID " + userId + " not found.");
	    }
	    
	    Set<String> distinctSubjects = new HashSet<>();
	    for (ResultAndAnalysis user : users) {
	        distinctSubjects.add(user.getSubject());
	    }
	    return new ArrayList<>(distinctSubjects);
	}
	
//	public List<?> getAssessmentsByIdSubject(Long userId, String subject) {
//		List<ResultAndAnalysis> results = repository.findAssessmentFieldsByUserIDAndSubject(userId,subject);
//		
//	    return results;
//		
//	}

	public List<?> getAssessmentsByIdSubject(Long userId, String subject) {
	    // Check if user exists
	    List<AssessmentsListDto> results = repository.findAssessmentFieldsByUserIDAndSubject(userId, subject);

	    if (results.isEmpty()) {
	        throw new UserNotFoundException("User with ID " + userId + " not found.");
	    }

	    // Check if any assessment exists
	    if (results.stream().noneMatch(result -> result != null && result.getAssessmentId() != null)) {
	        throw new AssessmentNotFoundException("No assessments found for user with ID " + userId + " and subject " + subject);
	    }

	    return results;
	}
	
	@Override
	public TraineeResultDto getResultById(Long userId, Long assessmentId) {
		
		//System.out.println(!(repository.existsByUserId(userId)) +" "+ !(repository.existsByAssessmentId(assessmentId)));
		
		if(!(repository.existsByUserId(userId)) && !(repository.existsByAssessmentId(assessmentId))) {

			throw new AssessmentNotFoundException("User with userID " + userId + " and AssessmentId "+assessmentId+" is not found");

		}else if((repository.existsByUserId(userId)) && !(repository.existsByAssessmentId(assessmentId))) {
			
			throw new AssessmentNotFoundException("User with userID " + userId + " not Attempted AssessmentId "+assessmentId+" is not found");
			
		}
		else if(!repository.existsByUserId(userId)) {
			
			throw new UserNotFoundException("User with ID " + userId + " not found.");
			
		}else if(!repository.existsByAssessmentId(assessmentId)) {
			
			throw new AssessmentNotFoundException("No assessments found for with ID " + assessmentId);
			
		} 
		
		ResultAndAnalysis result =  repository.findByUserIdAndAssessmentId(userId, assessmentId);
		
		return resultAndAnalysisConveter.entityToDto(result);
	}

	 public List<CompletedAssessments> getCompletedAssessmentsBySubject(String subject) {
	        List<ResultAndAnalysis> assessments = repository.findBySubject(subject); //doubt 
	        if (assessments.isEmpty()) {
	            throw new SubjectNotFoundException("No assessments found for subject: " + subject);
	        }

	        // Use a LinkedHashSet to maintain insertion order and ensure uniqueness
	        Set<CompletedAssessments> uniqueAssessments = new LinkedHashSet<>();
	        for (ResultAndAnalysis assessment : assessments) {
	            uniqueAssessments.add(new CompletedAssessments(assessment.getAssessmentId(), assessment.getAssessmentName()));
	        }

	        return new ArrayList<>(uniqueAssessments);
	    }

	 public List<CompletedAssessments> getAssessmentsByUserIdAndSubject(Long userId, String subject) {
	        List<ResultAndAnalysis> assessments = repository.findAssessmentsByUserIdAndSubject(userId, subject);
	        if (assessments.isEmpty()) {
	            throw new AssessmentNotFoundException("No assessments found for userId: " + userId + " and subject: " + subject);
	        }
	        return assessments.stream()
	                .map(assessment -> new CompletedAssessments(assessment.getAssessmentId(), assessment.getAssessmentName()))
	                .distinct()
	                .collect(Collectors.toList());
	 }
	 
	 
	// ******************************** admin endpoints */********************************************
	 
	 
	 @Override
		public List<ResultAndAnalysis> getAll() {
			return repository.findAll();
		}
		@Override
		public Set<String> getAllBatches() {
			List<ResultAndAnalysis> results = repository.findAll();
			Set<String> set = new HashSet<>();
			for (ResultAndAnalysis result : results) {

				set.add(result.getBatchNo());

			}
			return set;
		}



		@Override
		public List<Map<String, String>> getAllViolations() {
			List<ResultAndAnalysis> results = repository.findByTypeOfSubmission("VIOLATED_SUBMISSION");
			List<Map<String, String>> list = new ArrayList<>();
			for (ResultAndAnalysis result : results) {
				Map<String, String> userViolation = new HashMap<>();
				userViolation.put("userId", String.valueOf(result.getUserId()));
				userViolation.put("batchNo", result.getBatchNo());
				userViolation.put("assessmentName", result.getAssessmentName());
				userViolation.put("assessmentId", result.getAssessmentId().toString());
				list.add(userViolation);
			}
			return list;
		}




		@Override
		public List<Map<String, String>> assessmentIds(Integer userId) throws UserNotFoundException {
			List<ResultAndAnalysis> results = repository.findByUserId(userId);
			if (!results.isEmpty()) {
				List<Map<String, String>> assessmentIdsList = new ArrayList<>();
				// Extract assessment IDs from results
				for (ResultAndAnalysis result : results) {
					Map<String, String> assessmentIdMap = new HashMap<>();
					assessmentIdMap.put("assessmentName", result.getAssessmentName());
					assessmentIdMap.put("assessmentId", result.getAssessmentId().toString());
					assessmentIdsList.add(assessmentIdMap);
				}
				return assessmentIdsList;
			} else {
				// Handle the case where no assessments are found for the given user ID
				throw new UserNotFoundException("User ID: " + userId + " not found");
			}
		}


		@Override
		public List<Map<String, String>> assesmentIdsBySubject(String subject) throws SubjectNotFoundException {
			List<ResultAndAnalysis> sub = repository.findBySubject(subject);
			List<Map<String, String>> assessmentList = new ArrayList<>();
			if (!sub.isEmpty()) {
				Set<String> uniqueAssessments = new HashSet<>();
				for (ResultAndAnalysis result : sub) {
					String assessmentId = result.getAssessmentId().toString();
					// Use a combination of assessment name and ID to ensure uniqueness
					String uniqueKey = result.getAssessmentName() + assessmentId;
					if (!uniqueAssessments.contains(uniqueKey)) {
						uniqueAssessments.add(uniqueKey);
						Map<String, String> assessmentMap = new HashMap<>();
						assessmentMap.put("assessmentName", result.getAssessmentName());
						assessmentMap.put("assessmentId", assessmentId);
						assessmentList.add(assessmentMap);
					}
				}
				return assessmentList;
			} else {
				throw new SubjectNotFoundException("Subject: " + subject + " not found");
			}
		}


		@Override
		public Set<String> getSubjectsByBatch(String batchNo) throws BatchNoNotFoundException{
			List<ResultAndAnalysis> batch = repository.findByBatchNo(batchNo); 
			if (!batch.isEmpty()) {
				Set<String> sub = new HashSet<>();
				// Extract assessment IDs from results
				for (ResultAndAnalysis result : batch) {
					sub.add(result.getSubject());
				}
				return sub;
			}
			else {
				throw new BatchNoNotFoundException("BatchNo : " + batchNo + " not found");
			}
		}


		public List<Map<String,String>> getAssesbySubjectsByBatch(String batchNo, String subject) throws RecordNotFoundException{

			List<ResultAndAnalysis> batch = repository.findByBatchNoAndSubject(batchNo,subject); 
			List<Map<String, String>> assessmentList = new ArrayList<>();
			if (!batch.isEmpty()) {
				Set<String> uniqueas = new HashSet<>();
				// Extract assessment IDs from results
				for (ResultAndAnalysis result : batch) {
					String assessmentId = result.getAssessmentId().toString();
					String uniqueKey = result.getAssessmentName() + assessmentId;
					if (!uniqueas.contains(uniqueKey)) {
						uniqueas.add(uniqueKey);
						Map<String, String> assessmentIdMap = new HashMap<>();
						assessmentIdMap.put("assessmentName",result.getAssessmentName());
						assessmentIdMap.put("assessmentId",result.getAssessmentId().toString());
						assessmentList.add(assessmentIdMap);
					}
				}
				return assessmentList;
			}
			else {
				throw new RecordNotFoundException("Record not found");
			}
		}


		@Override
		public  List<Map<String,Double>> getPassPercentage(String batchNo,Long assessmentId, String subject) throws RecordNotFoundException{
			List<ResultAndAnalysis> batch = repository.findByBatchNoAndSubjectAndAssessmentId(batchNo,subject,assessmentId); 
			if (!batch.isEmpty()) {
				String pass="PASS";
				String fail="FAIL";
				int countPass=0;
				int countFail=0;

				for (ResultAndAnalysis result : batch) {
					if(result.getStatus().equals(pass)) {
						countPass++;
					}
					else if(result.getStatus().equals(fail)) {
						countFail++;
					}
				}
				Map<String,Double> mapping = new HashMap<>();
				int total=countPass+countFail;
				double Passper=((double)countPass / total) * 100;
				double Failper=((double)countFail / total) * 100;
				Passper = Math.round(Passper * 100.0) / 100.0;
				Failper = Math.round(Failper * 100.0) / 100.0;
				List<Map<String,Double>> PassAndFail = new ArrayList<>();
				mapping.put("PASS",Passper);
				mapping.put("FAIL",Failper);
				PassAndFail.add(mapping);
				return PassAndFail;
			}
			else {
				throw new RecordNotFoundException("Record not found");
			}
		}



		//@Override
		public List<Map<String,Double>> getSubmissionType(String batchNo,Long assessmentId, String subject) throws RecordNotFoundException{
			List<ResultAndAnalysis> batch = repository.findByBatchNoAndSubjectAndAssessmentId(batchNo,subject,assessmentId); 
			Map<String,Double> mapping = new HashMap<>();
			if (!batch.isEmpty()) {
				int countNrml=0;
				int countTimeUp=0;
				int countVio=0;
				for (ResultAndAnalysis result : batch) {
					if(result.getTypeOfSubmission().equals("NORMAL_SUBMISSION")) {
						countNrml++;
					}
					else if(result.getTypeOfSubmission().equals("TIMEUP_SUBMISSION")) {
						countTimeUp++;
					}
					else if(result.getTypeOfSubmission().equals("VIOLATED_SUBMISSION")) {
						countVio++;
					}
				}
				int total=countNrml+countTimeUp+countVio;
				double nrmlper=((double)countNrml / total) * 100;
				double timeper=((double)countTimeUp / total) * 100;
				double vioper=((double)countVio / total) * 100;
				nrmlper = Math.round(nrmlper * 100.0) / 100.0;
				timeper = Math.round(timeper * 100.0) / 100.0;
				vioper = Math.round(vioper * 100.0) / 100.0;

				List<Map<String,Double>> sub = new ArrayList<>();
				mapping.put("NORMAL_SUBMISSION", nrmlper);
				mapping.put("TIMEUP_SUBMISSION", timeper);
				mapping.put("VIOLATED_SUBMISSION", vioper);
				sub.add(mapping);
				return sub;
			}
			else {
				throw new RecordNotFoundException("Record not found");
			}
		}

		


		public Map<String, Map<String, Integer>> getDifficultyLevelDetailsByBatch(String batchNo, String subject, Long assessmentId) throws RecordNotFoundException{
			List<ResultAndAnalysis> results = repository.findByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId);
			if (!results.isEmpty()) {
				Map<String, Map<String, Integer>> difficultyLevelDetails = new HashMap<>();

				// Iterate through each result
				for (ResultAndAnalysis result : results) {
					Map<String, Integer> difficultyLevelCorrectlyAnswered = result.getDifficultyLevelCorrectlyAnswered();
					Map<String, Integer> difficultyLevelTotalQuestions = result.getDifficultyLevelSplit();

					// Iterate through each difficulty level split
					for (Map.Entry<String, Integer> entry : difficultyLevelCorrectlyAnswered.entrySet()) {
						String difficultyLevel = entry.getKey();
						int correctlyAnswered = entry.getValue();
						int totalQuestions = difficultyLevelTotalQuestions.getOrDefault(difficultyLevel, 0);

						// Calculate percentage for the difficulty level
						double percentage = (double) correctlyAnswered / totalQuestions * 100;

						// Update count based on percentage range
						String range;
						if (percentage < result.getClearingPercentage()) {
							range = "less than "+result.getClearingPercentage();
						} else {
							range = "greater than "+result.getClearingPercentage();
						}


						// Update the count of users falling in the respective range for the difficulty level
						difficultyLevelDetails.computeIfAbsent(difficultyLevel, k -> new HashMap<>())
						.merge(range, 1, Integer::sum);


					}
				}
				return difficultyLevelDetails;
			}
			else {
				throw new RecordNotFoundException("Record not found");
			}

		}
		// same method present , this mthd is there is trainer service

		// 23 rd may 2024
		@Override
		public Map<String, Map<String, Integer>> getSubtopicDetailsByBatch(String batchNo, String subject, Long assessmentId) throws RecordNotFoundException {
			List<ResultAndAnalysis> results = repository.findByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId);
			if (!results.isEmpty()) {
				Map<String, Map<String, Integer>> subtopicDetails = new HashMap<>();
				for (ResultAndAnalysis result : results) {
					Map<String, Map<String, Integer>> calculatedMarksOfSubtopics = result.getCalculatedMarksOfSubtopics();

					// Iterate through each subtopic in the result
					for (Map.Entry<String, Map<String, Integer>> entry : calculatedMarksOfSubtopics.entrySet()) {
						String subtopic = entry.getKey();
						Map<String, Integer> subtopicMarks = entry.getValue();

						// Retrieve total questions for the subtopic
						int totalQuestions = 0;
						Map<String, Integer> subtopicQuestionCounts = result.getQuestionPapersubtopics().get(subtopic);
						if (subtopicQuestionCounts != null) {
							totalQuestions = subtopicQuestionCounts.values().stream().mapToInt(Integer::intValue).sum();
						}

						// Retrieve correctly answered questions for the subtopic
						int correctlyAnswered = subtopicMarks.values().stream().mapToInt(Integer::intValue).sum();
						// Calculate percentage for the subtopic
						double percentage = (double) correctlyAnswered / totalQuestions * 100;

						// Update count based on percentage range
						String range;
						if (percentage < result.getClearingPercentage()) {
							range = "less than "+ result.getClearingPercentage();
						}  else {
							range = "greater than "+result.getClearingPercentage();
						}

						// Update the count of users falling in the respective range for the subtopic
						subtopicDetails.computeIfAbsent(subtopic, k -> new HashMap<>())
						.merge(range, 1, Integer::sum);
					}
				}
				// Fetch total users based on assessment ID and batch number
			       int totalUsers = getTotalUsersByBatchAndAssessment(batchNo, assessmentId);

			       // Add total users to each subtopic detail
			       for (Map<String, Integer> detail : subtopicDetails.values()) {
			          detail.put("Total Users", totalUsers);
			       }
				return subtopicDetails;
			}
			else {
				throw new RecordNotFoundException("Record not found");
			}
		
	}

		@Override
		public List<ResultAndAnalysis> getResultsByUserId(Long userId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result determineResultStatus(ResultAndAnalysis result) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResultStatusDto determineResultStatus1(ResultAndAnalysis result) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BatchResultStatsDto getBatchResultStats(String batchNo, Long assessmentId, String subject) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BatchResultStatsDto getBatchResultStats1(String batchNo, String subject) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<BatchResultStatsDto> getBatchResultStatsForAllBatches(Long assessmentId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, Double> getPassPercentageForEachBatch(String subject, Long assessmentId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Optional<UserIdandSubjectDTOresult> getUserDetailsByUserIdAndSubject(Long userId, String subject) {
			// TODO Auto-generated method stub
			return Optional.empty();
		}

		@Override
		public Map<String, Map<String, Object>> getSubtopicDetailsByUserIdAndAssessmentId(Long userId,
				Long assessmentId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, Map<String, Integer>> getSubtopicsDetailsByBatch(String batchNo, String subject,
				Long assessmentId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, Integer> getSubmissionDetailsByBatchAndAssessment(String batchNo, String subject,
				Long assessmentId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getTotalUsersInBatchAndAssessment(String batchNo, String subject, Long assessmentId) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getTotalUsersByBatchAndAssessment(String batchNo, Long assessmentId) {
			// TODO Auto-generated method stub
			return 0;
		}

		public List<Map<String, String>> getAllViolationsAndTimeupSubmissions(String batchNo, String subject, Long assessmentId) {
	        List<ResultAndAnalysis> results = repository.findByBatchNoAndSubjectAndAssessmentIdAndTypeOfSubmission(batchNo, subject, assessmentId);
	        List<Map<String, String>> list = new ArrayList<>();
	        for (ResultAndAnalysis result : results) {
	            Map<String, String> userViolation = new HashMap<>();
	            userViolation.put("userId", String.valueOf(result.getUserId()));
	            userViolation.put("batchNo", result.getBatchNo());
	            userViolation.put("assessmentName", result.getAssessmentName());
	            userViolation.put("assessmentId", result.getAssessmentId().toString());
	            userViolation.put("typeOfSubmission", result.getTypeOfSubmission().toString());
	            list.add(userViolation);
	        }
	        return list;
	    }

		

	
		// ************************* trainer ***********************************************************
		
		
		
}

