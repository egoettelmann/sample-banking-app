package com.github.egoettelmann.sample.banking.api.core.exceptions.payment;

import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;

public class PaymentBeneficiarySameAsGiverAccountException extends InvalidPaymentException {

    public PaymentBeneficiarySameAsGiverAccountException(String message) {
        super(message);
    }
}
