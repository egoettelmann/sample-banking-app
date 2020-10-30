package com.github.egoettelmann.sample.banking.api.components.accounts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BankAccountRepository extends PagingAndSortingRepository<BankAccountDbo, Long> {

    Page<BankAccountDbo> findAllByUserId(Long userId, Pageable pageable);

}
