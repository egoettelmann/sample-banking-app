package com.github.egoettelmann.sample.auth.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class SampleAuthApi {

	public static void main(String[] args) {
		SpringApplication.run(SampleAuthApi.class, args);
	}

}
