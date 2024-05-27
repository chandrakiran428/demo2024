package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTestdto {
	// this is entity getting from questionBank
	private Long questionId;
    private String subject;
    private String subTopic;
    private String title;
    private Map<String, String> options;
    private String level;
    
    @JsonProperty("questionType")
    private String type;
    private String answerType;
    private List<String> answers;
    
    @Override
    public String toString() {
        return "{" +
                "questionId=" + questionId +
                ", subject='" + subject + '\'' +
                ", subTopic='" + subTopic + '\'' +
                ", title='" + title + '\'' +
                ", options=" + options +
                ", level='" + level + '\'' +
                ", type='" + type + '\'' +
                ", answerType='" + answerType + '\'' +
                ", answers=" + answers +
                '}';
    }
    
    
  

}
