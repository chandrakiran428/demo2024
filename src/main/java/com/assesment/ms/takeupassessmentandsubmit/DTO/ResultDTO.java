package com.assesment.ms.takeupassessmentandsubmit.DTO;

import com.assesment.ms.takeupassessmentandsubmit.entity.Result;

public class ResultDTO {
	private Integer score;
	 
	private Result result;

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public ResultDTO(Integer score, Result result) {
		super();
		this.score = score;
		this.result = result;
	}

	public ResultDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
