package com.github.egoettelmann.sample.banking.api.config.rest;

import com.github.egoettelmann.sample.banking.api.core.exceptions.DataNotFoundException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

/**
 * Intercepts all exceptions thrown on controllers.
 * Exceptions are output as JSON with the problem-spring-web module.
 */
@ControllerAdvice
public class ControllerExceptionHandler implements ProblemHandling, SecurityAdviceTrait {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Problem> handleDataNotFound(DataNotFoundException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .with("message", ex.getMessage())
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(InvalidPaymentException.class)
    @ResponseBody
    public ResponseEntity<Problem> handleInvalidPayment(InvalidPaymentException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .with("message", ex.getMessage())
                .build();
        return create(ex, problem, request);
    }

}
