package com.github.egoettelmann.sample.banking.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class SampleBankingApi {

    public static void main(String[] args) {
        SpringApplication.run(SampleBankingApi.class, args);
    }

}
