package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPaperSubtopics {

	
	@JsonProperty("questionSplitCount")
	private Map<String, Map<String, Integer>> questionSplitCount;

 
    
    @JsonProperty("difficultyLevelSplit")
    private Map<String, Integer>  difficultyLevelSplit;
    
	
}
