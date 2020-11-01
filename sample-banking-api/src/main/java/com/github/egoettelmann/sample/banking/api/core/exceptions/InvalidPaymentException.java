package com.github.egoettelmann.sample.banking.api.core.exceptions;

public class InvalidPaymentException extends RuntimeException {

    public InvalidPaymentException(String message) {
        super(message);
    }
}
