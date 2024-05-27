package com.assesment.ms.takeupassessmentandsubmit.exceptions;

public class RecordNotFoundException extends Throwable{
	public RecordNotFoundException(){
        super();
    }

    public  RecordNotFoundException(String message){
        super(message);
    }
}