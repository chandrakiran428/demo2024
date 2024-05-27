package com.assesment.ms.takeupassessmentandsubmit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.assesment.ms.takeupassessmentandsubmit.exceptions.QuestionPaperNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.service.TakeUpAssessmentsMsService;

class TakeUpAssessmentsMSControllerTest {

    private TakeUpAssessmentsMsService service;
    private TakeUpAssessmentsMSController controller;

    @BeforeEach
    void setUp() {
        service = mock(TakeUpAssessmentsMsService.class);
        controller = new TakeUpAssessmentsMSController(service);
    }

    @Test
    void testGetQuestionsForPaper_Success() {
        int paperId = 1;
        ResponseEntity<?> expectedResponse = ResponseEntity.ok().body("Mocked response");

        when(service.getQuestionsForPaper(anyInt())).thenAnswer(invocation -> {
            return expectedResponse;
        });

        ResponseEntity<?> responseEntity = controller.getQuestionsForPaper(paperId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    void testGetQuestionsForPaper_EmptyQuestionNumbers() {
        int paperId = 1;
        ResponseEntity<?> expectedResponse = ResponseEntity.notFound().build();

        when(service.getQuestionsForPaper(anyInt())).thenAnswer(invocation -> {
            return expectedResponse;
        });

        ResponseEntity<?> responseEntity = controller.getQuestionsForPaper(paperId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testGetQuestionsForPaper_QuestionPaperNotFound() {
        int paperId = 1;
        when(service.getQuestionsForPaper(anyInt())).thenThrow(QuestionPaperNotFoundException.class);

        assertThrows(QuestionPaperNotFoundException.class, () -> controller.getQuestionsForPaper(paperId));
    }

    @Test
    void testGetQuestionsForPaper_ErrorFetchingQuestionNumbers() {
        int paperId = 1;
        ResponseEntity<?> expectedResponse = ResponseEntity.badRequest().body("Error fetching question numbers");

        when(service.getQuestionsForPaper(anyInt())).thenAnswer(invocation -> {
            return expectedResponse;
        });


        ResponseEntity<?> responseEntity = controller.getQuestionsForPaper(paperId);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
}
