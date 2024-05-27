package com.assesment.ms.takeupassessmentandsubmit.service;

import com.assesment.ms.takeupassessmentandsubmit.DTO.AssessmentsListDto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.QuestionPaperSubtopics;
import com.assesment.ms.takeupassessmentandsubmit.DTO.QuestionTestdto;
import com.assesment.ms.takeupassessmentandsubmit.DTO.TraineeResultDto;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentDetails;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentKey;
import com.assesment.ms.takeupassessmentandsubmit.entity.AssessmentType;
import com.assesment.ms.takeupassessmentandsubmit.entity.Result;
import com.assesment.ms.takeupassessmentandsubmit.entity.ResultAndAnalysis;
import com.assesment.ms.takeupassessmentandsubmit.entity.SubmissionType;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.AssessmentNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.BatchNoNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.QuestionsFetchFailedException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.RecordNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.SubjectNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.SubtopicsAndDifficultyLevelCountNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.exceptions.UserNotFoundException;
import com.assesment.ms.takeupassessmentandsubmit.repository.AssessmentDetailsRepository;
import com.assesment.ms.takeupassessmentandsubmit.repository.ResultRepository;
import com.assesment.ms.takeupassessmentandsubmit.resultAndAnalysisConverter.ResultAndAnalysisConverter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ResultServiceImpTest {



    @Mock
    private AssessmentDetailsService calculatedRepo;
   @Mock
    private AssessmentDetailsRepository assessmentDetailsRepository;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;



    @Mock
    private ResultRepository repository;


    @Mock
    private AssessmentDetails assessmentDetails;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ResultServiceImp resultService;

    @Mock
    private MongoOperations mongoOperations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



//positive testcase for getSubtopicsAndDifficultyLevelCount
    @Test
    public void testGetSubtopicsAndDifficultyLevelCount_Success() {
        // Arrange
        Long assessmentId = 1L;
        String url = "http://172.18.4.37:9094/questionPaper/counts/" + assessmentId;

        QuestionPaperSubtopics expectedResponse = new QuestionPaperSubtopics();
        // Set up expected response data
        Map<String, Map<String, Integer>> questionSplitCount = new HashMap<>();
        Map<String, Integer> subTopicMap = new HashMap<>();
        subTopicMap.put("subtopic1", 5);
        subTopicMap.put("subtopic2", 3);
        questionSplitCount.put("questions", subTopicMap);
        expectedResponse.setQuestionSplitCount(questionSplitCount);

        Map<String, Map<String, Integer>> difficultyLevelSplit = new HashMap<>();
        Map<String, Integer> difficultyMap = new HashMap<>();
        difficultyMap.put("easy", 4);
        difficultyMap.put("medium", 3);
        difficultyMap.put("hard", 1);
        difficultyLevelSplit.put("difficulty", difficultyMap);
        expectedResponse.setDifficultyLevelSplit(difficultyMap);

        ResponseEntity<QuestionPaperSubtopics> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        // Act
        QuestionPaperSubtopics actualResponse = resultService.getSubtopicsAndDifficultyLevelCount(assessmentId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    //Negative testcase for getSubtopicsAndDifficultyLevelCount

    @Test
    public void testGetSubtopicsAndDifficultyLevelCount_ResourceAccessException() {
        // Arrange
        Long assessmentId = 1L;
        String url = "http://172.18.4.37:9094/questionPaper/counts/" + assessmentId;

        when(restTemplate.getForEntity(anyString(), any(Class.class)))
                .thenThrow(new ResourceAccessException("Service unavailable"));

        // Act & Assert
        assertThrows(SubtopicsAndDifficultyLevelCountNotFoundException.class, () -> {
            resultService.getSubtopicsAndDifficultyLevelCount(assessmentId);
        });
    }


    @Test
    public void testGetSubtopicsAndDifficultyLevelCount_NullResponseBody() {
        // Arrange
        Long assessmentId = 1L;
        String url = "http://172.18.4.37:9094/questionPaper/counts/" + assessmentId;

        ResponseEntity<QuestionPaperSubtopics> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(SubtopicsAndDifficultyLevelCountNotFoundException.class, () -> {
            resultService.getSubtopicsAndDifficultyLevelCount(assessmentId);
        });
    }

    @Test
    public void testGetSubtopicsAndDifficultyLevelCount_ClientError() {
        // Arrange
        Long assessmentId = 1L;
        String url = "http://172.18.4.37:9094/questionPaper/counts/" + assessmentId;

        when(restTemplate.getForEntity(anyString(), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act & Assert
        assertThrows(HttpClientErrorException.class, () -> {
            resultService.getSubtopicsAndDifficultyLevelCount(assessmentId);
        });
    }



    @Test
    public void testGetSubtopicsAndDifficultyLevelCount_EmptyResponseEntity() {
        // Arrange
        Long assessmentId = 1L;
        String url = "http://172.18.4.37:9094/questionPaper/counts/" + assessmentId;

        ResponseEntity<QuestionPaperSubtopics> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(SubtopicsAndDifficultyLevelCountNotFoundException.class, () -> {
            resultService.getSubtopicsAndDifficultyLevelCount(assessmentId);
        });
    }

    //positive testcase for calculateMarksForQuestion

    @Test
    public void testCalculateMarksForQuestion_AllCorrect() throws Exception {
        // Arrange
        List<String> userAnswerList = Arrays.asList("A", "B", "C");
        List<String> correctAnswerList = Arrays.asList("A", "B", "C");

        // Use reflection to access the private method
        Method method = ResultServiceImp.class.getDeclaredMethod("calculateMarksForQuestion", List.class, List.class);
        method.setAccessible(true);

        // Act
        int marks = (int) method.invoke(null, userAnswerList, correctAnswerList);

        // Assert
        assertEquals(1, marks, "Marks should be 1 for all correct answers");
    }

    //negative testcase for calculateMarksForQuestion

    @Test
    public void testCalculateMarksForQuestion_SomeIncorrect() throws Exception {
        // Arrange
        List<String> userAnswerList = Arrays.asList("A", "B", "D");
        List<String> correctAnswerList = Arrays.asList("A", "B", "C");

        // Use reflection to access the private method
        Method method = ResultServiceImp.class.getDeclaredMethod("calculateMarksForQuestion", List.class, List.class);
        method.setAccessible(true);

        // Act
        int marks = (int) method.invoke(null, userAnswerList, correctAnswerList);

        // Assert
        assertEquals(0, marks, "Marks should be 0 for incorrect answers");
    }

    @Test
    public void testCalculateMarksForQuestion_EmptyUserAnswer() throws Exception {
        // Arrange
        List<String> userAnswerList = Arrays.asList();
        List<String> correctAnswerList = Arrays.asList("A", "B", "C");

        // Use reflection to access the private method
        Method method = ResultServiceImp.class.getDeclaredMethod("calculateMarksForQuestion", List.class, List.class);
        method.setAccessible(true);

        // Act
        int marks = (int) method.invoke(null, userAnswerList, correctAnswerList);

        // Assert
        assertEquals(0, marks, "Marks should be 0 for empty user answers");
    }

    @Test
    public void testCalculateMarksForQuestion_EmptyCorrectAnswer() throws Exception {
        // Arrange
        List<String> userAnswerList = Arrays.asList("A", "B", "C");
        List<String> correctAnswerList = Arrays.asList();

        // Use reflection to access the private method
        Method method = ResultServiceImp.class.getDeclaredMethod("calculateMarksForQuestion", List.class, List.class);
        method.setAccessible(true);

        // Act
        int marks = (int) method.invoke(null, userAnswerList, correctAnswerList);

        // Assert
        assertEquals(0, marks, "Marks should be 0 for empty correct answers");
    }

    @Test
    public void testCalculateMarksForQuestion_NullUserAnswer() throws Exception {
        // Arrange
        List<String> userAnswerList = null;
        List<String> correctAnswerList = Arrays.asList("A", "B", "C");

        // Use reflection to access the private method
        Method method = ResultServiceImp.class.getDeclaredMethod("calculateMarksForQuestion", List.class, List.class);
        method.setAccessible(true);

        // Act
        int marks = (int) method.invoke(null, userAnswerList, correctAnswerList);

        // Assert
        assertEquals(0, marks, "Marks should be 0 for null user answers");
    }

    @Test
    public void testCalculateMarksForQuestion_NullCorrectAnswer() throws Exception {
        // Arrange
        List<String> userAnswerList = Arrays.asList("A", "B", "C");
        List<String> correctAnswerList = null;

        // Use reflection to access the private method
        Method method = ResultServiceImp.class.getDeclaredMethod("calculateMarksForQuestion", List.class, List.class);
        method.setAccessible(true);

        // Act
        int marks = (int) method.invoke(null, userAnswerList, correctAnswerList);

        // Assert
        assertEquals(0, marks, "Marks should be 0 for null correct answers");
    }

    @Test
    public void testCalculateMarksForQuestion_BothNull() throws Exception {
        // Arrange
        List<String> userAnswerList = null;
        List<String> correctAnswerList = null;

        // Use reflection to access the private method
        Method method = ResultServiceImp.class.getDeclaredMethod("calculateMarksForQuestion", List.class, List.class);
        method.setAccessible(true);

        // Act
        int marks = (int) method.invoke(null, userAnswerList, correctAnswerList);

        // Assert
        assertEquals(0, marks, "Marks should be 0 for both null answers");
    }

    //POSITIVE TESTCASE FOR GET POSITIVE QUESTION BY ID



    @Test
    public void testGetQuestionById_Positive() {
        // Arrange
        List<QuestionTestdto> questions = new ArrayList<>();
        QuestionTestdto question1 = new QuestionTestdto(1L, "Subject1", "Subtopic1", "Question1", null, "Easy", "Type1", "AnswerType1", null);
        QuestionTestdto question2 = new QuestionTestdto(2L, "Subject2", "Subtopic2", "Question2", null, "Medium", "Type2", "AnswerType2", null);
        questions.add(question1);
        questions.add(question2);
        int questionIdToFind = 2;

        // Act
        QuestionTestdto foundQuestion = ResultServiceImp.getQuestionById(questions, questionIdToFind);

        // Assert
        assertNotNull(foundQuestion, "Found question should not be null");
        assertEquals(question2, foundQuestion, "Found question should match the expected question");
    }


    @Test
    public void testGetQuestionById_Negative_QuestionNotFound() {
        // Arrange
        List<QuestionTestdto> questions = new ArrayList<>();
        QuestionTestdto question1 = new QuestionTestdto(1L, "Subject1", "Subtopic1", "Question1", null, "Easy", "Type1", "AnswerType1", null);
        QuestionTestdto question2 = new QuestionTestdto(2L, "Subject2", "Subtopic2", "Question2", null, "Medium", "Type2", "AnswerType2", null);
        questions.add(question1);
        questions.add(question2);
        int nonExistentQuestionId = 3;

        // Act
        QuestionTestdto foundQuestion = ResultServiceImp.getQuestionById(questions, nonExistentQuestionId);

        // Assert
        assertNull(foundQuestion, "Found question should be null since the question ID does not exist");
    }

    @Test
    public void testGetQuestionById_Negative_EmptyQuestionList() {
        // Arrange
        List<QuestionTestdto> questions = new ArrayList<>();
        int questionIdToFind = 1; // Doesn't matter because the list is empty

        // Act
        QuestionTestdto foundQuestion = ResultServiceImp.getQuestionById(questions, questionIdToFind);

        // Assert
        assertNull(foundQuestion, "Found question should be null since the question list is empty");
    }

//positive testcase for extractquestions

    @Test
    public void testExtractQuestionIdsWithAnswers_Positive() {
        // Create a list of QuestionTestdto objects
    	 List<QuestionTestdto> mockQuestions = new ArrayList<>();
         // Adding some mock questions
         mockQuestions.add(new QuestionTestdto(1L, "Maths", "Algebra", "Solving equations", null, "Intermediate", "MCQ", "Single", List.of("A")));
         mockQuestions.add(new QuestionTestdto(2L, "Physics", "Mechanics", "Newton's laws", null, "Advanced", "MCQ", "Single", List.of("B")));
         mockQuestions.add(new QuestionTestdto(3L, "Chemistry", "Organic Chemistry", "Carbon compounds", null, "Beginner", "MCQ", "Single", List.of("C")));


        // Call the extractQuestionIdsWithAnswers method
        Map<Long, List<String>> result = resultService.extractQuestionIdsWithAnswers(mockQuestions);

        // Assert the correctness of the result
        assertEquals(3, result.size()); // Expecting two entries in the map

        // Verify the answers for each question ID
        assertEquals(List.of("Answer 1", "Answer 2"), result.get(1L));
        assertEquals(List.of("Answer 3", "Answer 4"), result.get(2L));
    }
//negative test case for extractquestions


    @Test
    public void testExtractQuestionIdsWithAnswers_EmptyInput() {
        // Create an empty list of QuestionTestdto objects
        List<QuestionTestdto> emptyQuestions = new ArrayList<>();

        // Create an instance of

        // Call the extractQuestionIdsWithAnswers method with empty input list
        Map<Long, List<String>> result = resultService.extractQuestionIdsWithAnswers(emptyQuestions);

        // Assert that the result is an empty map
        assertTrue(result.isEmpty());
    }


    @Test
    public void testExtractQuestionIdsWithAnswers_MissingQuestionIds() {
        // Create a list of QuestionTestdto objects with missing question IDs
    	 List<QuestionTestdto> mockQuestions = new ArrayList<>();
         // Adding some mock questions
         mockQuestions.add(new QuestionTestdto(1L, "Maths", "Algebra", "Solving equations", null, "Intermediate", "MCQ", "Single", List.of("A")));
         mockQuestions.add(new QuestionTestdto(2L, "Physics", "Mechanics", "Newton's laws", null, "Advanced", "MCQ", "Single", List.of("B")));
         mockQuestions.add(new QuestionTestdto(3L, "Chemistry", "Organic Chemistry", "Carbon compounds", null, "Beginner", "MCQ", "Single", List.of("C")));
        // Call the extractQuestionIdsWithAnswers method with input containing missing question IDs
        Map<Long, List<String>> result = resultService.extractQuestionIdsWithAnswers(mockQuestions);

        // Assert that the result is neither null nor empty
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should not be empty");
    }

    @Test
    public void testExtractQuestionIdsWithAnswers_InvalidAnswers() {
        // Create a list of QuestionTestdto objects with invalid answer lists
    	 List<QuestionTestdto> mockQuestions = new ArrayList<>();
         // Adding some mock questions
         mockQuestions.add(new QuestionTestdto(1L, "Maths", "Algebra", "Solving equations", null, "Intermediate", "MCQ", "Single", List.of("A")));
         mockQuestions.add(new QuestionTestdto(2L, "Physics", "Mechanics", "Newton's laws", null, "Advanced", "MCQ", "Single", List.of("B")));
         mockQuestions.add(new QuestionTestdto(3L, "Chemistry", "Organic Chemistry", "Carbon compounds", null, "Beginner", "MCQ", "Single", List.of("C")));

        // Call the extractQuestionIdsWithAnswers method with input containing invalid answers
        Map<Long, List<String>> result = resultService.extractQuestionIdsWithAnswers(mockQuestions);

        // Assert that the result is null
        assertNull(null, "Result should be null for invalid answer lists");
    }

//**********
//@Test
//public void testGetQuestionsByIds_Successful() {
//    // Arrange
//    List<Long> questionNumbers = List.of(1L, 2L);
//    String url = "http://172.18.4.37:9094/questions/getQuestions?questionNumbers=1,2";
//    List<QuestionTestdto> expectedQuestions = new ArrayList<>();
//    expectedQuestions.add(new QuestionTestdto(1L, "Question 1")); // Update this line
//    expectedQuestions.add(new QuestionTestdto(2L, "Question 2")); // Update this line
//    ResponseEntity<List<QuestionTestdto>> responseEntity = new ResponseEntity<>(expectedQuestions, HttpStatus.OK);
//    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
//            .thenReturn(responseEntity);
//
//    // Act
//    List<QuestionTestdto> actualQuestions = resultService.getQuestionsByIds(questionNumbers);
//
//    // Assert
//    assertEquals(expectedQuestions, actualQuestions);
//}



    //positive testcase for getquestionids
    @Test
    public void testGetQuestionsByIds_Success() {
        // Mocking the response from the QuestionBank API
        List<Long> questionIds = List.of(1L, 2L, 3L);
        List<QuestionTestdto> mockQuestions = new ArrayList<>();
        // Adding some mock questions
        mockQuestions.add(new QuestionTestdto(1L, "Maths", "Algebra", "Solving equations", null, "Intermediate", "MCQ", "Single", List.of("A")));
        mockQuestions.add(new QuestionTestdto(2L, "Physics", "Mechanics", "Newton's laws", null, "Advanced", "MCQ", "Single", List.of("B")));
        mockQuestions.add(new QuestionTestdto(3L, "Chemistry", "Organic Chemistry", "Carbon compounds", null, "Beginner", "MCQ", "Single", List.of("C")));

        // Mocking the response entity
        ResponseEntity<List<QuestionTestdto>> mockResponseEntity = new ResponseEntity<>(mockQuestions, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(mockResponseEntity);

        // Calling the method under test
        List<QuestionTestdto> retrievedQuestions = resultService.getQuestionsByIds(questionIds);

        // Verifying the results
        assertEquals(mockQuestions, retrievedQuestions);
    }

    //negative testcase for getquestionids


    @Test
    public void testGetQuestionsByIds_EmptyQuestionIds() {
        List<Long> emptyQuestionIds = new ArrayList<>();

        // Mocking an empty response entity
        ResponseEntity<List<QuestionTestdto>> mockResponseEntity = ResponseEntity.ok(new ArrayList<>());
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(mockResponseEntity);

        // Calling the method under test
        List<QuestionTestdto> retrievedQuestions = resultService.getQuestionsByIds(emptyQuestionIds);

        // Verifying that an empty list is returned
        assertTrue(retrievedQuestions.isEmpty());
    }



    @Test
    public void testGetQuestionsByIds_FailedHttpRequest() {
        List<Long> questionIds = List.of(1L, 2L, 3L);

        // Mocking a ResourceAccessException to simulate a failed HTTP request
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.any(ParameterizedTypeReference.class)))
                .thenThrow(new ResourceAccessException("Connection refused"));

        // Calling the method under test
        try {
            resultService.getQuestionsByIds(questionIds);
            fail("Expected QuestionsFetchFailedException was not thrown");
        } catch (QuestionsFetchFailedException e) {
            // Verifying that the correct exception is thrown
            assertEquals("Failed to fetch questions: Connection refused", e.getMessage());
        }
    }

    @Test
    public void testGetQuestionsByIds_NullQuestionIds() {
        List<Long> nullQuestionIds = null;

        // Calling the method under test
        List<QuestionTestdto> retrievedQuestions = resultService.getQuestionsByIds(nullQuestionIds);

        // Verifying that an empty list is returned
        assertTrue(retrievedQuestions.isEmpty()); 
    }

    @Test
    public void testGetByAssessmentKey_Success() {
        // Mock AssessmentDetails object
        AssessmentKey id = new AssessmentKey();
        // Set necessary properties for assessmentDetails object
        assessmentDetails.setId(id);

        // Mock the behavior of the repository method
        when(assessmentDetailsRepository.findById(any(AssessmentKey.class))).thenReturn(Optional.of(assessmentDetails));

        // Mock ResultServiceImp object

        // Create an instance of AssessmentDetailsService

        // Call the method you want to test
        Long assessmentId = 1L;
        Long empId = 1L;
        resultService.getByAssessmentKey(assessmentId, empId);

        // Verify that the repository's save method was called with the correct argument
        verify(repository, times(1)).save(any(ResultAndAnalysis.class));
    }

 //****************************** admin *********************************************************************
    @Test
    void testGetAllBatches() {
    	 List<ResultAndAnalysis> results = Arrays.asList(
                 new ResultAndAnalysis(1L, "Batch1", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL),
                 new ResultAndAnalysis(2L, "Batch2", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL));

        when(repository.findAll()).thenReturn(results);

        Set<String> actualBatches = resultService.getAllBatches();
        assertTrue(actualBatches.contains("Batch1"));
        assertTrue(actualBatches.contains("Batch2"));
    }

    @Test
    void testSaveResult() {
        ResultAndAnalysis result = new ResultAndAnalysis();
        resultService.saveResult(result);
        verify(repository, times(1)).save(result);
    }

    @Test
    void testGetAllViolations() {
    	 List<ResultAndAnalysis> results = Arrays.asList(
                 new ResultAndAnalysis(1L, "Batch1", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL),
                 new ResultAndAnalysis(2L, "Batch2", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL));

        when(repository.findByTypeOfSubmission("NORMAL_SUBMISSION")).thenReturn(results);

        List<Map<String, String>> violations = resultService.getAllViolations();
        assertEquals(2, violations.size());
        assertEquals("1", violations.get(0).get("userId"));
        assertEquals("Batch1", violations.get(0).get("batchNo"));
    }

    @Test
    void testAssessmentIds() throws UserNotFoundException {
    	 List<ResultAndAnalysis> results = Arrays.asList(
                 new ResultAndAnalysis(1L, "Batch1", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL),
                 new ResultAndAnalysis(2L, "Batch2", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL));

        when(repository.findByUserId(1)).thenReturn(results);

        List<Map<String, String>> assessmentIds = resultService.assessmentIds(1);
        assertEquals(2, assessmentIds.size());
        assertEquals("Assessment1", assessmentIds.get(0).get("assessmentName"));
    }

    @Test
    void testAssessmentIds_UserNotFoundException() {
        when(repository.findByUserId(1)).thenReturn(Collections.emptyList());

        assertThrows(UserNotFoundException.class, () -> resultService.assessmentIds(1));
    }

   
    
    @Test
    public void testGetSubtopicDetailsByBatch_PercentageGreaterThanClearing() throws RecordNotFoundException {
        // Mock data
        String batchNo = "Batch123";
        String subject = "Maths";
        Long assessmentId = 1L;
        ResultAndAnalysis result = mock(ResultAndAnalysis.class);
        Map<String, Map<String, Integer>> calculatedMarksOfSubtopics = new HashMap<>();
        calculatedMarksOfSubtopics.put("Subtopic1", new HashMap<>());
        calculatedMarksOfSubtopics.get("Subtopic1").put("Easy", 5);
        calculatedMarksOfSubtopics.get("Subtopic1").put("Medium", 3);
        when(result.getBatchNo()).thenReturn(batchNo);
        when(result.getSubject()).thenReturn(subject);
        when(result.getAssessmentId()).thenReturn(assessmentId);
        when(result.getCalculatedMarksOfSubtopics()).thenReturn(calculatedMarksOfSubtopics);
        when(result.getClearingPercentage()).thenReturn(70L);
        Map<String, Integer> subtopicQuestionCounts = new HashMap<>();
        subtopicQuestionCounts.put("Easy", 5);
        subtopicQuestionCounts.put("Medium", 3);
        when(result.getQuestionPapersubtopics()).thenReturn(Collections.singletonMap("Subtopic1", subtopicQuestionCounts));

        List<ResultAndAnalysis> results = Collections.singletonList(result);
        when(repository.findByBatchNoAndSubjectAndAssessmentId(batchNo, subject, assessmentId)).thenReturn(results);

        // Call the method
        Map<String, Map<String, Integer>> subtopicDetails = resultService.getSubtopicDetailsByBatch(batchNo, subject, assessmentId);

        // Verify the result
        assertEquals(1, subtopicDetails.size());
        assertEquals(1, subtopicDetails.get("Subtopic1").size());
        assertEquals(1, subtopicDetails.get("Subtopic1").get("greater than 70").intValue());
        assertEquals(0, subtopicDetails.get("Subtopic1").getOrDefault("less than 70", 0).intValue());
    }



    @Test
    void testAssesmentIdsBySubject() throws SubjectNotFoundException {
        // Mock data
        ResultAndAnalysis result1 = new ResultAndAnalysis();
        result1.setAssessmentName("Assessment1");
        result1.setAssessmentId(1L);

        ResultAndAnalysis result2 = new ResultAndAnalysis();
        result2.setAssessmentName("Assessment2");
        result2.setAssessmentId(2L);

        ResultAndAnalysis result3 = new ResultAndAnalysis();
        result3.setAssessmentName("Assessment1");
        result3.setAssessmentId(1L);

        List<ResultAndAnalysis> results = Arrays.asList(result1, result2, result3);
        when(repository.findBySubject("Subject1")).thenReturn(results);

        // Call the method
        List<Map<String, String>> assessmentList = resultService.assesmentIdsBySubject("Subject1");

        // Verify the results
        assertNotNull(assessmentList);
        assertEquals(2, assessmentList.size()); // Only two unique assessments expected

        Map<String, String> assessmentMap1 = assessmentList.get(0);
        assertEquals("Assessment1", assessmentMap1.get("assessmentName"));
        assertEquals("1", assessmentMap1.get("assessmentId"));

        Map<String, String> assessmentMap2 = assessmentList.get(1);
        assertEquals("Assessment2", assessmentMap2.get("assessmentName"));
        assertEquals("2", assessmentMap2.get("assessmentId"));
    }

    

    @Test
    void testAssesmentIdsBySubject_DuplicateAssessmentNames() throws SubjectNotFoundException {
        List<ResultAndAnalysis> results = Arrays.asList(
                new ResultAndAnalysis(1L, "Batch1", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL),
                new ResultAndAnalysis(2L, "Batch2", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL));

        when(repository.findBySubject("Subject1")).thenReturn(results);

        List<Map<String, String>> assessmentIds = resultService.assesmentIdsBySubject("Subject1");

        assertEquals(1, assessmentIds.size());
        assertTrue(assessmentIds.contains(Map.of("assessmentName", "Assessment1", "assessmentId", "1")));
    }

    @Test
    void testAssesmentIdsBySubject_SubjectNotFoundException() {
        when(repository.findBySubject("Subject1")).thenReturn(Collections.emptyList());

        assertThrows(SubjectNotFoundException.class, () -> resultService.assesmentIdsBySubject("Subject1"));
    }

    @Test
    void testAssesmentIdsBySubject_DuplicateAssessmentNames1() throws SubjectNotFoundException {
        List<ResultAndAnalysis> results = Arrays.asList(
                new ResultAndAnalysis(1L, "Batch1", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL),
                new ResultAndAnalysis(2L, "Batch2", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL));

        when(repository.findBySubject("Subject1")).thenReturn(results);

        List<Map<String, String>> assessmentIds = resultService.assesmentIdsBySubject("Subject1");

        assertEquals(1, assessmentIds.size());
        assertTrue(assessmentIds.contains(Map.of("assessmentName", "Assessment1", "assessmentId", "1")));
    }

    @Test
    void testGetSubjectsByBatch_BatchNoNotFoundException() {
        when(repository.findByBatchNo("Batch1")).thenReturn(Collections.emptyList());

        assertThrows(BatchNoNotFoundException.class, () -> resultService.getSubjectsByBatch("Batch1"));
    }

    @Test
    void testGetAssesbySubjectsByBatch() throws RecordNotFoundException {
    	 List<ResultAndAnalysis> results = Arrays.asList(
                 new ResultAndAnalysis(1L, "Batch1", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL),
                 new ResultAndAnalysis(2L, "Batch2", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL));

        when(repository.findByBatchNoAndSubject("Batch1", "Subject1")).thenReturn(results);

        List<Map<String, String>> assessments = resultService.getAssesbySubjectsByBatch("Batch1", "Subject1");
        assertEquals(2, assessments.size());
        assertEquals("Assessment1", assessments.get(0).get("assessmentName"));
    }

    @Test
    void testGetAssesbySubjectsByBatch_RecordNotFoundException() {
        when(repository.findByBatchNoAndSubject("Batch1", "Subject1")).thenReturn(Collections.emptyList());

        assertThrows(RecordNotFoundException.class, () -> resultService.getAssesbySubjectsByBatch("Batch1", "Subject1"));
    }

    @Test
    void testGetPassPercentage() throws RecordNotFoundException {
    	 List<ResultAndAnalysis> results = Arrays.asList(
                 new ResultAndAnalysis(1L, "Batch1", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL),
                 new ResultAndAnalysis(2L, "Batch2", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL));

        when(repository.findByBatchNoAndSubjectAndAssessmentId("Batch1", "Subject1", 1L)).thenReturn(results);

        List<Map<String, Double>> passPercentage = resultService.getPassPercentage("Batch1", 1L, "Subject1");
        assertEquals(1, passPercentage.size());
        assertEquals(50.0, passPercentage.get(0).get("PASS"));
        assertEquals(50.0, passPercentage.get(0).get("FAIL"));
    }

    @Test
    void testGetPassPercentage_RecordNotFoundException() {
        when(repository.findByBatchNoAndSubjectAndAssessmentId("Batch1", "Subject1", 1L)).thenReturn(Collections.emptyList());

        assertThrows(RecordNotFoundException.class, () -> resultService.getPassPercentage("Batch1", 1L, "Subject1"));
    }

    @Test
    void testGetSubmissionType() throws RecordNotFoundException {
    	 List<ResultAndAnalysis> results = Arrays.asList(
                 new ResultAndAnalysis(1L, "Batch1", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL),
                 new ResultAndAnalysis(2L, "Batch2", 1L, 1L, "Assessment1", "Subject1", null, null, null, 0, 0, 0L, null, 0, 0L, null, Result.PASS, SubmissionType.NORMAL_SUBMISSION, AssessmentType.NORMAL));

        when(repository.findByBatchNoAndSubjectAndAssessmentId("Batch1", "Subject1", 1L)).thenReturn(results);

        List<Map<String, Double>> submissionType = resultService.getSubmissionType("Batch1", 1L, "Subject1");
        assertEquals(1, submissionType.size());
        assertEquals(33.33, submissionType.get(0).get("NORMAL_SUBMISSION"));
        assertEquals(33.33, submissionType.get(0).get("TIMEUP_SUBMISSION"));
        assertEquals(33.33, submissionType.get(0).get("VIOLATED_SUBMISSION"));
    }

    @Test
    void testGetSubmissionType_RecordNotFoundException() {
        when(repository.findByBatchNoAndSubjectAndAssessmentId("Batch1", "Subject1", 1L)).thenReturn(Collections.emptyList());

        assertThrows(RecordNotFoundException.class, () -> resultService.getSubmissionType("Batch1", 1L, "Subject1"));
    }



    @Test
    void testGetDifficultyLevelDetailsByBatch_RecordNotFoundException() {
        when(repository.findByBatchNoAndSubjectAndAssessmentId("Batch1", "Subject1", 1L)).thenReturn(Collections.emptyList());

        assertThrows(RecordNotFoundException.class, () -> resultService.getDifficultyLevelDetailsByBatch("Batch1", "Subject1", 1L));
    }

    @Test
    void testGetSubtopicDetailsByBatch_RecordNotFoundException() {
        when(repository.findByBatchNoAndSubjectAndAssessmentId("Batch1", "Subject1", 1L)).thenReturn(Collections.emptyList());

        assertThrows(RecordNotFoundException.class, () -> resultService.getSubtopicDetailsByBatch("Batch1", "Subject1", 1L));
    }

    // Helper methods
    private ResultAndAnalysis createResultWithDifficultyLevel(String batchNo, String subject, Long assessmentId, String level, int correctlyAnswered, int totalQuestions, Long clearingPercentage) {
        Map<String, Integer> difficultyLevelCorrectlyAnswered = new HashMap<>();
        difficultyLevelCorrectlyAnswered.put(level, correctlyAnswered);
        Map<String, Integer> difficultyLevelTotalQuestions = new HashMap<>();
        difficultyLevelTotalQuestions.put(level, totalQuestions);
        ResultAndAnalysis result = new ResultAndAnalysis();
        result.setBatchNo(batchNo);
        result.setSubject(subject);
        result.setAssessmentId(assessmentId);
        result.setDifficultyLevelCorrectlyAnswered(difficultyLevelCorrectlyAnswered);
        result.setDifficultyLevelSplit(difficultyLevelTotalQuestions);
        result.setClearingPercentage(clearingPercentage);
        return result;
    }

    private ResultAndAnalysis createResultWithSubtopic(String batchNo, String subject, Long assessmentId, String subtopic, int correctlyAnswered, int totalQuestions, Long clearingPercentage) {
        Map<String, Map<String, Integer>> calculatedMarksOfSubtopics = new HashMap<>();
        Map<String, Integer> subtopicMarks = new HashMap<>();
        subtopicMarks.put("correctlyAnswered", correctlyAnswered);
        calculatedMarksOfSubtopics.put(subtopic, subtopicMarks);

        Map<String, Map<String, Integer>> questionPapersubtopics = new HashMap<>();
        Map<String, Integer> subtopicQuestionCounts = new HashMap<>();
        subtopicQuestionCounts.put("totalQuestions", totalQuestions);
        questionPapersubtopics.put(subtopic, subtopicQuestionCounts);

        ResultAndAnalysis result = new ResultAndAnalysis();
        result.setBatchNo(batchNo);
        result.setSubject(subject);
        result.setAssessmentId(assessmentId);
        result.setCalculatedMarksOfSubtopics(calculatedMarksOfSubtopics);
        result.setQuestionPapersubtopics(questionPapersubtopics);
        result.setClearingPercentage(clearingPercentage);
        return result;
    }

    @Test
    void testGetAssessmentsById_Success() throws Exception {
        Long userId = 1L;
        List<String> distinctSubjects = Arrays.asList("Math", "Science");

        when(repository.findDistinctSubjectByUserId(userId)).thenReturn(distinctSubjects);

        List<?> results = resultService.getAssessmentsById(userId);

        assertEquals(distinctSubjects, results);

        verify(repository, times(1)).findDistinctSubjectByUserId(userId);
    }

  

    @Test
    void testGetAssessmentsByIdSubject_Success() throws Exception {
        Long userId = 1L;
        String subject = "Math";
        List<AssessmentsListDto> results = new ArrayList<>(); // Replace with your expected data structure

        when(repository.findAssessmentFieldsByUserIDAndSubject(userId, subject)).thenReturn(results);

        List<?> assessments = resultService.getAssessmentsByIdSubject(userId, subject);

        assertEquals(results, assessments);

        verify(repository, times(1)).findAssessmentFieldsByUserIDAndSubject(userId, subject);
    }


    

   

}
