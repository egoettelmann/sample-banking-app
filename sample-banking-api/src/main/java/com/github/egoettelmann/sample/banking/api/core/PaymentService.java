package com.github.egoettelmann.sample.banking.api.core;

import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    Page<Payment> getPaymentsForUser(AppUser user, Pageable pageable);

    Payment createPayment(AppUser user, PaymentRequest paymentRequest);

    void deletePayment(AppUser user, Long paymentId);

}
