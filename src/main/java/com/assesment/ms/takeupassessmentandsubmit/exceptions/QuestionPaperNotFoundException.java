package com.assesment.ms.takeupassessmentandsubmit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuestionPaperNotFoundException extends RuntimeException {
    public QuestionPaperNotFoundException(String message) {
        super(message);
    }
}