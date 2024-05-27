package com.assesment.ms.takeupassessmentandsubmit.exceptions;

public class ResultServiceException extends RuntimeException{
    public ResultServiceException(String message){
        super(message);
    }

    public ResultServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
