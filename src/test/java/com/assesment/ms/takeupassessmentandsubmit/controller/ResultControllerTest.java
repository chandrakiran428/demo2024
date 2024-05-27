package com.assesment.ms.takeupassessmentandsubmit.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.assesment.ms.takeupassessmentandsubmit.DTO.CompletedAssessments;
import com.assesment.ms.takeupassessmentandsubmit.DTO.TraineeResultDto;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.AssessmentNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.SubjectNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.UserNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.service.ResultService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultControllerTest {

    @Mock
    private ResultService resultService;

    @InjectMocks
    private ResultController resultController;

   

    @Test
    void testGetAssessmentsWithoutSubject() {
        // Arrange
        Long userId = 1L;
        List<String> distinctSubjects = Arrays.asList("Math", "Science");

        when(resultService.findDistinctSubjectByUserId(userId)).thenReturn(distinctSubjects);

        // Act
        ResponseEntity<?> response = resultController.getAssessments(userId, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(distinctSubjects, response.getBody());

        // Verify
        verify(resultService, times(1)).findDistinctSubjectByUserId(userId);
    }
    
    @Test
    void testGetAssessments() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";
        List<String> distinctSubjects = Arrays.asList("Math", "Science");

        when(resultService.findDistinctSubjectByUserId(userId)).thenReturn(distinctSubjects);

        // Act
        ResponseEntity<?> response = resultController.getAssessments(userId, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(distinctSubjects, response.getBody());
    }

  

    @Test
    void testGetResultById() {
        // Arrange
        Long userId = 1L;
        Long assessmentId = 1L;
        TraineeResultDto traineeResultDto = new TraineeResultDto();

        when(resultService.getResultById(userId, assessmentId)).thenReturn(traineeResultDto);

        // Act
        ResponseEntity<?> response = resultController.getResultById(userId, assessmentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(traineeResultDto, response.getBody());
    }

    @Test
    void testGetCompletedAssessments() {
        // Arrange
        String subject = "Math";
        List<CompletedAssessments> assessments = Arrays.asList(new CompletedAssessments(), new CompletedAssessments());

        when(resultService.getCompletedAssessmentsBySubject(subject)).thenReturn(assessments);

        // Act
        ResponseEntity<?> response = resultController.getCompletedAssessments(subject);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assessments, response.getBody());
    }

    @Test
    void testGetAssessmentsByUserId() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";
        List<CompletedAssessments> assessments = Arrays.asList(new CompletedAssessments(), new CompletedAssessments());

        when(resultService.getAssessmentsByUserIdAndSubject(userId, subject)).thenReturn(assessments);

        // Act
        ResponseEntity<?> response = resultController.getAssessmentsByUserId(userId, subject);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assessments, response.getBody());
    }
    @Test
    void testGetAssessments_NoSubject() {
        // Arrange
        Long userId = 1L;
        List<String> distinctSubjects = Arrays.asList("Math", "Science");

        when(resultService.findDistinctSubjectByUserId(userId)).thenReturn(distinctSubjects);

        // Act
        ResponseEntity<?> response = resultController.getAssessments(userId, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(distinctSubjects, response.getBody());
    }

   

    

    @Test
    void testGetAssessments_UserNotFoundException() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";

        when(resultService.getAssessmentsByUserIdAndSubject(userId, subject)).thenThrow(UserNotFoundException.class);

        // Act
        ResponseEntity<?> response = resultController.getAssessmentsByUserId(userId, subject);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAssessments_SubjectNotFoundException() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";

        when(resultService.getAssessmentsByUserIdAndSubject(userId, subject)).thenThrow(SubjectNotFoundException.class);

        // Act
        ResponseEntity<?> response = resultController.getAssessmentsByUserId(userId, subject);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAssessments_Exception() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";

        when(resultService.getAssessmentsByUserIdAndSubject(userId, subject)).thenThrow(RuntimeException.class);

        // Act
        ResponseEntity<?> response = resultController.getAssessmentsByUserId(userId, subject);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
    @Test
    void testGetResultById_UserNotFound() {
        // Arrange
        Long userId = 1L;
        Long assessmentId = 1L;

        when(resultService.getResultById(userId, assessmentId)).thenThrow(UserNotFoundException.class);

        // Act
        ResponseEntity<?> response = resultController.getResultById(userId, assessmentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetResultById_AssessmentNotFound() {
        // Arrange
        Long userId = 1L;
        Long assessmentId = 1L;

        when(resultService.getResultById(userId, assessmentId)).thenThrow(AssessmentNotFoundException.class);

        // Act
        ResponseEntity<?> response = resultController.getResultById(userId, assessmentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetResultById_IllegalArgumentException() {
        // Arrange
        Long userId = 1L;
        Long assessmentId = 1L;

        when(resultService.getResultById(userId, assessmentId)).thenThrow(IllegalArgumentException.class);

        // Act
        ResponseEntity<?> response = resultController.getResultById(userId, assessmentId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetResultById_InternalServerError() {
        // Arrange
        Long userId = 1L;
        Long assessmentId = 1L;

        when(resultService.getResultById(userId, assessmentId)).thenThrow(RuntimeException.class);

        // Act
        ResponseEntity<?> response = resultController.getResultById(userId, assessmentId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    void testGetAssessments_UserNotFound() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";

        when(resultService.getAssessmentsByIdSubject(userId, subject)).thenThrow(UserNotFoundException.class);

        // Act
        ResponseEntity<?> response = resultController.getAssessments(userId, subject);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAssessments_InternalServerError() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";

        when(resultService.getAssessmentsByIdSubject(userId, subject)).thenThrow(RuntimeException.class);

        // Act
        ResponseEntity<?> response = resultController.getAssessments(userId, subject);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetCompletedAssessments_SubjectNotFound() {
        // Arrange
        String subject = "Math";

        when(resultService.getCompletedAssessmentsBySubject(subject)).thenThrow(SubjectNotFoundException.class);

        // Act
        ResponseEntity<?> response = resultController.getCompletedAssessments(subject);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetCompletedAssessments_InternalServerError() {
        // Arrange
        String subject = "Math";

        when(resultService.getCompletedAssessmentsBySubject(subject)).thenThrow(RuntimeException.class);

        // Act
        ResponseEntity<?> response = resultController.getCompletedAssessments(subject);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetCompletedAssessments_Success() {
        // Arrange
        String subject = "Math";
        List<CompletedAssessments> assessments = new ArrayList<>(); // Mocked assessments

        when(resultService.getCompletedAssessmentsBySubject(subject)).thenReturn(assessments);

        // Act
        ResponseEntity<?> response = resultController.getCompletedAssessments(subject);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assessments, response.getBody());
    }
    @Test
    void testGetAssessmentsByUserId_AssessmentNotFound() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";

        when(resultService.getAssessmentsByUserIdAndSubject(userId, subject)).thenThrow(AssessmentNotFoundException.class);

        // Act
        ResponseEntity<?> response = resultController.getAssessmentsByUserId(userId, subject);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAssessmentsByUserId_InternalServerError() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";

        when(resultService.getAssessmentsByUserIdAndSubject(userId, subject)).thenThrow(RuntimeException.class);

        // Act
        ResponseEntity<?> response = resultController.getAssessmentsByUserId(userId, subject);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAssessmentsByUserId_Success() {
        // Arrange
        Long userId = 1L;
        String subject = "Math";
        List<CompletedAssessments> assessments = new ArrayList<>(); // Mocked assessments

        when(resultService.getAssessmentsByUserIdAndSubject(userId, subject)).thenReturn(assessments);

        // Act
        ResponseEntity<?> response = resultController.getAssessmentsByUserId(userId, subject);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assessments, response.getBody());
    }
    
  
    
}

