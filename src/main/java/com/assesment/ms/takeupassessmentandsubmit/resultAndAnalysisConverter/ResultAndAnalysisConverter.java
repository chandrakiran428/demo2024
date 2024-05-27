package com.assesment.ms.takeupassessmentandsubmit.resultAndAnalysisConverter;

import com.assesment.ms.takeupassessmentandsubmit.DTO.TraineeResultDto;
import com.assesment.ms.takeupassessmentandsubmit.entity.ResultAndAnalysis;

public class ResultAndAnalysisConverter {
	 
	 public static TraineeResultDto entityToDto(ResultAndAnalysis resultAndAnalysis){
		 
	        TraineeResultDto traineeResultDto = new TraineeResultDto();
	         
	        traineeResultDto.setTotalQuestions(resultAndAnalysis.getTotalQuestions());
	        traineeResultDto.setUserId(resultAndAnalysis.getUserId());
	        
	         
	        traineeResultDto.setAssessmentName(resultAndAnalysis.getAssessmentName());
//	        traineeResultDto.setQuestionAttempted(resultAndAnalysis.getQuestionAttempted());
	        traineeResultDto.setQuestionNotAttempted(resultAndAnalysis.getQuestionNotAttempted());
	        traineeResultDto.setTotalPercentage(resultAndAnalysis.getQuestionAttempted().get("correct"),resultAndAnalysis.getTotalQuestions());
	        traineeResultDto.setNumberOfViolations(resultAndAnalysis.getNumberOfViolations());
//	        traineeResultDto.setDifficultyLevelCorrectlyAnswered(resultAndAnalysis.getDifficultyLevelCorrectlyAnswered());
//	        traineeResultDto.setCalculatedMarksOfSubtopics(resultAndAnalysis.getCalculatedMarksOfSubtopics());
	        traineeResultDto.setCalculationsOfSubtopics(resultAndAnalysis.getCalculatedMarksOfSubtopics(),resultAndAnalysis.getQuestionPapersubtopics());
	        traineeResultDto.setSubtopicLevelwiseCalculations(resultAndAnalysis.getCalculatedMarksOfSubtopics(),resultAndAnalysis.getQuestionPapersubtopics());
	        traineeResultDto.setMarksObtained(resultAndAnalysis.getQuestionAttempted());
	        traineeResultDto.setTotalAttempted();
	        traineeResultDto.setStatus(resultAndAnalysis.getStatus());
	        
	        return traineeResultDto; 
	    }
	    
//	    public ResultAndAnalysis dtoToEntity(TraineeResultDto traineeResultDto) {
//	    	
//	    	ResultAndAnalysis resultAndAnalysis = new ResultAndAnalysis();
//	    	
//	    	resultAndAnalysis.setTotalQuestions(traineeResultDto.getTotalQuestions());
////	    	resultAndAnalysis.setQuestionAttempted(traineeResultDto.getQuestionAttempted());
//	    	resultAndAnalysis.setQuestionNotAttempted(traineeResultDto.getQuestionNotAttempted());
//	    	resultAndAnalysis.setNumberOfViolations(traineeResultDto.getNumberOfViolations());
////	    	resultAndAnalysis.setDifficultyLevelCorrectlyAnswered(traineeResultDto.getDifficultyLevelCorrectlyAnswered());
////	    	resultAndAnalysis.setCalculatedMarksOfSubtopics(traineeResultDto.getCalculatedMarksOfSubtopics());
//	    	
//	    	return resultAndAnalysis;
//	    }
	    
	}