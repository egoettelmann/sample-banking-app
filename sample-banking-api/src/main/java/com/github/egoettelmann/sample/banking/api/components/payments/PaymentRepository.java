package com.github.egoettelmann.sample.banking.api.components.payments;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PaymentRepository extends PagingAndSortingRepository<PaymentDbo, Long>, JpaSpecificationExecutor<PaymentDbo> {

}
