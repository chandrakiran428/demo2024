package com.assesment.ms.takeupassessmentandsubmit.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import com.assesment.ms.takeupassessmentandsubmit.DTO.BatchResultStatsDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.ResultDTO;
import com.assesment.ms.takeupassessmentandsubmit.DTO.ResultStatusDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.UserIdandSubjectDTOresult;
import com.assesment.ms.takeupassessmentandsubmit.entity.ResultAndAnalysis;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.RecordNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.ResultServiceException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.UserNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.repository.ResultRepository;
import com.assesment.ms.takeupassessmentandsubmit.service.ResultService;

import java.util.*;


@RestController
@RequestMapping("/result")
@CrossOrigin(origins = "http://localhost:8087")
public class TrainerController {
	
@Qualifier("TrainerServImp")
 @Autowired
 private ResultService resultService;

 

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<?> getResultsByUserId(@PathVariable Long userId) {
//       try {
//          List<ResultAndAnalysis> results = resultService.getResultsByUserId(userId);
//          return ResponseEntity.ok().body(results);
//       } catch (ResultServiceException e) {
//          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//       }
//    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDetails(@Valid @RequestBody ResultAndAnalysis resultAndAnalysis) {
       try {
          resultService.saveResult(resultAndAnalysis);
          return ResponseEntity.ok("Save");
       } catch (ResultServiceException ex) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
       }
    }
    @Autowired
    private ResultRepository resultRepository; // Assuming you have a ResultRepository




//    @GetMapping("/{batchNo}/{subject}/batchstats")  // not required
//    public ResponseEntity<?> getBatchResultStats(@PathVariable String batchNo, @PathVariable String subject) {
//       try {
//          // Retrieve batch statistics based on batch number and subject
//          BatchResultStatsDto batchResultStatsDto = resultService.getBatchResultStats1(batchNo, subject);
//          // Return the batch statistics in the response body
//          return ResponseEntity.ok(batchResultStatsDto);
//       } catch (ResultServiceException ex) {
//          // Handle the custom exception and return a 404 response
//          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//       } catch (Exception ex) {
//          // Handle any other exceptions and return a 500 response
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//       }
//    }



    @GetMapping("/{batchNo}/{subject}/{assessmentId}/batchstats")  // required
    public ResponseEntity<?> getBatchResultStats(@PathVariable String batchNo, @PathVariable Long assessmentId, @PathVariable String subject) {
       try {
          // Retrieve statistics for the given batch number, assessment ID, and subject
          BatchResultStatsDto batchResultStatsDto = resultService.getBatchResultStats(batchNo, assessmentId, subject);
          return ResponseEntity.ok(batchResultStatsDto);
       } catch (ResultServiceException ex) {
          // Handle the custom exception and return a 404 response
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       } catch (Exception ex) {
          // Handle any other exceptions and return a 500 response
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
       }
    }

    //This below method is useless as we are not using resultID for getMapping so not required
//    @GetMapping("/allstatus")//mentions all details of the user present in BatchResultStatsDto
//    public ResponseEntity<List<ResultStatusDto>> getAllResultStatus() {
//       List<ResultStatusDto> resultStatusList = resultService.getAllResultStatus();
//       return ResponseEntity.ok(resultStatusList);
//    }

    @GetMapping("/batch-result-stats/{assessmentId}")    //required
    public ResponseEntity<?> getBatchResultStatsForAllBatches(@PathVariable Long assessmentId) {
       try {
          List<BatchResultStatsDto> batchResultStatsList = resultService.getBatchResultStatsForAllBatches(assessmentId);
          return new ResponseEntity<>(batchResultStatsList, HttpStatus.OK);
       } catch (ResultServiceException ex) {
          // Handle the custom exception and return a 404 response
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       } catch (Exception ex) {
          // Handle any other exceptions and return a 500 response
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
       }
    }

 
//  @GetMapping("/{subject}/{assessmentId}/normalpassPercentage")
//  public ResponseEntity<Object> getPassPercentageForEachBatchNormal(@PathVariable("subject") String subject, @PathVariable("assessmentId") Long assessmentId) {
//     try {
//        if (subject == null || subject.isEmpty()) {
//           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Subject must not be empty");
//        }
//
//        Map<String, Double> passPercentageMap = resultService.getPassPercentageForEachBatchNormal(subject, assessmentId);
//        return ResponseEntity.ok(passPercentageMap);
//     } catch (ResultServiceException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//     } catch (Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
//     }
//  }

