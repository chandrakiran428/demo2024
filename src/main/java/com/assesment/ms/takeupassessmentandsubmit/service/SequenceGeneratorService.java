package com.assesment.ms.takeupassessmentandsubmit.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.assesment.ms.takeupassessmentandsubmit.entity.DataSequences;
import com.assesment.ms.takeupassessmentandsubmit.repository.DataSequenceRepository;



@Service
public class SequenceGeneratorService {

    private DataSequenceRepository dataSequenceRepository;


    public SequenceGeneratorService(DataSequenceRepository dataSequenceRepository) {
        super();
        this.dataSequenceRepository = dataSequenceRepository;
    }

    public long generateSequence(String seqName){
        /*
            a function which will generate sequence
            if there is not seq regarding the give sequence name
            it will initiate a new seq by setting it to 1
            after every call of generateSequence method the increase method will get called
         */
        Optional<DataSequences> sequence = dataSequenceRepository.findById(seqName);
        if(sequence.isPresent()){
            increase(seqName);
            return sequence.get().getSeq();
        }
        else{
            DataSequences newSequence = dataSequenceRepository.save(new DataSequences(seqName,1));
            increase(seqName);
            return newSequence.getSeq();
        }
    }

    public boolean increase(String seqName){
        /*
            this method will fetch the data from the Datasequence collection
            and updates by increasing the value of seq stored in the document by 1

        */
        DataSequences dataSequence = dataSequenceRepository.findById(seqName).get();
        dataSequence.setSeq(dataSequence.getSeq()+1);
        dataSequenceRepository.save(dataSequence);
        return true;
    }

}
