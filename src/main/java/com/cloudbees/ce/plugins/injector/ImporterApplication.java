package com.cloudbees.ce.plugins.injector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ImporterApplication {
	public static void main(String[] args) {
		SpringApplication.run(ImporterApplication.class, args);
	}
}
