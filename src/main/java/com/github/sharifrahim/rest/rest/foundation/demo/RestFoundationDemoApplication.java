package com.github.sharifrahim.rest.rest.foundation.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Entry point for the Spring Boot REST Foundation Demo Application.
 * 
 * @author Sharif
 * @see <a href=
 *      "https://github.com/sharifrahim">https://github.com/sharifrahim</a>
 */
@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class RestFoundationDemoApplication {

	/**
	 * Main method to launch the Spring Boot application.
	 *
	 * @param args the application arguments
	 */
	public static void main(String[] args) {
		log.info("Starting RestFoundationDemoApplication...");
		SpringApplication.run(RestFoundationDemoApplication.class, args);
		log.info("RestFoundationDemoApplication started successfully.");
	}

}
