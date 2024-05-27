package com.assesment.ms.takeupassessmentandsubmit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
 
import com.assesment.ms.takeupassessmentandsubmit.DTO.AssessmentDetailsInputDTO;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentDetails;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentKey;
import com.assesment.ms.takeupassessmentandsubmit.entity.Result;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.InvalidAssessmentDetailsException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.InvalidDataException;
import com.assesment.ms.takeupassessmentandsubmit.repository.AssessmentDetailsRepository;
import com.assesment.ms.takeupassessmentandsubmit.service.AssessmentDetailsService;
 
public class AssessmentDetailsServiceTest {
 
	@Mock
	private AssessmentDetailsRepository assessmentDetailsRepository;
 
	@Mock
	private RestTemplate restTemplate;
 
	@InjectMocks
	private AssessmentDetailsService assessmentDetailsService;
 
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
 
	private AssessmentDetailsInputDTO validInputDTO;
 
	@Test
	public void testSaveAssessment() {
		// Create a sample AssessmentDetails object
		AssessmentDetails assessment = new AssessmentDetails();
		when(assessmentDetailsRepository.save(any(AssessmentDetails.class))).thenReturn(assessment);
 
		// Call the method under test
		AssessmentDetails savedAssessment = assessmentDetailsService.saveAssessment(assessment);
 
		// Verify that the assessment was saved
		assertNotNull(savedAssessment);
		verify(assessmentDetailsRepository, times(1)).save(any(AssessmentDetails.class));
	}
 
	public void setUpInput() {
 
		// Initialize valid input DTO
		validInputDTO = new AssessmentDetailsInputDTO();
		validInputDTO.setAssessmentId(1L);
		validInputDTO.setEmpId(222L);
		validInputDTO.setAssessmentName("Java assessment 1");
		validInputDTO.setAssessmentDate(LocalDateTime.of(2024, 5, 15, 14, 14));
		validInputDTO.setCourseName("Java");
		validInputDTO.setAssessmentType("NORMAL");
		validInputDTO.setTargetBatch("BC103");
		validInputDTO.setNumberOfQuestions(10);
		validInputDTO.setPassingPercentage(10);
		validInputDTO.setSubmissionType("NORMAL_SUBMISSION");
		validInputDTO.setNumberOfViolations(0);
		validInputDTO.setQuestionPaperId(1);
		Map<Integer, List<String>> employeeResponses = new HashMap<>();
		employeeResponses.put(32, Arrays.asList("A", "C"));
		employeeResponses.put(33, Arrays.asList("B"));
		employeeResponses.put(34, Arrays.asList("C", "D"));
		employeeResponses.put(35, Arrays.asList("B", "C"));
		employeeResponses.put(37, Collections.emptyList());
		employeeResponses.put(40, Arrays.asList("A"));
		employeeResponses.put(41, Arrays.asList("B"));
		employeeResponses.put(42, Arrays.asList("A", "B", "D"));
		employeeResponses.put(43, Arrays.asList("A", "C"));
		employeeResponses.put(44, Arrays.asList("B", "D"));
		validInputDTO.setEmployeeResponses(employeeResponses);
 
		// Mock external service response
		List<Map<String, Object>> mockQuestionDetails = new ArrayList<>();
		// Add mock question details to the list
		// For example:
		// Mock question details for question 1
		Map<String, Object> question1 = new HashMap<>();
		question1.put("questionId", 1);
		question1.put("answers", Arrays.asList("A", "B", "C"));
		mockQuestionDetails.add(question1);
 
		// Mock question details for question 2
		Map<String, Object> question2 = new HashMap<>();
		question2.put("questionId", 2);
		question2.put("answers", Arrays.asList("B", "C", "D"));
		mockQuestionDetails.add(question2);
 
		// Mock question details for question 3
		Map<String, Object> question3 = new HashMap<>();
		question3.put("questionId", 3);
		question3.put("answers", Arrays.asList("A", "C", "D"));
		mockQuestionDetails.add(question3);
 
		// Mock question details for question 4
		Map<String, Object> question4 = new HashMap<>();
		question4.put("questionId", 4);
		question4.put("answers", Arrays.asList("A", "B", "C"));
		mockQuestionDetails.add(question4);
 
		// Mock question details for question 5
		Map<String, Object> question5 = new HashMap<>();
		question5.put("questionId", 5);
		question5.put("answers", Arrays.asList("B", "C", "D"));
		mockQuestionDetails.add(question5);
 
		// Mock question details for question 6
		Map<String, Object> question6 = new HashMap<>();
		question6.put("questionId", 6);
		question6.put("answers", Arrays.asList("A", "C", "D"));
		mockQuestionDetails.add(question6);
 
		// Mock question details for question 7
		Map<String, Object> question7 = new HashMap<>();
		question7.put("questionId", 7);
		question7.put("answers", Arrays.asList("A", "B", "C"));
		mockQuestionDetails.add(question7);
 
		// Mock question details for question 8
		Map<String, Object> question8 = new HashMap<>();
		question8.put("questionId", 8);
		question8.put("answers", Arrays.asList("B", "C", "D"));
		mockQuestionDetails.add(question8);
 
		// Mock question details for question 9
		Map<String, Object> question9 = new HashMap<>();
		question9.put("questionId", 9);
		question9.put("answers", Arrays.asList("A", "C", "D"));
		mockQuestionDetails.add(question9);
 
		// Mock question details for question 10
		Map<String, Object> question10 = new HashMap<>();
		question10.put("questionId", 10);
		question10.put("answers", Arrays.asList("A", "B", "C"));
		mockQuestionDetails.add(question10);
 
		// Add more mock question details as needed
		when(restTemplate.getForObject(anyString(), any())).thenReturn(mockQuestionDetails);
	}
 
