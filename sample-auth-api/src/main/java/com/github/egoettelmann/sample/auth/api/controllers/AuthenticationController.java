package com.github.egoettelmann.sample.auth.api.controllers;

import com.github.egoettelmann.sample.auth.api.core.dtos.TokenHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication API specification for Swagger documentation and Code Generation.
 * Implemented by Spring Security.
 */
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    /**
     * Implemented by Spring Security
     */
    @PostMapping(value = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TokenHolder login(
            @RequestPart("username") String username,
            @RequestPart("password") String password
    ) {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }

    /**
     * Implemented by Spring Security
     */
    @PostMapping("/logout")
    public void logout() {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }

}
