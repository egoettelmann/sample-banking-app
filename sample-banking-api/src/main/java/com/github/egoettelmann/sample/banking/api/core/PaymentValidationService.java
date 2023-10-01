package com.github.egoettelmann.sample.banking.api.core;

import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;

public interface PaymentValidationService {

    void checkPaymentCreation(AppUser user, PaymentRequest paymentRequest);

}
