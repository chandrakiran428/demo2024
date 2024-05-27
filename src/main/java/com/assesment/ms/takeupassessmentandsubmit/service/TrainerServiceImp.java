package com.assesment.ms.takeupassessmentandsubmit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.assesment.ms.takeupassessmentandsubmit.DTO.BatchResultStatsDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.CompletedAssessments;
import com.assesment.ms.takeupassessmentandsubmit.DTO.QuestionPaperSubtopics;
import com.assesment.ms.takeupassessmentandsubmit.DTO.ResultStatusDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.TraineeResultDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.UserIdandSubjectDTOresult;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentType;
import com.assesment.ms.takeupassessmentandsubmit.entity.Result;
import com.assesment.ms.takeupassessmentandsubmit.entity.ResultAndAnalysis;
import com.assesment.ms.takeupassessmentandsubmit.entity.SubmissionType;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.BatchNoNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.RecordNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.ResultServiceException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.SubjectNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.UserNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.repository.ResultRepository;
import com.assesment.ms.takeupassessmentandsubmit.resultAndAnalysisConverter.ResultAndAnalysisConverter;

import jakarta.validation.Validator;


@Service("TrainerServImp")
public class TrainerServiceImp implements ResultService {

	
	@Autowired
	 private ResultRepository repository;


	    @Autowired
	    private Validator validator;

	   

	    @Override
	    public ResultStatusDto determineResultStatus1(ResultAndAnalysis result) {  //not required
	       ResultStatusDto resultStatusDto = new ResultStatusDto();
	       resultStatusDto.setResultId(result.getResultId());
	       resultStatusDto.setUserId(result.getUserId());
	       resultStatusDto.setBatchNo(result.getBatchNo());
	       resultStatusDto.setStatus(result.getStatus());
	       return resultStatusDto;
	    }


//	    @Override
//	    public List<ResultStatusDto> getAllResultStatus() {  //not required
//	       List<ResultAndAnalysis> results = repository.findAll();
//	       List<ResultStatusDto> resultStatusList = new ArrayList<>();
//	       int passCount = 0;
//	       int totalCount = results.size();
//	       for (ResultAndAnalysis result : results) {
//	          Result status = result.getStatus();
//	          if ("pass".equals(status)) {
//	             passCount++;
//	          }
//
//	          ResultStatusDto resultStatusDto = new ResultStatusDto();
//	          resultStatusDto.setResultId(result.getResultId());
//	          resultStatusDto.setUserId(result.getUserId());
//	          resultStatusDto.setBatchNo(result.getBatchNo());
//	          resultStatusDto.setStatus(status);
//	          resultStatusList.add(resultStatusDto);
//	       }
//
//	       double passPercentage = (double) passCount / totalCount * 100;
//	       ResultStatusDto passPercentageDto = new ResultStatusDto();
//	       passPercentageDto.setBatchNo("All Users");
//	       passPercentageDto.setStatus("Pass Percentage: " + passPercentage + "%");
//	       resultStatusList.add(passPercentageDto);
//
//	       return resultStatusList;
//	    }

	    @Override
	    public List<BatchResultStatsDto> getBatchResultStatsForAllBatches(Long assessmentId) {
	       List<ResultAndAnalysis> results = repository.findByAssessmentId(assessmentId);

	       if (results.isEmpty()) {
	          throw new ResultServiceException("Assessment ID not found: " + assessmentId);
	       }

	       Map<String, Integer> passCountPerBatch = new HashMap<>();
	       Map<String, Integer> totalUsersPerBatch = new HashMap<>();

	       for (ResultAndAnalysis result : results) {
	          String batchNo = result.getBatchNo();
	          if (Result.PASS.equals(result.getStatus())) {
	             passCountPerBatch.put(batchNo, passCountPerBatch.getOrDefault(batchNo, 0) + 1);
	          }
	          totalUsersPerBatch.put(batchNo, totalUsersPerBatch.getOrDefault(batchNo, 0) + 1);
	       }

	       List<BatchResultStatsDto> batchResultStatsList = new ArrayList<>();
	       for (Map.Entry<String, Integer> entry : totalUsersPerBatch.entrySet()) {
	          String batchNo = entry.getKey();
	          int totalUsers = entry.getValue();
	          int passCount = passCountPerBatch.getOrDefault(batchNo, 0);
	          double passPercentage = (double) passCount / totalUsers * 100;

	          BatchResultStatsDto batchResultStatsDto = new BatchResultStatsDto();
	          batchResultStatsDto.setBatchNo(batchNo);
	          batchResultStatsDto.setTotalUsers(totalUsers);
	          batchResultStatsDto.setPassCount(passCount);
	          batchResultStatsDto.setPassPercentage(passPercentage);

	          batchResultStatsList.add(batchResultStatsDto);
	       }

	       return batchResultStatsList;
	    }

