package com.github.egoettelmann.sample.banking.api.components.balances;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BalanceRepository extends CrudRepository<BalanceDbo, Long>, JpaSpecificationExecutor<BalanceDbo> {

}
