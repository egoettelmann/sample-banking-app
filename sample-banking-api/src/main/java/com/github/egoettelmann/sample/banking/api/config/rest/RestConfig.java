package com.github.egoettelmann.sample.banking.api.config.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.egoettelmann.sample.banking.api.config.AppProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
public class RestConfig {

    /**
     * The IBAN rest template.
     *
     * @param appProperties the app properties
     * @return the rest template
     */
    @Bean("ibanRestTemplate")
    public RestTemplate ibanRestTemplate(final AppProperties appProperties) {
        return new RestTemplateBuilder()
                .rootUri(appProperties.getIbanApiUrl())
                .build();
    }

    /**
     * Default Jackson mapper for REST endpoints.
     *
     * @return the object mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .modules(
                        new JavaTimeModule(),
                        new ProblemModule(),
                        new ConstraintViolationProblemModule()
                )
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();
    }

}