	    @Override
	    public Map<String, Double> getPassPercentageForEachBatch(String subject,Long assessmentid) {  //required
	       List<ResultAndAnalysis> results = repository.findBySubjectAndAssessmentIdAndTypeOfAssessment(subject, assessmentid,AssessmentType.NORMAL);
	       Map<String, Integer> batchCountMap = new HashMap<>();
	       Map<String, Integer> passedBatchCountMap = new HashMap<>();
	       for (ResultAndAnalysis result : results) {
	          batchCountMap.put(result.getBatchNo(), batchCountMap.getOrDefault(result.getBatchNo(), 0) + 1);
	          if (Result.PASS.equals(result.getStatus())) {
	             passedBatchCountMap.put(result.getBatchNo(), passedBatchCountMap.getOrDefault(result.getBatchNo(), 0) + 1);
	          }
	       }
	       Map<String, Double> passPercentageMap = new HashMap<>();
	       for (Map.Entry<String, Integer> entry : batchCountMap.entrySet()) {
	          String batchNo = entry.getKey();
	          int totalRegularAssessments = entry.getValue();
	          int passedAssessments = passedBatchCountMap.getOrDefault(batchNo, 0);
	          double passPercentage = (double) passedAssessments / totalRegularAssessments * 100;
	          passPercentageMap.put(batchNo, passPercentage);
	       }

	       return passPercentageMap;
	    }


 // 23rd may 2024
         @Override
	    public BatchResultStatsDto getBatchResultStats(String batchNo, Long assessmentId, String subject) {  //required
	       List<ResultAndAnalysis> results = repository.findByBatchNoAndAssessmentIdAndSubject(batchNo, assessmentId, subject);
	       if (results.isEmpty()) {
	          throw new ResultServiceException("Batch number " + batchNo + " with assessment ID " + assessmentId + " and subject " + subject + " not found.");
	       }
	       int totalUsers = results.size();
	       int passCount = 0;
	       for (ResultAndAnalysis result : results) {
	          if (Result.PASS.equals(result.getStatus())) {
	             passCount++;
	          }
	       }
	       double passPercentage = (double) passCount / totalUsers * 100;
	       BatchResultStatsDto batchResultStatsDto = new BatchResultStatsDto();
	       batchResultStatsDto.setBatchNo(batchNo);
	       batchResultStatsDto.setTotalUsers(totalUsers);
	       batchResultStatsDto.setPassCount(passCount);
	       batchResultStatsDto.setPassPercentage(passPercentage);
	       return batchResultStatsDto;
	    }

