package com.github.egoettelmann.sample.banking.api.config.rest;

import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

@Configuration
public class SwaggerConfig {

    public SwaggerConfig() {
        SpringDocUtils.getConfig()
                .addRequestWrapperToIgnore(Pageable.class);
    }
}
