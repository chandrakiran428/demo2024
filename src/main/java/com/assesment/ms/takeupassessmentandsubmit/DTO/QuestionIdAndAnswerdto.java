package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class QuestionIdAndAnswerdto {

	
	   private Long questionId;
	    private List<String> answers;

	    public Long getQuestionId() {
	        return questionId;
	    }

	    public List<String> getAnswers() {
	        return answers;
	    }
}