	    @Override
	    public BatchResultStatsDto getBatchResultStats1(String batchNo, String subject) {   //not required
	       // Check if the subject exists in the database
	       if (!repository.existsBySubject(subject)) {
	          throw new ResultServiceException("Subject not found: " + subject);
	       }

	       List<ResultAndAnalysis> results = repository.findByBatchNoAndSubject(batchNo, subject);

	       if (results.isEmpty()) {
	          throw new ResultServiceException("No results found for batch: " + batchNo + ", subject: " + subject);
	       }

	       int totalUsers = results.size();
	       int passCount = 0;

	       for (ResultAndAnalysis result : results) {
	          // Count pass status directly from the entity
	          if (result.getStatus().equals("PASS")) {
	             passCount++;
	          }
	       }

	       double passPercentage = (double) passCount / totalUsers * 100;

	       BatchResultStatsDto batchResultStatsDto = new BatchResultStatsDto();
	       batchResultStatsDto.setBatchNo(batchNo);
	       batchResultStatsDto.setTotalUsers(totalUsers);
	       batchResultStatsDto.setPassCount(passCount);
	       batchResultStatsDto.setPassPercentage(passPercentage);

	       return batchResultStatsDto;
	    }

	    public Optional<UserIdandSubjectDTOresult> getUserDetailsByUserIdAndSubject(Long userId, String subject) {    //not required
	       Optional<ResultAndAnalysis> result = repository.findByUserIdAndSubject(userId, subject);
	       if (!result.isPresent()) {
	          throw new UserNotFoundException("User with ID " + userId + " not found for subject " + subject);
	       }
	       return result.map(r -> new UserIdandSubjectDTOresult(
	             r.getBatchNo(),
	             r.getCalculatedMarksOfSubtopics(),
	             r.getDifficultyLevelCorrectlyAnswered(),
	             r.getTotalQuestions(),
	             calculatePercentage(r.getTotalQuestions(), r.getDifficultyLevelCorrectlyAnswered())
	       ));
	    }



	    private double calculatePercentage(Integer totalQuestions, Map<String, Integer> difficultyLevelCorrectlyAnswered) {
	       int totalCorrect = difficultyLevelCorrectlyAnswered.values().stream().mapToInt(Integer::intValue).sum();
	       return (totalCorrect * 100.0) / totalQuestions;
	    }



	    public Map<String, Map<String, Object>> getSubtopicDetailsByUserIdAndAssessmentId(Long userId, Long assessmentId) {
	       ResultAndAnalysis result = repository.findByUserIdAndAssessmentId(userId, assessmentId);
	       if (result == null) {
	          throw new ResultServiceException("No result found for the provided user ID and assessment ID");
	       }

	       Map<String, Map<String, Integer>> calculatedMarksOfSubtopics = result.getCalculatedMarksOfSubtopics();
	       Map<String, Map<String, Integer>> questionPapersubtopics = result.getQuestionPapersubtopics();

	       if (questionPapersubtopics == null) {
	          throw new ResultServiceException("Question papersubtopics is null");
	       }

	       Map<String, Map<String, Object>> subtopicDetails = new HashMap<>();

	       for (Map.Entry<String, Map<String, Integer>> entry : calculatedMarksOfSubtopics.entrySet()) {
	          String subtopic = entry.getKey();
	          Map<String, Integer> marks = entry.getValue();
	          Map<String, Integer> questionCounts = questionPapersubtopics.get(subtopic);

	          if (questionCounts == null) {
	             throw new ResultServiceException("Question counts for subtopic " + subtopic + " are null");
	          }

	          int totalQuestions = questionCounts.values().stream().mapToInt(Integer::intValue).sum();
	          int correctlyAnswered = marks.values().stream().mapToInt(Integer::intValue).sum();
	          double percentage = (double) correctlyAnswered / totalQuestions * 100;

	          Map<String, Object> subtopicDetail = new HashMap<>();
	          subtopicDetail.put("totalQuestions", totalQuestions);
	          subtopicDetail.put("correctlyAnswered", correctlyAnswered);
	          subtopicDetail.put("percentage", percentage);

	          subtopicDetails.put(subtopic, subtopicDetail);
	       }

	       return subtopicDetails;
	    }


