package com.assesment.ms.takeupassessmentandsubmit.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.assesment.ms.takeupassessmentandsubmit.DTO.AssessmentsListDto;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentType;
import com.assesment.ms.takeupassessmentandsubmit.entity.ResultAndAnalysis;



public interface ResultRepository extends MongoRepository<ResultAndAnalysis, Long>  {
	
public List<String> findDistinctSubjectByUserId(Long userID);
 
	
	@Query(value = "{'userId' : ?0}", fields = "{ 'subject': 1}")
	public List<ResultAndAnalysis> findByUserId(Long userId);
 
	
	
	@Query(value = "{ 'userId' : ?0 }", fields = "{ 'subject' : 1 }")
	public List<ResultAndAnalysis> findAssessmentsByUserId(Long userId);
	
	public  ResultAndAnalysis findByUserIdAndAssessmentId(Long userId, Long assessmentId);
	
	@Query(value = "{ 'userId' : ?0, 'subject' : ?1 }", fields = "{ 'assessmentId' : 1, 'assessmentName' : 1, 'typeOfAssessment' : 1 }")
	public List<AssessmentsListDto> findAssessmentFieldsByUserIDAndSubject(Long userId, String subject);
	
	//*****************
     boolean existsByUserId(Long userId);
     boolean existsByAssessmentId(Long assessmentId);
    //*****************
     
     //public List<ResultAndAnalysis> findBySubjectName(String subject);
     
	 @Query(value = "{ 'userId' : ?0, 'subject' : ?1 }", fields = "{ 'assessmentId' : 1, 'assessmentName' : 1 }")
	    List<ResultAndAnalysis> findAssessmentsByUserIdAndSubject(Long userId, String subject);
	 
	 // pawan
	 
		public List<ResultAndAnalysis> findByUserId(Integer userId);
		public List<ResultAndAnalysis> findBySubject(String subject);
		public List<ResultAndAnalysis> findByBatchNo(String batchNo);
		public List<ResultAndAnalysis> findByBatchNoAndSubject(String batchNo,String subject);
//		public List<ResultAndAnalysis> findBySubjectAndAssessmentId(String batchNo,String subject,Long assessmentId);
//		public List<ResultAndAnalysis> findByBatchNoSubjectAndAssessmentId(String batchNo, String subject, Long assessmentId);
		public List<ResultAndAnalysis> findByBatchNoAndSubjectAndAssessmentId(String batchNo, String subject, Long assessmentId);
		public List<ResultAndAnalysis> findByTypeOfSubmission(String submissionType);
		public List<ResultAndAnalysis> findByAssessmentId(Long assessmentId);


	// ************* trainer ******************************************************	
		
	//	  List<ResultAndAnalysis> findByUserId(Long userId);

	//    List<ResultAndAnalysis> findByBatchNoAndSubject(String batchNo, String subject);

		    Optional<ResultAndAnalysis> findByUserIdAndSubject(Long userId, String subject);

		//    ResultAndAnalysis findByUserIdAndAssessmentId(Long userId, Long assessmentId);

		 //  List<ResultAndAnalysis> findByBatchNoAndSubjectAndAssessmentId(String batchNo, String subject, Long assessmentId);

		   // List<ResultAndAnalysis> findByAssessmentId(Long assessmentId);

		    List<ResultAndAnalysis> findByBatchNoAndAssessmentId(String batchNo, Long assessmentId);

		    List<ResultAndAnalysis> findByBatchNoAndAssessmentIdAndSubject(String batchNo, Long assessmentId, String subject);

		    int countTotalUsersByBatchNoAndSubjectAndAssessmentId(String batchNo, String subject, Long assessmentId);

		    boolean existsBySubject(String subject);

		  //  Collection<Object> findBySubject(String subject);

		    boolean existsByBatchNoAndSubjectAndAssessmentId(String batchNo, String subject, Long assessmentId);

		    List<ResultAndAnalysis> findBySubjectAndAssessmentIdAndTypeOfAssessment(String subject, Long assessmentId, AssessmentType normal);
		    List<ResultAndAnalysis> findByBatchNoAndSubjectAndAssessmentIdAndTypeOfSubmissionIn(
		            String batchNo, String subject, Long assessmentId, List<String> typeOfSubmissions);

			public int countByBatchNoAndAssessmentId(String batchNo, Long assessmentId);

			@Query("{ 'batchNo': ?0, 'subject': ?1, 'assessmentId': ?2, 'typeOfSubmission': { $in: ['VIOLATED_SUBMISSION', 'TIMEUP_SUBMISSION'] } }")
		   public List<ResultAndAnalysis> findByBatchNoAndSubjectAndAssessmentIdAndTypeOfSubmission(String batchNo, String subject, Long assessmentId);
		
	 
}
