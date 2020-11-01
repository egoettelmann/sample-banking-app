package com.github.egoettelmann.sample.banking.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "banking-api")
public class AppProperties {

    private List<String> allowedOrigins;

    private String ibanApiUrl;

}
