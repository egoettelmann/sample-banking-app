package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.PaymentService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class DefaultPaymentService implements PaymentService {

    private final SqlPaymentRepositoryService sqlPaymentRepositoryService;

    @Autowired
    public DefaultPaymentService(SqlPaymentRepositoryService sqlPaymentRepositoryService) {
        this.sqlPaymentRepositoryService = sqlPaymentRepositoryService;
    }

    @Override
    public Page<Payment> getPaymentsForUser(AppUser user, Pageable pageable) {
        return sqlPaymentRepositoryService.getPaymentsForUserId(user.getId(), pageable);
    }
}
