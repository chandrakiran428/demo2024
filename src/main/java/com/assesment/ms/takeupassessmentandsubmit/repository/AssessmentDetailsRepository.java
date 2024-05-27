package com.assesment.ms.takeupassessmentandsubmit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Repository;
//import com.assesment.ms.takeupassessmentandsubmit.DTO.AssessmentDetailsInputDTO;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentDetails;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentKey;

public interface AssessmentDetailsRepository extends MongoRepository<AssessmentDetails, AssessmentKey> {
    AssessmentDetails findByQuestionPaperId(int questionPaperId);

	
}