	    public boolean isBatchExists(String batchNo, String subject, Long assessmentId) {
	       // Check if the batch exists for the given subject and assessment ID
	       return repository.existsByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId);
	    }
	


	    public Map<String, Map<String, Integer>> getSubtopicsDetailsByBatch(String batchNo, String subject, Long assessmentId) {
	       if (!isBatchExists(batchNo, subject, assessmentId)) {
	          throw new ResultServiceException("Result not found for the above details");
	       }

	       List<ResultAndAnalysis> results = repository.findByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId);

	       Map<String, Map<String, Integer>> subtopicDetails = new HashMap<>();

	       // Iterate through each result
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
	                range = "less than " + result.getClearingPercentage();
	             } else {
	                range = "greater than " + result.getClearingPercentage();
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

	    public Map<String, Map<String, Integer>> getDifficultyLevelDetailsByBatch(String batchNo, String subject, Long assessmentId) {  //required
	        // Check if the batch exists for the given subject and assessment ID
	        if (!isBatchExists(batchNo, subject, assessmentId)) {
	           throw new ResultServiceException("Resultt not found for the above details");
	        }

	        List<ResultAndAnalysis> results = repository.findByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId);

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
	                 range = "less than " + result.getClearingPercentage();
	              } else {
	                 range = "greater than " + result.getClearingPercentage();
	              }

	              // Update the count of users falling in the respective range for the difficulty level
	              difficultyLevelDetails.computeIfAbsent(difficultyLevel, k -> new HashMap<>())
	                    .merge(range, 1, Integer::sum);
	           }
	        }

	        return difficultyLevelDetails;
	     }
	    
