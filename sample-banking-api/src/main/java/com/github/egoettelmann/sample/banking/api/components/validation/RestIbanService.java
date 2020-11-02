package com.github.egoettelmann.sample.banking.api.components.validation;

import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.InvalidIbanException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
class RestIbanService {

    private static final String VALIDATION_URL = "/validate/{iban-number}";

    private static final String DEFAULT_VALIDATION_MESSAGE = "No message";

    private final RestTemplate restTemplate;

    public RestIbanService(
            @Qualifier("ibanRestTemplate") RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public void validate(final String iban) {
        final String uri = UriComponentsBuilder.fromPath(VALIDATION_URL)
                .buildAndExpand(iban)
                .toUriString();
        OpenIbanValidationResponse validationResponse;
        try {
            ResponseEntity<OpenIbanValidationResponse> response = restTemplate.exchange(uri, HttpMethod.GET, buildRequestEntity(), OpenIbanValidationResponse.class);
            validationResponse = response.getBody();
        } catch (Exception e) {
            log.error("An error occurred while calling the remote IBAN service: ", e);
            throw new InvalidPaymentException("IBAN validation service exception");
        }

        if (validationResponse == null) {
            throw new InvalidPaymentException("IBAN validation service exception: no response");
        }
        if (!validationResponse.isValid()) {
            String errorMessage = DEFAULT_VALIDATION_MESSAGE;
            if (validationResponse.getMessages() != null && !validationResponse.getMessages().isEmpty()) {
                errorMessage = validationResponse.getMessages().get(0);
            }
            throw new InvalidIbanException("IBAN validation failed: " + errorMessage);
        }
    }

    /**
     * The Remote service requires to use a Browser 'User-Agent', otherwise the request is blocked.
     *
     * @return the HTTP entity
     */
    private HttpEntity<?> buildRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0");
        return new HttpEntity<>(headers);
    }

    @Data
    private static class OpenIbanValidationResponse {

        private boolean valid;
        private List<String> messages;

    }

}
