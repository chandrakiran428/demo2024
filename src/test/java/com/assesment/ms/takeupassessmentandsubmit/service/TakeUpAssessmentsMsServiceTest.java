package com.assesment.ms.takeupassessmentandsubmit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.assesment.ms.takeupassessmentandsubmit.exceptions.QuestionPaperNotFoundException;

class TakeUpAssessmentsMsServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    TakeUpAssessmentsMsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetQuestionsForPaper_Success() {
        // Mocked response from the question paper URL
        Map<String, Object> questionPaperResponse = new HashMap<>();
        questionPaperResponse.put("questionNumbers", Arrays.asList(1, 2, 3));
        questionPaperResponse.put("questionPaperId", 123);

        // Mocked response from the question fetch URL
        List<Map<String, Object>> questions = Arrays.asList(
                createQuestion("Question 1", Arrays.asList("Option 1", "Option 2"), 1, Arrays.asList("Answer 1")),
                createQuestion("Question 2", Arrays.asList("Option 1", "Option 2"), 2, Arrays.asList("Answer 2"))
        );

        // Mocking restTemplate behavior
        when(restTemplate.getForObject(any(String.class), eq(Map.class))).thenReturn(questionPaperResponse);
        when(restTemplate.getForObject(any(String.class), eq(List.class))).thenReturn(questions);

        // Call the service method
        ResponseEntity<?> responseEntity = service.getQuestionsForPaper(123);

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody.get("questionPaperId"));
        assertEquals(123, responseBody.get("questionPaperId"));

        List<Map<String, Object>> extractedQuestions = (List<Map<String, Object>>) responseBody.get("questions");
        assertNotNull(extractedQuestions);
        assertEquals(2, extractedQuestions.size());

        // Assert on each question
        Map<String, Object> question1 = extractedQuestions.get(0);
        assertEquals("Question 1", question1.get("title"));
        assertEquals(Arrays.asList("Option 1", "Option 2"), question1.get("options"));
        assertEquals(1, question1.get("questionId"));
        assertEquals(1, question1.get("answersLength"));

        Map<String, Object> question2 = extractedQuestions.get(1);
        assertEquals("Question 2", question2.get("title"));
        assertEquals(Arrays.asList("Option 1", "Option 2"), question2.get("options"));
        assertEquals(2, question2.get("questionId"));
        assertEquals(1, question2.get("answersLength"));

        // Verify restTemplate invocations
        verify(restTemplate, times(1)).getForObject(any(String.class), eq(Map.class));
        verify(restTemplate, times(1)).getForObject(any(String.class), eq(List.class));
    }



    @Test
    void testGetQuestionsForPaper_QuestionPaperNotFound_HTTPException() {
        int questionPaperId = 123;
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenThrow(HttpClientErrorException.NotFound.class);
        assertThrows(QuestionPaperNotFoundException.class, () -> service.getQuestionsForPaper(questionPaperId));
    }

    @Test
    void testGetQuestionsForPaper_EmptyQuestionNumbers() {
        int questionPaperId = 123;
        Map<String, Object> questionPaperResponse = new HashMap<>();
        questionPaperResponse.put("questionNumbers", Arrays.asList());
        questionPaperResponse.put("questionPaperId", 123);
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(questionPaperResponse);
        ResponseEntity<?> responseEntity = service.getQuestionsForPaper(questionPaperId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testGetQuestionsForPaper_ErrorFetchingQuestionNumbers() {
        int questionPaperId = 123;

        // Mocking the behavior of restTemplate.getForObject to return null
        when(restTemplate.getForObject(anyString(), any())).thenReturn(null);

        // Call the service method
        ResponseEntity<?> responseEntity = service.getQuestionsForPaper(questionPaperId);

        // Asserting that the response entity is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error fetching question numbers or question paper ID for the given paper ID.",
                     responseEntity.getBody());
    }

    private Map<String, Object> createQuestion(String title, List<String> options, int questionId, List<String> answers) {
        Map<String, Object> question = new HashMap<>();
        question.put("title", title);
        question.put("options", options);
        question.put("questionId", questionId);
        question.put("answers", answers);
        return question;
    }
}