//  @GetMapping("/{subject}/{assessmentId}/reassessmentpasspercentage")
//  public ResponseEntity<Object> getPassPercentageForEachBatchReassessment(@PathVariable("subject") String subject, @PathVariable("assessmentId") Long assessmentId) {
//     try {
//        if (subject == null || subject.isEmpty()) {
//           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Subject must not be empty");
//        }
//
//        Map<String, Double> passPercentageMap = resultService.getPassPercentageForEachBatchReassessment(subject, assessmentId);
//        return ResponseEntity.ok(passPercentageMap);
//     } catch (ResultServiceException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//     } catch (Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
//     }
//  }
//    @GetMapping("/user/details/{userId}/{subject}")  //not required
//    public ResponseEntity<?> getUserDetailsByUserIdAndSubject(@PathVariable Long userId, @PathVariable String subject) {
//       try {
//          Optional<UserIdandSubjectDTOresult> userDetails = resultService.getUserDetailsByUserIdAndSubject(userId, subject);
//          if (userDetails.isPresent()) {
//             return ResponseEntity.ok(userDetails.get());
//          } else {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//          }
//       } catch (UserNotFoundException ex) {
//          // Handle the custom exception and return a 404 response
//          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//       } catch (Exception ex) {
//          // Handle any other exceptions and return a 500 response
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//       }
//    }




//    @GetMapping("/{userId}/{assessmentId}/subtopic-details")  //not required
//    public ResponseEntity<?> getSubtopicDetailsByUserIdAndAssessmentId(
//          @PathVariable Long userId,
//          @PathVariable Long assessmentId
//    ) {
//       try {
//          Map<String, Map<String, Object>> subtopicDetails = resultService.getSubtopicDetailsByUserIdAndAssessmentId(userId, assessmentId);
//          return ResponseEntity.ok(subtopicDetails);
//       } catch (ResultServiceException ex) {
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//       }
//    }
    public boolean isBatchExists(String batchNo, String subject, Long assessmentId) {
       // Check if the batch exists for the given subject and assessment ID
       return resultRepository.existsByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId);
    }




    @GetMapping("/{batchNo}/{subject}/{assessmentId}/subtopic-details")
    public ResponseEntity<?> getSubtopicDetailsByBatch(
          @PathVariable String batchNo,
          @PathVariable String subject,
          @PathVariable Long assessmentId
    ) throws RecordNotFoundException {
       try {
          Map<String, Map<String, Integer>> subtopicDetails = resultService.getSubtopicsDetailsByBatch(batchNo, subject, assessmentId);

          // Extracting total users from any subtopic detail since it should be the same for all
          int totalUsers = !subtopicDetails.isEmpty() ? subtopicDetails.values().iterator().next().getOrDefault("Total Users", 0) : 0;

          // Remove the "Total Users" entry from each subtopic detail before returning the response
          subtopicDetails.values().forEach(detail -> detail.remove("Total Users"));

          Map<String, Object> response = new HashMap<>();
          response.put("subtopicDetails", subtopicDetails);
          response.put("totalUsers", totalUsers);

          return ResponseEntity.ok(response);
       } catch (ResultServiceException ex) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
       } catch (Exception ex) {
          throw new ResultServiceException(ex.getMessage());
       }
    }



    @GetMapping("/{batchNo}/{subject}/{assessmentId}/difficulty-details")
    public ResponseEntity<?> getDifficultyLevelDetailsByBatch(
          @PathVariable String batchNo,
          @PathVariable String subject,
          @PathVariable Long assessmentId
    ) throws RecordNotFoundException {
       try {
          Map<String, Map<String, Integer>> difficultyLevelDetails = resultService.getDifficultyLevelDetailsByBatch(batchNo, subject, assessmentId);
          int totalUsers = resultService.getTotalUsersByBatchAndAssessment(batchNo, assessmentId);

          Map<String, Object> response = new HashMap<>();
          response.put("difficultyLevelDetails", difficultyLevelDetails);
          response.put("totalUsers", totalUsers);

          return ResponseEntity.ok(response);
       } catch (ResultServiceException ex) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       }
    }


@GetMapping("/{batchNo}/{subject}/{assessmentId}/submission-details")  //required
public ResponseEntity<Map<String, Object>> getSubmissionDetailsByBatchAndAssessment(
       @PathVariable String batchNo,
       @PathVariable String subject,
       @PathVariable Long assessmentId) {
    try {
       Map<String, Integer> submissionDetails = resultService.getSubmissionDetailsByBatchAndAssessment(batchNo, subject, assessmentId);
       int totalUsers = resultService.getTotalUsersInBatchAndAssessment(batchNo, subject, assessmentId);

       Map<String, Object> response = new HashMap<>();
       response.put("submissionDetails", submissionDetails);
       response.put("totalUsers", totalUsers);

       return ResponseEntity.ok(response);
    } catch (ResultServiceException ex) {
       // Handling exceptions and returning appropriate response
       Map<String, Object> errorResponse = new HashMap<>();
       errorResponse.put("error", ex.getMessage());
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    } catch (Exception ex) {
       // Handling exceptions and returning appropriate response
       Map<String, Object> errorResponse = new HashMap<>();
       errorResponse.put("error", "An error occurred while processing the request");
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }
}

