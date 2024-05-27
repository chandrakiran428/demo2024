package com.assesment.ms.takeupassessmentandsubmit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.assesment.ms.takeupassessmentandsubmit.exceptions.QuestionPaperNotFoundException;

@Service
public class TakeUpAssessmentsMsService {
    
    @Value("${question.paper.url}")
    private String questionPaperUrl;


    
    @Autowired
    private RestTemplate restTemplate;    

    public ResponseEntity<?> getQuestionsForPaper(int questionPaperId) {
        
        String url = questionPaperUrl + "/" + questionPaperId;
        try {
            // Fetch the object containing the list of question numbers and question paper ID
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("questionNumbers") && response.containsKey("questionPaperId")) {
                // Extract the list of question numbers and question paper ID from the response
                List<Integer> questionNumbers = (List<Integer>) response.get("questionNumbers");
                int questionPaperIdFromResponse = (int) response.get("questionPaperId");
                if (!questionNumbers.isEmpty()) {
                    // Convert the list of question numbers to a comma-separated string
                    String questionNumbersString = questionNumbers.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));   
                    // Construct the URL for the next request with request parameters
                    String fetchUrl = "http://172.18.4.37:9094/questions/getQuestions?questionNumbers=" + questionNumbersString;
                    // Make the request to the next URL
                    List<Map<String, Object>> questions = restTemplate.getForObject(fetchUrl, List.class);
                    
                    List<Map<String, Object>> extractedQuestions = new ArrayList<>();
                    for (Map<String, Object> question : questions) {
                        Map<String, Object> extractedQuestion = new HashMap<>();
                        extractedQuestion.put("title", question.get("title"));
                        extractedQuestion.put("options", question.get("options"));
                        extractedQuestion.put("questionId", question.get("questionId"));
                        List<?> answers = (List<?>) question.get("answers");
                        extractedQuestion.put("answersLength", answers != null ? answers.size() : 0);
                        extractedQuestions.add(extractedQuestion);
                    }
                    
                    // Add question paper ID to the final response
                    Map<String, Object> result = new HashMap<>();
                    result.put("questionPaperId", questionPaperIdFromResponse);
                    result.put("questions", extractedQuestions);

                    return ResponseEntity.ok(result);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.badRequest().body("Error fetching question numbers or question paper ID for the given paper ID.");
            }
        } catch (HttpClientErrorException.NotFound ex) {
            throw new QuestionPaperNotFoundException("Question paper not found with ID: " + questionPaperId);
        }
    }
}
