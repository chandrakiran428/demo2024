package com.assesment.ms.takeupassessmentandsubmit.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assesment.ms.takeupassessmentandsubmit.entity.ResultAndAnalysis;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.BatchNoNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.RecordNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.SubjectNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.UserNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.service.ResultService;




@RestController
@RequestMapping("/analysis")
public class AdminAnalysisController {
	@Qualifier("resultservImp")
	@Autowired
	ResultService resultService;
	
	//to get total database
	@GetMapping("/all")
	public List<ResultAndAnalysis> getAll() {
		return resultService.getAll();
	}
	@GetMapping("/allBatches")
	public Set<String> getAllBatches() {
		return resultService.getAllBatches();
	}

	//To get all userIds who submitted by Violations
	@GetMapping("violations")
	public List<Map<String, String>> getAllViolations() {
		return resultService.getAllViolations();
	} 
	//To get all assessments based on userId  
	@GetMapping("/{userId}/assessmentsByUserId")
	public  List<Map<String, String>> getAssesmentIdsByUserId(@PathVariable Integer userId) throws UserNotFoundException {
		return resultService.assessmentIds(userId);
	}

	//To get all assessments based on subject  
	@GetMapping("/{subject}/assessmentsBySubject")
	public List<Map<String,String>> getAssesmentIdsBySubject(@PathVariable String subject) throws SubjectNotFoundException{
		return resultService.assesmentIdsBySubject(subject);
	}

	//To get all subjects by batch number.  
	@GetMapping("/{batchNo}/getSubjectsByBatch")
	public Set<String> getSubjectsByBatch(@PathVariable String batchNo) throws BatchNoNotFoundException{
		return resultService.getSubjectsByBatch(batchNo);
	}

	//To get all assessments by batch number.  
	@GetMapping("/{batchNo}/{Subject}/getAssesbySubjectsByBatch")
	public  List<Map<String,String>> getAssesbySubjectsByBatch(@PathVariable String batchNo , @PathVariable String Subject) throws RecordNotFoundException{
		return resultService.getAssesbySubjectsByBatch(batchNo,Subject);
	}

	//To get Pass percentage and fail percentage 
	@GetMapping("/{batchNo}/{subject}/{assessmentId}/passfail")
	public List<Map<String,Double>> getPassPercentage(@PathVariable String batchNo,@PathVariable Long assessmentId, @PathVariable String subject) throws RecordNotFoundException{
		return resultService.getPassPercentage(batchNo,assessmentId,subject);
	}
	

	//To get percentage for each submission type
	@GetMapping("/{batchNo}/{subject}/{assessmentId}/submissionType")
	public List<Map<String,Double>> getSubmissionType(@PathVariable String batchNo,@PathVariable Long assessmentId, @PathVariable String subject) throws RecordNotFoundException{
		return resultService.getSubmissionType(batchNo,assessmentId,subject);
	}
	
	//To get percentage for each difficulty level
	@GetMapping("/{batchNo}/{subject}/{assessmentId}/difficulty-details")
	public Map<String, Map<String, Integer>> getSubtopicDetailsByBatch1(
	       @PathVariable String batchNo,
	       @PathVariable String subject,
	       @PathVariable Long assessmentId
	)throws RecordNotFoundException {
	    return resultService.getDifficultyLevelDetailsByBatch(batchNo, subject, assessmentId);
	}

	//To get percentage for each subtopic
	@GetMapping("/{batchNo}/{subject}/{assessmentId}/subtopic-details")
	public ResponseEntity<?> getSubtopicDetailsByBatch(
	       @PathVariable String batchNo,
	       @PathVariable String subject,
	       @PathVariable Long assessmentId
	)throws RecordNotFoundException {
	       Map<String, Map<String, Integer>> subtopicDetails = resultService.getSubtopicDetailsByBatch(batchNo, subject, assessmentId);
	       return ResponseEntity.ok(subtopicDetails);

	}
}

