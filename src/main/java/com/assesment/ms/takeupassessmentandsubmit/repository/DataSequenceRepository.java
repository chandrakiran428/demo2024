package com.assesment.ms.takeupassessmentandsubmit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.assesment.ms.takeupassessmentandsubmit.entity.DataSequences;


public interface DataSequenceRepository extends MongoRepository<DataSequences,String> {
}

