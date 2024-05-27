package com.assesment.ms.takeupassessmentandsubmit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assesment.ms.takeupassessmentandsubmit.DTO.CompletedAssessments;
import com.assesment.ms.takeupassessmentandsubmit.DTO.TraineeResultDto;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.AssessmentNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.SubjectNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.UserNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.service.AssessmentDetailsService;
import com.assesment.ms.takeupassessmentandsubmit.service.ResultService;



@RestController
@RequestMapping("/result")
@CrossOrigin(origins = "http://172.18.4.100:5173")
public class ResultController {

	@Qualifier("resultservImp")
    @Autowired
    @Lazy
    private ResultService resultService;

    @Autowired 
    @Lazy
    private AssessmentDetailsService calculatedService;  // Convention need to change


     
    @GetMapping("/trainee/assessments/{userId}")
    public ResponseEntity<?> getAssessments(@PathVariable Long userId, @RequestParam(required = false) String subject) {
        try {
            if (subject != null) {
                List<?> assessments = resultService.getAssessmentsByIdSubject(userId, subject);
                return ResponseEntity.ok(assessments);
            } else {
                List<String> distinctSubjects = resultService.findDistinctSubjectByUserId(userId);
                return ResponseEntity.ok(distinctSubjects);
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching assessments: " + e.getMessage());
        }
    }


    
    @GetMapping("/{userId}/{assessmentId}")
    public ResponseEntity<?> getResultById(@PathVariable Long userId, @PathVariable Long assessmentId) {
        try {
            TraineeResultDto resultDto = resultService.getResultById(userId, assessmentId);
            return ResponseEntity.ok(resultDto);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AssessmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Handle case where either userId or assessmentId is not provided
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching trainee report: " + e.getMessage());
        }
    }

    @GetMapping("/completedAssessments/{subject}")
    public ResponseEntity<?> getCompletedAssessments(@PathVariable String subject) {
        try {
            List<CompletedAssessments> assessments = resultService.getCompletedAssessmentsBySubject(subject);
            return ResponseEntity.ok(assessments);
        } catch (SubjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching completed assessments: " + e.getMessage());
        }
    }
    
    @GetMapping("/trainee/{userId}/{subject}")
    public ResponseEntity<?> getAssessmentsByUserId(@PathVariable Long userId, @PathVariable String subject) {
        try {
            List<CompletedAssessments> assessments = resultService.getAssessmentsByUserIdAndSubject(userId, subject);
            return ResponseEntity.ok(assessments);
        } catch (AssessmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching assessments: " + e.getMessage());
        }
    }
    
    @GetMapping("/violations")
    public List<Map<String, String>> getViolationsAndTimeupSubmissions(
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String subject,
            @RequestParam Long assessmentId) {
        return resultService.getAllViolationsAndTimeupSubmissions(batchNo, subject, assessmentId);
    }

}

