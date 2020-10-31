package com.github.egoettelmann.sample.auth.api.config.rest;

import com.github.egoettelmann.sample.auth.api.controllers.AuthenticationController;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public SwaggerConfig() {
        SpringDocUtils.getConfig()
                .addRestControllers(AuthenticationController.class);
    }

    @Bean
    public OpenAPI sampleAuthApiSpecs() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sample Auth API")
                        .description("Sample project: auth API with Spring Boot")
                        .version("v0.0.1-SNAPSHOT")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Github Link")
                        .url("https://github.com/egoettelmann/sample-banking-app"))
                ;
    }

}
