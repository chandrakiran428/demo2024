package com.assesment.ms.takeupassessmentandsubmit.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AssessmentsListDto {
	
	
	 private Long assessmentId;
	 private String assessmentName;
	 private String typeOfAssessment;
	 
	 
	 
	public Long getAssessmentId() {
		return assessmentId;
	}
	public void setAssessmentId(Long assessmentId) {
		this.assessmentId = assessmentId;
	}
	public String getAssessmentName() {
		return assessmentName;
	}
	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}
	public String getTypeOfAssessment() {
		return typeOfAssessment;
	}
	public void setTypeOfAssessment(String typeOfAssessment) {
		this.typeOfAssessment = typeOfAssessment;
	} 

}