//	    public Map<String, Map<String, Integer>> getDifficultyLevelDetailsByBatch(String batchNo, String subject, Long assessmentId) throws RecordNotFoundException {  
//	        // Check if the batch exists for the given subject and assessment ID
//	        if (!isBatchExists(batchNo, subject, assessmentId)) {
//	            throw new ResultServiceException("Result not found for the above details");
//	        }
//
//	        List<ResultAndAnalysis> results = repository.findByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId);
//
//	        if (results.isEmpty()) {
//	            throw new RecordNotFoundException("Record not found");
//	        }
//
//	        Map<String, Map<String, Integer>> difficultyLevelDetails = new HashMap<>();
//
//	        // Iterate through each result
//	        for (ResultAndAnalysis result : results) {
//	            // Logic for processing each result
//	            Map<String, Integer> difficultyLevelCorrectlyAnswered = result.getDifficultyLevelCorrectlyAnswered();
//	            Map<String, Integer> difficultyLevelTotalQuestions = result.getDifficultyLevelSplit();
//
//	            // Iterate through each difficulty level split
//	            for (Map.Entry<String, Integer> entry : difficultyLevelCorrectlyAnswered.entrySet()) {
//	                String difficultyLevel = entry.getKey();
//	                int correctlyAnswered = entry.getValue();
//	                int totalQuestions = difficultyLevelTotalQuestions.getOrDefault(difficultyLevel, 0);
//
//	                // Calculate percentage for the difficulty level
//	                double percentage = (double) correctlyAnswered / totalQuestions * 100;
//
//	                // Update count based on percentage range
//	                String range = (percentage < result.getClearingPercentage()) ? "less than " + result.getClearingPercentage() : "greater than " + result.getClearingPercentage();
//
//	                // Update the count of users falling in the respective range for the difficulty level
//	                difficultyLevelDetails.computeIfAbsent(difficultyLevel, k -> new HashMap<>())
//	                    .merge(range, 1, Integer::sum);
//	            }
//	        }
//
//	        return difficultyLevelDetails;
//	    }


	    
	    
	    public int getTotalUsersByBatchAndAssessment(String batchNo, Long assessmentId) {
	       // Fetch the total users for the given batch number and assessment ID
	       return repository.countByBatchNoAndAssessmentId(batchNo, assessmentId);
	    }






	    @Override
	    public List<ResultAndAnalysis> getResultsByUserId(Long userId) throws ResultServiceException {
	       if (userId == null || userId == 0) {
	          throw new ResultServiceException("User ID cannot be null or zero");
	       }
	       List<ResultAndAnalysis> results = repository.findByUserId(userId);
	       if (results == null || results.isEmpty()) {
	          throw new ResultServiceException("No results found for user ID: " + userId);
	       }
	       return results;
	    }

	    @Override
	    public Result determineResultStatus(ResultAndAnalysis result) {
	        if (result == null) {
	            throw new ResultServiceException("Result cannot be null");
	        }

	        Result status = result.getStatus();

	        if (status == null) {
	            throw new ResultServiceException("Result status cannot be null");
	        }

	        // Additional logic for determining the result status can be added here.

	        return status;
	    }


	    public Map<String, Integer> getSubmissionDetailsByBatchAndAssessment(String batchNo, String subject, Long assessmentId) {  //required
	       List<ResultAndAnalysis> results = repository.findByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId);
	       if (results.isEmpty()) {
	          throw new ResultServiceException("No results found for batch: " + batchNo + ", subject: " + subject + ", assessment ID: " + assessmentId);
	       }
	       Map<String, Integer> submissionCountMap = new HashMap<>();
	       submissionCountMap.put("NORMAL_SUBMISSION", 0);
	       submissionCountMap.put("TIMEUP_SUBMISSION", 0);
	       submissionCountMap.put("VIOLATED_SUBMISSION", 0); // Initialize NORMAL_SUBMISSION count to 0

	       for (ResultAndAnalysis result : results) {
	            SubmissionType submissionType = result.getTypeOfSubmission();
	            switch (submissionType) {
	                case NORMAL_SUBMISSION:
	                    submissionCountMap.put("NORMAL_SUBMISSION", submissionCountMap.get("NORMAL_SUBMISSION") + 1);
	                    break;
	                case TIMEUP_SUBMISSION:
	                    submissionCountMap.put("TIMEUP_SUBMISSION", submissionCountMap.get("TIMEUP_SUBMISSION") + 1);
	                    break;
	                case VIOLATED_SUBMISSION:
	                    submissionCountMap.put("VIOLATED_SUBMISSION", submissionCountMap.get("VIOLATED_SUBMISSION") + 1);
	                    break;
	                // Add more cases if there are other submission types
	                default:
	                    // Handle unrecognized submission types if needed
	            }
	        }
	       return submissionCountMap;
	    }


	    public int getTotalUsersInBatchAndAssessment(String batchNo, String subject, Long assessmentId) {
	       // Retrieve total number of users based on batchNo, subject, and assessmentId
	       return repository.countTotalUsersByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId);
	    }


		@Override
		public void saveResult(ResultAndAnalysis resultAndAnalysis) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public QuestionPaperSubtopics getSubtopicsAndDifficultyLevelCount(Long assessmentId) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public TraineeResultDto getResultById(Long userId, Long assessmentId) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<?> getAssessmentsById(Long userId) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<?> getAssessmentsByIdSubject(Long userId, String subject) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<String> findDistinctSubjectByUserId(Long userId) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<CompletedAssessments> getCompletedAssessmentsBySubject(String subject) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<CompletedAssessments> getAssessmentsByUserIdAndSubject(Long userId, String subject) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<Map<String, String>> assessmentIds(Integer userId) throws UserNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<Map<String, String>> assesmentIdsBySubject(String subject) throws SubjectNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public Set<String> getSubjectsByBatch(String batchNo) throws BatchNoNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<Map<String, String>> getAssesbySubjectsByBatch(String batchNo, String subject)
				throws RecordNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<Map<String, Double>> getPassPercentage(String batchNo, Long assessmentId, String subject)
				throws RecordNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<Map<String, Double>> getSubmissionType(String batchNo, Long assessmentId, String subject)
				throws RecordNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public Map<String, Map<String, Integer>> getSubtopicDetailsByBatch(String batchNo, String subject,
				Long assessmentId) throws RecordNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<Map<String, String>> getAllViolations() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<ResultAndAnalysis> getAll() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public Set<String> getAllBatches() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<Map<String, String>> getAllViolationsAndTimeupSubmissions(String batchNo, String subject,
				Long assessmentId) {
			// TODO Auto-generated method stub
			return null;
		}


		


		


}


