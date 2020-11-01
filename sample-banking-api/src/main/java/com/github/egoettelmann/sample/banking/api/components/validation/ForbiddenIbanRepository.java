package com.github.egoettelmann.sample.banking.api.components.validation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ForbiddenIbanRepository extends PagingAndSortingRepository<ForbiddenIbanDbo, Long> {

    boolean existsByIban(String iban);

}
