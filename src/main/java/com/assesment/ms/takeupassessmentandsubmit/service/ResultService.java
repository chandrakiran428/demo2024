package com.assesment.ms.takeupassessmentandsubmit.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.assesment.ms.takeupassessmentandsubmit.DTO.BatchResultStatsDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.CompletedAssessments;
import com.assesment.ms.takeupassessmentandsubmit.DTO.QuestionPaperSubtopics;
import com.assesment.ms.takeupassessmentandsubmit.DTO.ResultStatusDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.TraineeResultDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.UserIdandSubjectDTOresult;
import com.assesment.ms.takeupassessmentandsubmit.entity.Result;
import com.assesment.ms.takeupassessmentandsubmit.entity.ResultAndAnalysis;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.BatchNoNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.RecordNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.SubjectNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.UserNotFoundException;


public interface ResultService {

	public void saveResult(ResultAndAnalysis resultAndAnalysis);
	//public void detailCalculation();
	public QuestionPaperSubtopics getSubtopicsAndDifficultyLevelCount(Long assessmentId);
	TraineeResultDto getResultById(Long userId, Long assessmentId);
	List<?> getAssessmentsById(Long userId);
	List<?> getAssessmentsByIdSubject(Long userId, String subject);
	List<String> findDistinctSubjectByUserId(Long userId);
	public List<CompletedAssessments> getCompletedAssessmentsBySubject(String subject);
	public List<CompletedAssessments> getAssessmentsByUserIdAndSubject(Long userId, String subject);
	
	
	List<Map<String, String>> assessmentIds(Integer userId) throws UserNotFoundException;

	List<Map<String,String>> assesmentIdsBySubject(String subject) throws SubjectNotFoundException;

	Set<String> getSubjectsByBatch(String batchNo) throws BatchNoNotFoundException;

	List<Map<String,String>> getAssesbySubjectsByBatch(String batchNo, String subject) throws RecordNotFoundException;

	List<Map<String,Double>> getPassPercentage(String batchNo,Long assessmentId, String subject) throws RecordNotFoundException;

	List<Map<String,Double>> getSubmissionType(String batchNo,Long assessmentId, String subject) throws RecordNotFoundException;
	
	Map<String, Map<String, Integer>> getDifficultyLevelDetailsByBatch(String batchNo, String subject, Long assessmentId) throws RecordNotFoundException;

	Map<String, Map<String, Integer>> getSubtopicDetailsByBatch(String batchNo, String subject, Long assessmentId) throws RecordNotFoundException;

	List<Map<String, String>> getAllViolations();

	List<ResultAndAnalysis> getAll();

	Set<String> getAllBatches();

 // trainerr*************************************************
	
	
//	void saveResult(ResultAndAnalysis resultAndAnalysis);

    List<ResultAndAnalysis> getResultsByUserId(Long userId);  //checked

    Result determineResultStatus(ResultAndAnalysis result);

    ResultStatusDto determineResultStatus1(ResultAndAnalysis result);

    //BatchResultStatsDto getBatchResultStats(String batchNo);

    public BatchResultStatsDto getBatchResultStats(String batchNo, Long assessmentId, String subject);
    BatchResultStatsDto getBatchResultStats1(String batchNo,String subject);

  //  List<ResultStatusDto> getAllResultStatus();

    List<BatchResultStatsDto> getBatchResultStatsForAllBatches(Long assessmentId);

    Map<String, Double> getPassPercentageForEachBatch(String subject,Long assessmentId);

//  Map<String, Double> getPassPercentageForEachBatchReassessment(String subject);

//  Map<String, Double> getPassPercentageForEachBatchNormal(String subject, Long assessmentId);
    Optional<UserIdandSubjectDTOresult> getUserDetailsByUserIdAndSubject(Long userId, String subject);

//    Map<String, Double> getScoreAndPercentageByUserIdAndAssessmentId(Long userId, Long assessmentId);

    Map<String, Map<String, Object>> getSubtopicDetailsByUserIdAndAssessmentId(Long userId, Long assessmentId);

 //   Map<String, Map<String, Integer>> getDifficultyLevelDetailsByBatch(String batchNo, String subject, Long assessmentId);
    Map<String, Map<String, Integer>> getSubtopicsDetailsByBatch(String batchNo, String subject, Long assessmentId);
//  Map<String, Integer> getSubtopicDetailsByBatch(String batchNo, String subject, Long assessmentId);

    Map<String, Integer> getSubmissionDetailsByBatchAndAssessment(String batchNo, String subject, Long assessmentId);

    int getTotalUsersInBatchAndAssessment(String batchNo, String subject, Long assessmentId);


    int getTotalUsersByBatchAndAssessment(String batchNo, Long assessmentId);
	public List<Map<String, String>> getAllViolationsAndTimeupSubmissions(String batchNo, String subject,
			Long assessmentId);
	

}
