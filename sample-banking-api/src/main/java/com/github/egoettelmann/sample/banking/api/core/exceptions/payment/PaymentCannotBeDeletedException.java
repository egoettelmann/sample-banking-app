package com.github.egoettelmann.sample.banking.api.core.exceptions.payment;

import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;

public class PaymentCannotBeDeletedException extends InvalidPaymentException {

    public PaymentCannotBeDeletedException(String message) {
        super(message);
    }
}
