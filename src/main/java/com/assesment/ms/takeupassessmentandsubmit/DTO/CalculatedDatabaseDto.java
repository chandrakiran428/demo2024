package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.util.List;
import java.util.Map;

import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatedDatabaseDto {
//check this

	private AssessmentKey id;
	private long questionPaperId;
	private long empId;
	private String assessmentName;
	//private AssessmentType assessmentType;
	private String courseName;
	private String targetBatch;
	private Integer numberOfQuestions;
	private Integer passingPercentage;
	//private SubmissionType submissionType;
	private Integer numberOfViolations;
	private Map<Integer, List<String>> employeeResponses;
	private Integer score;
	private String result;

}

