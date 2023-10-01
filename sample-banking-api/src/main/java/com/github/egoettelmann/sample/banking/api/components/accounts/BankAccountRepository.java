package com.github.egoettelmann.sample.banking.api.components.accounts;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BankAccountRepository extends PagingAndSortingRepository<BankAccountDbo, Long>, JpaSpecificationExecutor<BankAccountDbo> {

}
