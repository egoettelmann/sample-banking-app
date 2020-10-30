package com.github.egoettelmann.sample.auth.api.config.rest;

import com.github.egoettelmann.sample.auth.api.controllers.AuthenticationController;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public SwaggerConfig() {
        SpringDocUtils.getConfig()
                .addRestControllers(AuthenticationController.class);
    }
}
