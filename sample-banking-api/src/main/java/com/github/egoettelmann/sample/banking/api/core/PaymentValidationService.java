package com.github.egoettelmann.sample.banking.api.core;

import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;

public interface PaymentValidationService {

    void validateInternalPayment(Payment payment);

    void validateExternalPayment(Payment payment);

    void validatePaymentDeletion(Payment payment);

}
