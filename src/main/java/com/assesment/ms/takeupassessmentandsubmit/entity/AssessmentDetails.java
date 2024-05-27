package com.assesment.ms.takeupassessmentandsubmit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;




@Document(collection = "AssessmentDetails")
public class AssessmentDetails {

	@Id
	private AssessmentKey id;
	private long questionPaperId;
	private String assessmentName;
	private AssessmentType assessmentType;
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate assessmentDate;
	private String courseName;
	private String targetBatch;
	private Integer numberOfQuestions;
	private Integer passingPercentage; //If conversion Problem Need to change
	private SubmissionType submissionType;
	private Integer numberOfViolations;
	private Map<Integer, List<String>> employeeResponses;
	private Integer score;     //If conversion Problem Need to change
	private Result result;

	public AssessmentDetails(AssessmentKey id, long questionPaperId, String assessmentName,
			AssessmentType assessmentType, LocalDate assessmentDate, String courseName, String targetBatch,
			Integer numberOfQuestions, Integer passingPercentage, SubmissionType submissionType,
			Integer numberOfViolations, Map<Integer, List<String>> employeeResponses, Integer score, Result result) {
		super();
		this.id = id;
		this.questionPaperId = questionPaperId;
		this.assessmentName = assessmentName;
		this.assessmentType = assessmentType;
		this.assessmentDate = assessmentDate;
		this.courseName = courseName;
		this.targetBatch = targetBatch;
		this.numberOfQuestions = numberOfQuestions;
		this.passingPercentage = passingPercentage;
		this.submissionType = submissionType;
		this.numberOfViolations = numberOfViolations;
		this.employeeResponses = employeeResponses;
		this.score = score;
		this.result = result;
	}

	public LocalDate getAssessmentDate() {
		return assessmentDate;
	}

	public void setAssessmentDate(LocalDate assessmentDate) {
		this.assessmentDate = assessmentDate;
	}

	public AssessmentDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AssessmentKey getId() {
		return id;
	}

	public void setId(AssessmentKey id) {
		this.id = id;
	}

	public long getQuestionPaperId() {
		return questionPaperId;
	}

	public void setQuestionPaperId(long questionPaperId) {
		this.questionPaperId = questionPaperId;
	}

	public String getAssessmentName() {
		return assessmentName;
	}

	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}

	public AssessmentType getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(AssessmentType assessmentType) {
		this.assessmentType = assessmentType;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTargetBatch() {
		return targetBatch;
	}

	public void setTargetBatch(String targetBatch) {
		this.targetBatch = targetBatch;
	}

	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public void setNumberOfQuestions(Integer numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}

	public Integer getPassingPercentage() {
		return passingPercentage;
	}

	public void setPassingPercentage(Integer passingPercentage) {
		this.passingPercentage = passingPercentage;
	}

	public SubmissionType getSubmissionType() {
		return submissionType;
	}

	public void setSubmissionType(SubmissionType submissionType) {
		this.submissionType = submissionType;
	}

	public Integer getNumberOfViolations() {
		return numberOfViolations;
	}

	public void setNumberOfViolations(Integer numberOfViolations) {
		this.numberOfViolations = numberOfViolations;
	}

	public Map<Integer, List<String>> getEmployeeResponses() {
		return employeeResponses;
	}

	public void setEmployeeResponses(Map<Integer, List<String>> employeeResponses) {
		this.employeeResponses = employeeResponses;
	}

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

}
