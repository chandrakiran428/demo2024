package com.assesment.ms.takeupassessmentandsubmit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assesment.ms.takeupassessmentandsubmit.DTO.AssessmentDetailsInputDTO;
import com.assesment.ms.takeupassessmentandsubmit.DTO.ResultDTO;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentDetails;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentKey;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.InvalidDataException;
import com.assesment.ms.takeupassessmentandsubmit.service.AssessmentDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class Controller {
	@Autowired
	private AssessmentDetailsService assessmentDetailsService;

	// to add assessment from postman
	@PostMapping("/addassessment")
	public ResponseEntity<AssessmentDetails> createAssessment(@RequestBody AssessmentDetails assessment) {
		return ResponseEntity.ok(assessmentDetailsService.saveAssessment(assessment));
	}

	// get all assessments
	@GetMapping
	public ResponseEntity<List<AssessmentDetails>> getAllAssessments() {
		return ResponseEntity.ok(assessmentDetailsService.getAllAssessments());
	}

	// submit endpoint for frontend
	@PostMapping("/submit")
	public ResponseEntity<?> submit(@Valid @RequestBody AssessmentDetailsInputDTO inputDTO) {
		try {
			AssessmentDetails assessment = assessmentDetailsService.processAssessment(inputDTO);
			return ResponseEntity.ok(assessment);
		} catch (InvalidDataException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// return score and result to frontend
	@GetMapping("/{assessmentId}/{empId}/score-result")
	public ResponseEntity<ResultDTO> getScoreAndResult(@PathVariable("assessmentId") Long assessmentId,
			@PathVariable("empId") Long empId) {
		AssessmentKey key = new AssessmentKey(assessmentId, empId);
		AssessmentDetails assessment = assessmentDetailsService.getAssessmentById(key);

		if (assessment == null) {
			return ResponseEntity.notFound().build(); // Return 404 if not found
		}

		ResultDTO response = new ResultDTO(assessment.getScore(), assessment.getResult());

		return ResponseEntity.ok(response); // Return 200 with score and result
	}

	// get single assessment details
	@GetMapping("/getassessment/{assessmentId}/{empId}")
	public AssessmentDetails getAssessment(@PathVariable("assessmentId") Long assessmentId,
			@PathVariable("empId") Long empId) {
		AssessmentKey key = new AssessmentKey(assessmentId, empId);
		return assessmentDetailsService.getAssessmentById(key);
	}

	// delete all records in the db
	@DeleteMapping("/all")
	public void deleteAllAssessments() {
		assessmentDetailsService.deleteAll();
	}

}
