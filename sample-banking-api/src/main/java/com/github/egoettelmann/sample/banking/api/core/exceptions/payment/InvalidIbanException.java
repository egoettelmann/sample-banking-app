package com.github.egoettelmann.sample.banking.api.core.exceptions.payment;

import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;

public class InvalidIbanException extends InvalidPaymentException {

    public InvalidIbanException(String message) {
        super(message);
    }
}
