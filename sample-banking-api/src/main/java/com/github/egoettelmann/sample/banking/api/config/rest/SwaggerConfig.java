package com.github.egoettelmann.sample.banking.api.config.rest;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    public SwaggerConfig() {
        SpringDocUtils.getConfig()
                .addRequestWrapperToIgnore(Pageable.class);
    }

    @Bean
    public OpenAPI sampleBankingApiSpecs() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sample Banking API")
                        .description("Sample project: banking API with Spring Boot")
                        .version("v0.0.1-SNAPSHOT")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Github Link")
                        .url("https://github.com/egoettelmann/sample-banking-app"))
                .components(jwtSecurityScheme())
                .addSecurityItem(jwtSecurityRequirement())
                ;
    }

    private Components jwtSecurityScheme() {
        return new Components()
                .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER).name("Authorization")
                );
    }

    private SecurityRequirement jwtSecurityRequirement() {
        return new SecurityRequirement()
                .addList("bearer-jwt", Arrays.asList("read", "write"));
    }
}