	@Test
	public void testProcessAssessment_ValidInput() throws InvalidDataException {
		setUpInput();
		// Mock repository save method
		when(assessmentDetailsRepository.save(any())).thenReturn(new AssessmentDetails());
 
		// Call the method under test
		AssessmentDetails result = assessmentDetailsService.processAssessment(validInputDTO);
 
		// Assert that the assessment was saved
		assertNotNull(result);
	}
 
	@Test
	public void testProcessAssessment_InvalidInput() throws InvalidDataException {
		setUpInput();
 
		// Set up invalid input DTO
		AssessmentDetailsInputDTO invalidInputDTO = new AssessmentDetailsInputDTO();
		invalidInputDTO = new AssessmentDetailsInputDTO();
		invalidInputDTO.setAssessmentId(null);
		invalidInputDTO.setEmpId(null);
		invalidInputDTO.setAssessmentName("Java assessment 1");
		invalidInputDTO.setAssessmentDate(LocalDateTime.of(2024, 5, 15, 14, 14));
		invalidInputDTO.setCourseName("Java");
		invalidInputDTO.setAssessmentType("NORMAL");
		invalidInputDTO.setTargetBatch("BC103");
		invalidInputDTO.setNumberOfQuestions(10);
		invalidInputDTO.setPassingPercentage(10);
		invalidInputDTO.setSubmissionType("NORMAL_SUBMISSION");
		invalidInputDTO.setNumberOfViolations(0);
		invalidInputDTO.setQuestionPaperId(1);
		Map<Integer, List<String>> employeeResponses = new HashMap<>();
		employeeResponses.put(32, Arrays.asList("A", "C"));
		employeeResponses.put(33, Arrays.asList("B"));
		employeeResponses.put(34, Arrays.asList("C", "D"));
		employeeResponses.put(35, Arrays.asList("B", "C"));
		employeeResponses.put(37, Collections.emptyList());
		employeeResponses.put(40, Arrays.asList("A"));
		employeeResponses.put(41, Arrays.asList("B"));
		employeeResponses.put(42, Arrays.asList("A", "B", "D"));
		employeeResponses.put(43, Arrays.asList("A", "C"));
		employeeResponses.put(44, Arrays.asList("B", "D"));
		invalidInputDTO.setEmployeeResponses(employeeResponses);
		// Set properties of invalidInputDTO such that it violates validation rules
 
		// Call the method under test
		assessmentDetailsService.processAssessment(invalidInputDTO);
	}
 
