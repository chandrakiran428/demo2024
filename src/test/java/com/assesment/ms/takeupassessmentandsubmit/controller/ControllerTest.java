package com.assesment.ms.takeupassessmentandsubmit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.assesment.ms.takeupassessmentandsubmit.DTO.AssessmentDetailsInputDTO;
import com.assesment.ms.takeupassessmentandsubmit.DTO.ResultDTO;
import com.assesment.ms.takeupassessmentandsubmit.controller.Controller;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentDetails;
import com.assesment.ms.takeupassessmentandsubmit.entity.Result;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.InvalidDataException;
import com.assesment.ms.takeupassessmentandsubmit.service.AssessmentDetailsService;

public class ControllerTest {

	@InjectMocks
	private Controller controller;

	@Mock
	private AssessmentDetailsService assessmentDetailsService;

	private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }


	@Test
	public void testCreateAssessment_Success() {
		AssessmentDetails assessment = new AssessmentDetails();
		when(assessmentDetailsService.saveAssessment(any())).thenReturn(assessment);
		ResponseEntity<AssessmentDetails> response = controller.createAssessment(new AssessmentDetails());
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	public void testGetAllAssessments_Success() {
		List<AssessmentDetails> assessments = new ArrayList<>();
		when(assessmentDetailsService.getAllAssessments()).thenReturn(assessments);
		ResponseEntity<List<AssessmentDetails>> response = controller.getAllAssessments();
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	public void testSubmit_ValidInputDTO() throws InvalidDataException {
		AssessmentDetailsInputDTO inputDTO = new AssessmentDetailsInputDTO();
		AssessmentDetails assessment = new AssessmentDetails();
		when(assessmentDetailsService.processAssessment(inputDTO)).thenReturn(assessment);
		ResponseEntity<?> responseEntity = controller.submit(inputDTO);
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(assessment, responseEntity.getBody());
	}

	@Test
	public void testSubmit_InvalidInputDTO() throws InvalidDataException {
		AssessmentDetailsInputDTO inputDTO = new AssessmentDetailsInputDTO();
		when(assessmentDetailsService.processAssessment(inputDTO)).thenThrow(new InvalidDataException("Invalid data"));
		ResponseEntity<?> responseEntity = controller.submit(inputDTO);
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals("Invalid data", responseEntity.getBody());
	}

	@Test
	public void testGetAssessment_Success() {
		AssessmentDetails assessment = new AssessmentDetails();
		when(assessmentDetailsService.getAssessmentById(any())).thenReturn(assessment);
		AssessmentDetails result = controller.getAssessment(1L, 2L);
		assertNotNull(result);
	}

	@Test
	public void testGetScoreAndResult_AssessmentFound() {
		Long assessmentId = 1L;
		Long empId = 100L;
		AssessmentDetails assessment = new AssessmentDetails();
		assessment.setScore(80);
		assessment.setResult(Result.PASS);
		when(assessmentDetailsService.getAssessmentById(any())).thenReturn(assessment);
		ResponseEntity<ResultDTO> responseEntity = controller.getScoreAndResult(assessmentId, empId);
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		ResultDTO responseBody = responseEntity.getBody();
		assertNotNull(responseBody);
		assertEquals(80, responseBody.getScore());
		assertEquals(Result.PASS, responseBody.getResult());
	}

	@Test
	public void testGetScoreAndResult_AssessmentNotFound() {
		Long assessmentId = 1L;
		Long empId = 100L;
		when(assessmentDetailsService.getAssessmentById(any())).thenReturn(null);
		ResponseEntity<ResultDTO> responseEntity = controller.getScoreAndResult(assessmentId, empId);
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void testDeleteAllAssessments_Success() {
		controller.deleteAllAssessments();
		verify(assessmentDetailsService, times(1)).deleteAll();
	}
}
