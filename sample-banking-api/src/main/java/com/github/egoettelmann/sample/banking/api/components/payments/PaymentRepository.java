package com.github.egoettelmann.sample.banking.api.components.payments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PaymentRepository extends PagingAndSortingRepository<PaymentDbo, Long> {

    PaymentDbo findByIdAndGiverAccountUserId(Long paymentId, Long userId);

    Page<PaymentDbo> findAllByGiverAccountId(Long userId, Pageable pageable);

}
