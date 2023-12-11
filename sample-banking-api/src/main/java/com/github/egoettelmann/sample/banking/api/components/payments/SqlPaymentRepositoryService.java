package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Page<Payment> findAll(PaymentFilter filter, Pageable pageable) {
        return paymentRepository.findAll(
                paymentSpecification(filter),
                pageable
        ).map(paymentMapper::to);
    }

    public Optional<Payment> findOne(PaymentFilter filter) {
        return paymentRepository.findOne(
                paymentSpecification(filter)
        ).map(paymentMapper::to);
    }

    public Payment save(Payment payment) {
        final PaymentFilter filter = PaymentFilter.builder()
                .reference(payment.getReference())
                .originAccountNumber(payment.getOriginAccountNumber())
                .build();
        final Optional<PaymentDbo> existing = paymentRepository.findOne(
                paymentSpecification(filter)
        );
        PaymentDbo dbo = existing.orElse(new PaymentDbo());
        dbo = paymentRepository.save(
                paymentMapper.from(payment, dbo)
        );
        return paymentMapper.to(dbo);
    }

    private Specification<PaymentDbo> paymentSpecification(PaymentFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            // Reference
            if (filter.getReference() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(PaymentDbo.Fields.reference),
                                filter.getReference()
                        )
                );
            }

            // Origin Account Number
            if (filter.getOriginAccountNumber() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(PaymentDbo.Fields.originAccountNumber),
                                filter.getOriginAccountNumber()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
