package com.github.egoettelmann.sample.banking.api.core.exceptions.payment;

import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;

public class ForbiddenIbanException extends InvalidPaymentException {

    public ForbiddenIbanException(String message) {
        super(message);
    }
}
