package com.assesment.ms.takeupassessmentandsubmit.DTO;

import java.util.Objects;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedAssessments {

	
	private Long assessmentId;
    private String assessmentName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedAssessments that = (CompletedAssessments) o;
        return Objects.equals(assessmentId, that.assessmentId) && Objects.equals(assessmentName, that.assessmentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentId, assessmentName);
    }  
}
