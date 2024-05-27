package com.assesment.ms.takeupassessmentandsubmit.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Builder
@NoArgsConstructor
@ToString
@Document(value="result_and_analysis")
public class ResultAndAnalysis {
    @Transient
    public static final String SEQUENCE_NAME = "result_and_analysis_id";
	@Id
    private Long resultId;
	
    private String batchNo;               // TakeUp&Submit endpoint
    private Long userId;                   // TakeUp&Submit endpoint
    private Long assessmentId;            // TakeUp&Submit endpoint
    private String assessmentName;       // TakeUp&Submit endpoint
    private String subject;             // TakeUp&Submit endpoint           
    private Map<String, Map<String, Integer>> calculatedMarksOfSubtopics; // Subtopic -> Difficulty Level -> Correctly Answered
    private Map<String, Integer> difficultyLevelCorrectlyAnswered;
    private Map<String, Integer> questionAttempted; // "crt" for correct, "wrong" for wrong
    private Integer questionNotAttempted;
    private Integer numberOfViolations;    // TakeUp&Submit endpoint
  
    private Long totalMarks;               // TakeUp&Submit endpoint
    private Map<String, Integer> difficultyLevelSplit; // Total Questions, Easy, Medium, Hard (QP endPoint)
    private Integer totalQuestions;  // total questions in that assessment 

   
    private Long clearingPercentage;                        //sprint:1 endpoint
    //private Map<Integer, List<String>> userAnswers;    // user attempted  answers from TakeUp&Submit endpoint
    private Map<String, Map<String, Integer>> questionPapersubtopics; //received from QP microservice 
    
    //*******************************
    private Result status;                                  // "pass" or "fail"
    private SubmissionType typeOfSubmission;                        // TakeUp&Submit endpoint
    private AssessmentType typeOfAssessment;       // "regular" or "re-assessment" from  TakeUp&Submit endpoint

    
    public ResultAndAnalysis(Long resultId, String batchNo, Long userId, Long assessmentId, String assessmentName, String subject,
            Map<String, Map<String, Integer>> calculatedMarksOfSubtopics, Map<String, Integer> difficultyLevelCorrectlyAnswered,
            Map<String, Integer> questionAttempted, Integer questionNotAttempted, Integer numberOfViolations,
            Long totalMarks, Map<String, Integer> difficultyLevelSplit, Integer totalQuestions,
            Long clearingPercentage, Map<String, Map<String, Integer>> questionPapersubtopics, Result status,
            SubmissionType typeOfSubmission, AssessmentType typeOfAssessment) {
this.resultId = resultId;
this.batchNo = batchNo;
this.userId = userId;
this.assessmentId = assessmentId;
this.assessmentName = assessmentName;
this.subject = subject;
this.calculatedMarksOfSubtopics = calculatedMarksOfSubtopics;
this.difficultyLevelCorrectlyAnswered = difficultyLevelCorrectlyAnswered;
this.questionAttempted = questionAttempted;
this.questionNotAttempted = questionNotAttempted;
this.numberOfViolations = numberOfViolations;
this.totalMarks = totalMarks;
this.difficultyLevelSplit = difficultyLevelSplit;
this.totalQuestions = totalQuestions;
this.clearingPercentage = clearingPercentage;
this.questionPapersubtopics = questionPapersubtopics;
this.status = status;
this.typeOfSubmission = typeOfSubmission;
this.typeOfAssessment = typeOfAssessment;
}
    public Result getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(Result status) {
        this.status = status;
    }  
}
