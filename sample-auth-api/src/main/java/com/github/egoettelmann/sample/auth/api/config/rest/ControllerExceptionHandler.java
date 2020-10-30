package com.github.egoettelmann.sample.auth.api.config.rest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

/**
 * Intercepts all exceptions thrown on controllers.
 * Exceptions are output as JSON with the problem-spring-web module.
 */
@ControllerAdvice
public class ControllerExceptionHandler implements ProblemHandling, SecurityAdviceTrait {
}
