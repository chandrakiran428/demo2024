package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.util.HashMap;
import java.util.Map;

import com.assesment.ms.takeupassessmentandsubmit.entity.Result;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TraineeResultDto {
 
	 private Integer totalQuestions;
	    private Long userId;
	    private String assessmentName;
	    private Integer totalAttempted;
	    private Integer marksObtained;
//	    private Map<String, Integer> questionAttempted;
	    private Integer questionNotAttempted;
	    private Integer numberOfViolations;
//	    private Map<String, Integer> difficultyLevelCorrectlyAnswered;
	    private HashMap<String, HashMap<String, Float>> calculationsOfSubtopics;
	    private Float totalPercentage;
	    private Result status;
	    private HashMap<String, HashMap<String, HashMap<String, Float>>> subtopicLevelwiseCalculations;
	    
	    
	 
	    public TraineeResultDto() {
	    }
	 
	    public TraineeResultDto(Integer totalQuestions, Map<String, Integer> questionAttempted, Integer questionNotAttempted, Map<String, Integer> difficultyLevelCorrectlyAnswered, Integer numberOfViolations, Map<String, Map<String, Integer>> calculatedMarksOfSubtopics) {
	        this.totalQuestions = totalQuestions;
//	        this.questionAttempted = questionAttempted;
	        this.questionNotAttempted = questionNotAttempted;
//	        this.difficultyLevelCorrectlyAnswered = difficultyLevelCorrectlyAnswered;
	        this.numberOfViolations = numberOfViolations;
//	        this.calculatedMarksOfSubtopics = calculatedMarksOfSubtopics;
	    }
	 
	    public Integer getTotalQuestions() {
	        return totalQuestions;
	    }
	 
	    public void setTotalQuestions(Integer totalQuestions) {
	        this.totalQuestions = totalQuestions;
	    }
	 
//	    public Map<String, Integer> getQuestionAttempted() {
//	        return questionAttempted;
//	    }
	//
//	    public void setQuestionAttempted(Map<String, Integer> questionAttempted) {
//	        this.questionAttempted = questionAttempted;
//	    }
	 
	    public Integer getQuestionNotAttempted() {
	        return questionNotAttempted;
	    }
	 
	    public void setQuestionNotAttempted(Integer questionNotAttempted) {
	        this.questionNotAttempted = questionNotAttempted;
	    }
	 
	    public Integer getNumberOfViolations() {
	        return numberOfViolations;
	    }
	 
	    public void setNumberOfViolations(Integer numberOfViolations) {
	        this.numberOfViolations = numberOfViolations;
	    }
	 
//	    public Map<String, Integer> getDifficultyLevelCorrectlyAnswered() {
//	        return difficultyLevelCorrectlyAnswered;
//	    }
	//
//	    public void setDifficultyLevelCorrectlyAnswered(Map<String, Integer> difficultyLevelCorrectlyAnswered) {
//	        this.difficultyLevelCorrectlyAnswered = difficultyLevelCorrectlyAnswered;
//	    }
	//
//	    public Map<String, Map<String, Integer>> getCalculatedMarksOfSubtopics() {
//	        return calculatedMarksOfSubtopics;
//	    }
	//
//	    public void setCalculatedMarksOfSubtopics(Map<String, Map<String, Integer>> calculatedMarksOfSubtopics) {
//	        this.calculatedMarksOfSubtopics = calculatedMarksOfSubtopics;
//	    }
	    
	    public void setTotalPercentage(Integer a , Integer b) {
	    	this.totalPercentage = (float) ((a*100)/b);
	    }
	    public Float getTotalPercentage() {
	    	return this.totalPercentage;
	    }
	    
	    public void setSubtopicLevelwiseCalculations( Map<String, Map<String, Integer>> calculated ,Map<String, Map<String, Integer>> questionPaper) {
	    	HashMap<String, HashMap<String, HashMap<String, Float>>> calculations = new HashMap();
	    	for (String subTopic : calculated.keySet()) {
	    		// submisiion data
	    		Map<String, Integer> calculatedLevels =calculated.get(subTopic);
//	    		System.out.println(calculatedLevels);
	    		
	    		//questionpaper data
	    		Map<String, Integer> qpLevels = questionPaper.get(subTopic);
//	    		System.out.println(qpLevels);
	    		//subtopic level
	    		Map<String , Map<String , Map<String , Float>>> subTopicData = new HashMap<>();
	    		//level data
	    		HashMap<String , HashMap<String , Float>> Leveldata = new HashMap<>();
	    		
	    		
	    		for (String level : calculatedLevels.keySet() ) {
	        		//detailed level data
	 
	        		HashMap<String , Float> data = new HashMap<>();
	 
	    			data.put("subtopicLevelwiseQuestions", (float) qpLevels.get(level));
	    			data.put("subtopicLevelwiseMarksObtained", (float) calculatedLevels.get(level));
	    			data.put("subtopicLevelwisePercentage", (float) ((calculatedLevels.get(level)*100)/qpLevels.get(level)));
	    			Leveldata.put(level, data);
	        		calculations.put(subTopic, Leveldata);
	 
	    		}
	    		
	    	}
	    	this.setSubtopicLevelwiseCalculations(calculations);
	    }
	    
	    public void setCalculationsOfSubtopics(Map<String, Map<String, Integer>> calculated, Map<String, Map<String, Integer>> questions) {
	    	HashMap<String, HashMap<String, Float>> subtopicCalculations = new HashMap();
	    	for(String subTopic : calculated.keySet()) {
	    		Map<String, Integer> calculatedCalculations = calculated.get(subTopic);
	    		Map<String, Integer> qpCalculations = questions.get(subTopic);
	    		HashMap<String, Float> subtopicData = new HashMap();
	    		subtopicData.put("subtopicTotalQuestions", (float) (qpCalculations.getOrDefault("Easy",0)+qpCalculations.getOrDefault("Medium",0)+qpCalculations.getOrDefault("Hard",0)));
	    		subtopicData.put("subtopicMarksObtained", (float) (calculatedCalculations.getOrDefault("Easy",0)+calculatedCalculations.getOrDefault("Medium",0)+calculatedCalculations.getOrDefault("Hard",0)));
	    		subtopicData.put("subtopicPercentage", (float) (calculatedCalculations.getOrDefault("Easy",0)+calculatedCalculations.getOrDefault("Medium",0)+calculatedCalculations.getOrDefault("Hard",0))*100 /(qpCalculations.getOrDefault("Easy",0)+qpCalculations.getOrDefault("Medium",0)+qpCalculations.getOrDefault("Hard",0)) );
	    		subtopicCalculations.put(subTopic, subtopicData);
	    	}
	    	this.setCalculationsOfSubtopics(subtopicCalculations);
	    }
	    
	    public void setTotalAttempted() {
	    	this.totalAttempted = this.totalQuestions-this.questionNotAttempted;
	    }
	    
	    public void setMarksObtained(Map<String, Integer> questionAttempted ) {
	    	this.marksObtained = questionAttempted.get("correct");
	    }
	 
	}