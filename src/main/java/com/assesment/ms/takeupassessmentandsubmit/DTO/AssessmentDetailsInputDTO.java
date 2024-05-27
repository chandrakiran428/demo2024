package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class AssessmentDetailsInputDTO {

	@NotNull(message = "Assessment ID cannot be null")
	private Long assessmentId;

	@NotNull(message = "Employee ID cannot be null")
	private Long empId;

	@NotBlank(message = "Assessment name cannot be blank")
	private String assessmentName;
	
	@NotNull(message = "Date cannot be null")
	private LocalDateTime assessmentDate;


	@NotBlank(message = "Course Name cannot be blank")
	private String courseName;

	@NotBlank(message = "Assessment Type cannot be blank")
	private String assessmentType;

	@NotBlank(message = "Target Batch cannot be blank")
	private String targetBatch;

	@NotNull(message = "Number Of Questions cannot be null")
	@PositiveOrZero(message = "Number Of Questions must be a non-negative integer")
	private Integer numberOfQuestions;

	@NotNull(message = "Passing Percentage cannot be null")
	@PositiveOrZero(message = "Passing Percentage must be a non-negative integer")
	private Integer passingPercentage;

	@NotBlank(message = "Submission Type cannot be blank")
	private String submissionType;

	@NotNull(message = "Number Of Violations cannot be null")
	@PositiveOrZero(message = "Number Of Violations must be a non-negative integer")
	private Integer numberOfViolations;

	@NotNull(message = "Question Paper Id cannot be null")
	private Integer questionPaperId;

	@NotNull(message = "Employee responses cannot be null")
	@Valid
	private Map<@NotNull Integer, List<String>> employeeResponses;

	public AssessmentDetailsInputDTO(@NotNull(message = "Assessment ID cannot be null") Long assessmentId,
			@NotNull(message = "Employee ID cannot be null") Long empId,
			@NotBlank(message = "Assessment name cannot be blank") String assessmentName,
			@NotNull(message = "Date cannot be null") LocalDateTime assessmentDate,
			@NotBlank(message = "Course Name cannot be blank") String courseName,
			@NotBlank(message = "Assessment Type cannot be blank") String assessmentType,
			@NotBlank(message = "Target Batch cannot be blank") String targetBatch,
			@NotNull(message = "Number Of Questions cannot be null") @PositiveOrZero(message = "Number Of Questions must be a non-negative integer") Integer numberOfQuestions,
			@NotNull(message = "Passing Percentage cannot be null") @PositiveOrZero(message = "Passing Percentage must be a non-negative integer") Integer passingPercentage,
			@NotBlank(message = "Submission Type cannot be blank") String submissionType,
			@NotNull(message = "Number Of Violations cannot be null") @PositiveOrZero(message = "Number Of Violations must be a non-negative integer") Integer numberOfViolations,
			@NotNull(message = "Question Paper Id cannot be null") Integer questionPaperId,
			@NotNull(message = "Employee responses cannot be null") @Valid Map<@NotNull Integer, List<String>> employeeResponses) {
		super();
		this.assessmentId = assessmentId;
		this.empId = empId;
		this.assessmentName = assessmentName;
		this.assessmentDate = assessmentDate;
		this.courseName = courseName;
		this.assessmentType = assessmentType;
		this.targetBatch = targetBatch;
		this.numberOfQuestions = numberOfQuestions;
		this.passingPercentage = passingPercentage;
		this.submissionType = submissionType;
		this.numberOfViolations = numberOfViolations;
		this.questionPaperId = questionPaperId;
		this.employeeResponses = employeeResponses;
	}

	public AssessmentDetailsInputDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(Long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getAssessmentName() {
		return assessmentName;
	}

	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}

	public LocalDateTime getAssessmentDate() {
		return assessmentDate;
	}

	public void setAssessmentDate(LocalDateTime assessmentDate) {
		this.assessmentDate = assessmentDate;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(String assessmentType) {
		this.assessmentType = assessmentType;
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

	public String getSubmissionType() {
		return submissionType;
	}

	public void setSubmissionType(String submissionType) {
		this.submissionType = submissionType;
	}

	public Integer getNumberOfViolations() {
		return numberOfViolations;
	}

	public void setNumberOfViolations(Integer numberOfViolations) {
		this.numberOfViolations = numberOfViolations;
	}

	public Integer getQuestionPaperId() {
		return questionPaperId;
	}

	public void setQuestionPaperId(Integer questionPaperId) {
		this.questionPaperId = questionPaperId;
	}

	public Map<Integer, List<String>> getEmployeeResponses() {
		return employeeResponses;
	}

	public void setEmployeeResponses(Map<Integer, List<String>> employeeResponses) {
		this.employeeResponses = employeeResponses;
	}

	

	

}
