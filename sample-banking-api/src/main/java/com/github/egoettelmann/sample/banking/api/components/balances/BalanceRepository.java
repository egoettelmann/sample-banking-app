package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BalanceRepository extends CrudRepository<BalanceDbo, Long> {

    List<BalanceDbo> findAllByAccountId(Long accountId);

    BalanceDbo getByAccountIdAndStatus(Long accountId, BalanceStatus status);

}
