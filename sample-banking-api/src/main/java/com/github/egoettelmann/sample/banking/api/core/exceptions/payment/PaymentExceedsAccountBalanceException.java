package com.github.egoettelmann.sample.banking.api.core.exceptions.payment;

import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;

public class PaymentExceedsAccountBalanceException extends InvalidPaymentException {

    public PaymentExceedsAccountBalanceException(String message) {
        super(message);
    }
}
