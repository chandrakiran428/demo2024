package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.util.Map;

public class UserIdandSubjectDTOresult
{
    private String batchNo;
    private Map<String, Map<String, Integer>> calculatedMarksOfSubtopics;
    private Map<String, Integer> difficultyLevelCorrectlyAnswered;
    private Integer totalMarks;
    private double percentageScored;

    public UserIdandSubjectDTOresult(String batchNo, Map<String, Map<String, Integer>> calculatedMarksOfSubtopics, Map<String, Integer> difficultyLevelCorrectlyAnswered, Integer totalMarks, double percentageScored) {
        this.batchNo = batchNo;
        this.calculatedMarksOfSubtopics = calculatedMarksOfSubtopics;
        this.difficultyLevelCorrectlyAnswered = difficultyLevelCorrectlyAnswered;
        this.totalMarks = totalMarks;
        this.percentageScored = percentageScored;
    }

    // Getters and setters

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Map<String, Map<String, Integer>> getCalculatedMarksOfSubtopics() {
        return calculatedMarksOfSubtopics;
    }

    public void setCalculatedMarksOfSubtopics(Map<String, Map<String, Integer>> calculatedMarksOfSubtopics) {
        this.calculatedMarksOfSubtopics = calculatedMarksOfSubtopics;
    }

    public Map<String, Integer> getDifficultyLevelCorrectlyAnswered() {
        return difficultyLevelCorrectlyAnswered;
    }

    public void setDifficultyLevelCorrectlyAnswered(Map<String, Integer> difficultyLevelCorrectlyAnswered) {
        this.difficultyLevelCorrectlyAnswered = difficultyLevelCorrectlyAnswered;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public double getPercentageScored() {
        return percentageScored;
    }

    public void setPercentageScored(double percentageScored) {
        this.percentageScored = percentageScored;
    }
}

