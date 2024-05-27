package com.assesment.ms.takeupassessmentandsubmit.entity;

import java.io.Serializable;
import java.util.Objects;

public class AssessmentKey implements Serializable {

	private Long assessmentId;
    private Long empId;

    public AssessmentKey() {}
    
    

    public AssessmentKey(Long assessmentId, Long empId) {
		super();
		this.assessmentId = assessmentId;
		this.empId = empId;
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



	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssessmentKey)) return false;
        AssessmentKey that = (AssessmentKey) o;
        return empId == that.empId && Objects.equals(assessmentId, that.assessmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentId, empId);
    }
}
