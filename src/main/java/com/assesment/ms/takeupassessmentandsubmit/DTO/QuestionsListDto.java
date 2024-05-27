package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.util.List;



import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter

@NoArgsConstructor
public class QuestionsListDto {
	
	private List<Long> questionIds ;

	public QuestionsListDto(List<Long> questionIds) {
		super();
		this.questionIds = questionIds;
	}
	

	
}