	@Test
	public void testCalculateScore_Success() {
		// Prepare mock data
		Map<Integer, List<String>> employeeResponses = new HashMap<>();
		employeeResponses.put(32, List.of("A", "C"));
		employeeResponses.put(33, List.of("B"));
		employeeResponses.put(34, List.of("C", "D"));
		employeeResponses.put(35, List.of("B", "C"));
		employeeResponses.put(37, List.of());
		employeeResponses.put(40, List.of("A"));
		employeeResponses.put(41, List.of("B"));
		employeeResponses.put(42, List.of("A", "B", "D"));
		employeeResponses.put(43, List.of("A", "C"));
		employeeResponses.put(44, List.of("B", "D"));
 
		// Prepare mock response from external service
		List<Map<String, Object>> mockQuestions = List.of(Map.of("questionId", 32, "answers", List.of("A", "C")),
				Map.of("questionId", 33, "answers", List.of("B")),
				Map.of("questionId", 34, "answers", List.of("C", "D")),
				Map.of("questionId", 35, "answers", List.of("B", "C")), Map.of("questionId", 37, "answers", List.of()),
				Map.of("questionId", 40, "answers", List.of("A")), Map.of("questionId", 41, "answers", List.of("B")),
				Map.of("questionId", 42, "answers", List.of("A", "B", "D")),
				Map.of("questionId", 43, "answers", List.of("A", "C")),
				Map.of("questionId", 44, "answers", List.of("B", "D")));
 
		when(restTemplate.getForObject(anyString(), any())).thenReturn(mockQuestions);
 
		// Call the method under test
		Integer score = assessmentDetailsService.calculateScore(employeeResponses);
 
		// Verify the score calculation
		assertNotNull(score);
		assertEquals(10, score); // Since all employee responses are correct, the score should be 10
	}
 
	@Test
	public void testDetermineResult_Pass() {
		// Call the method under test
		Result result = assessmentDetailsService.determineResult(8, 70, 10);
 
		// Verify the result
		assertNotNull(result);
		assertEquals(Result.PASS, result);
	}
 
	@Test
	public void testDetermineResult_Fail() {
		// Call the method under test
		Result result = assessmentDetailsService.determineResult(5, 70, 10);
 
		// Verify the result
		assertNotNull(result);
		assertEquals(Result.FAIL, result);
	}
	
	    @Test
	    public void testGetAllAssessments_Failure() {
	        // Mock the repository method to throw an exception
	        when(assessmentDetailsRepository.findAll()).thenThrow(new RuntimeException("Failed to retrieve assessments."));
 
	        // Call the method under test and assert that it throws an exception
	        assertThrows(InvalidAssessmentDetailsException.class, () -> {
	            assessmentDetailsService.getAllAssessments();
	        });
	    }
 
	@Test
	public void testGetAssessmentById_Success() {
		// Prepare mock data
		AssessmentKey key = new AssessmentKey(1L, 1L);
		AssessmentDetails mockAssessment = new AssessmentDetails();
		when(assessmentDetailsRepository.findById(key)).thenReturn(Optional.of(mockAssessment));
 
		// Call the method under test
		AssessmentDetails result = assessmentDetailsService.getAssessmentById(key);
 
		// Verify the result
		assertNotNull(result);
		assertEquals(mockAssessment, result);
		verify(assessmentDetailsRepository, times(1)).findById(key);
	}
 
	@Test
	public void testDeleteAll() {
		// Call the method under test
		assessmentDetailsService.deleteAll();
 
		// Verify that deleteAll method of repository is called
		verify(assessmentDetailsRepository, times(1)).deleteAll();
	}
	
	
 
}