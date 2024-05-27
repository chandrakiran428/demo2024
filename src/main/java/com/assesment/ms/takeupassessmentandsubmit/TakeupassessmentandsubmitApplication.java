package com.assesment.ms.takeupassessmentandsubmit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.assesment.ms.takeupassessmentandsubmit.resultAndAnalysisConverter.ResultAndAnalysisConverter;



@SpringBootApplication
public class TakeupassessmentandsubmitApplication {

	public static void main(String[] args) {
		SpringApplication.run(TakeupassessmentandsubmitApplication.class, args);
	}
	
	 @Bean
	   public ResultAndAnalysisConverter resultAndAnalysisConverter() {
		   return new ResultAndAnalysisConverter();
	   }

}
