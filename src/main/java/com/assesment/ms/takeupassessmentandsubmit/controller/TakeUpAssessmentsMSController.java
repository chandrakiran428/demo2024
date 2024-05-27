package com.assesment.ms.takeupassessmentandsubmit.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assesment.ms.takeupassessmentandsubmit.service.TakeUpAssessmentsMsService;

@RestController
@RequestMapping("/questions")
public class TakeUpAssessmentsMSController {

	@Autowired
	private TakeUpAssessmentsMsService takeUpAssessmentsMsService;
	
	
	
	public TakeUpAssessmentsMSController(TakeUpAssessmentsMsService takeUpAssessmentsMsService) {
		this.takeUpAssessmentsMsService = takeUpAssessmentsMsService;
	}



	@GetMapping("/paper/{paperId}")
    public ResponseEntity<?> getQuestionsForPaper(@PathVariable int paperId) {

        return takeUpAssessmentsMsService.getQuestionsForPaper(paperId);
    }
}

