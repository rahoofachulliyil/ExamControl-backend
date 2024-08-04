package com.mea.examcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ExamcontrolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamcontrolApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "http://localhost:7194",
						"http://103.86.176.161:3001", "http://103.86.176.161:3002","*");
			}
		};
	}

}
