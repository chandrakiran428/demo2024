package com.assesment.ms.takeupassessmentandsubmit.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
public class DataSequences {

    @Id
    private String id;

    private long seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public DataSequences(String id, long seq) {
        this.id = id;
        this.seq = seq;
    }

    public DataSequences() {
    }
}
