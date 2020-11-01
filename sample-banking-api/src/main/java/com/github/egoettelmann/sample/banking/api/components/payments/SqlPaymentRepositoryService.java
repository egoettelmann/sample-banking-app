package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class SqlPaymentRepositoryService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    @Autowired
    public SqlPaymentRepositoryService(
            PaymentRepository paymentRepository,
            PaymentMapper paymentMapper
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    public Page<Payment> getPaymentsForUserId(Long userId, Pageable pageable) {
        return paymentRepository.findAllByGiverAccountId(userId, pageable).map(paymentMapper::to);
    }

    public Payment savePayment(Payment payment) {
        PaymentDbo dbo = paymentRepository.save(
                paymentMapper.from(payment)
        );
        return paymentMapper.to(dbo);
    }

}
