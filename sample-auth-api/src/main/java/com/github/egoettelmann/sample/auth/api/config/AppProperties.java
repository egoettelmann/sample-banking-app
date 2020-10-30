package com.github.egoettelmann.sample.auth.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "auth-api")
public class AppProperties {

    private List<String> allowedOrigins;

}
