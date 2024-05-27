package com.assesment.ms.takeupassessmentandsubmit.exceptions;
import java.util.ArrayList;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;




 
@ControllerAdvice
public class GlobalExceptionHandler {
 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        String errorMessage = "Validation failed: \n" + String.join(",\n ", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
 // -----------------------result and analysis ------------------------
    

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<String> handleHttpStatusCodeException(HttpStatusCodeException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }



    @ExceptionHandler(SubtopicsAndDifficultyLevelCountNotFoundException.class)
    public ResponseEntity<String> handleSubtopicsAndDifficultyLevelCountNotFoundException(SubtopicsAndDifficultyLevelCountNotFoundException ex) {
        // Log the exception message to the console
        System.err.println(ex.getMessage());
        // Return the exception message with status code 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
    @ExceptionHandler(QuestionsFetchFailedException.class)
    public ResponseEntity<String> handleQuestionsFetchFailedException(QuestionsFetchFailedException ex) {
        // Log the exception message to the console
        System.err.println(ex.getMessage());
        // Return the exception message with status code 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<String> handleResourceAccessException(ResourceAccessException ex) {
        // Check if the exception message contains "Connection timed out"
        if (ex.getMessage() != null && ex.getMessage().contains("Connection timed out")) {
            // Log the exception message to the console
            System.err.println("Unable to connect to the external service: " + ex.getMessage());
            // Return a custom message indicating the connection timed out
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to connect to the external service: " + ex.getMessage());
        } else {
            // Log the exception message to the console
            System.err.println("Internal Server Error: " + ex.getMessage());
            // Return a generic internal server error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        // Log the exception message to the console
        System.err.println("Internal Server Error: " + ex.getMessage());
        // Return a generic internal server error message
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
 // pk

    @ExceptionHandler(QuestionPaperNotFoundException.class)
    public ResponseEntity<?> handleQuestionPaperNotFoundException(QuestionPaperNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
    // ****************** admin code ****************************************************************
    
    
    @ExceptionHandler(BatchNoNotFoundException.class)
    public ResponseEntity<String> batchNoNotFoundException(BatchNoNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<String> recordNotFoundException(RecordNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    
}