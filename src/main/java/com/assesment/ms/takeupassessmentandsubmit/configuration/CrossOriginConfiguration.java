package com.assesment.ms.takeupassessmentandsubmit.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossOriginConfiguration implements WebMvcConfigurer{
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Allow CORS for all endpoints
		.allowedOrigins("*") // Allow requests from any origin
		.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // Allowed HTTP methods
		.allowedHeaders("*"); // Allowed headers

	}
}
