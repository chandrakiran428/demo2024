package com.assesment.ms.takeupassessmentandsubmit.exceptions;

public class QuestionsFetchFailedException extends RuntimeException {

    public QuestionsFetchFailedException(String message) {
        super(message);
    }

    public QuestionsFetchFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
