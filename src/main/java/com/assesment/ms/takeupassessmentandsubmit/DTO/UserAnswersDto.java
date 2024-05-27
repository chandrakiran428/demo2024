package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAnswersDto {
	 
	@JsonProperty()
	 private Map<Long, List<String>> userAnswers; 

}