package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class SqlBalanceRepositoryService {

    private final BalanceRepository balanceRepository;

    private final BalanceMapper balanceMapper;

    @Autowired
    public SqlBalanceRepositoryService(
            BalanceRepository balanceRepository,
            BalanceMapper balanceMapper
    ) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
    }

    public List<Balance> getBalancesForUserIdAndAccountId(Long userId, Long accountId) {
        return balanceMapper.to(
                balanceRepository.findAllByAccountIdAndAccountUserId(accountId, userId)
        );
    }

    public Balance getBalanceForAccountIdAndStatus(Long accountId, BalanceStatus status) {
        return balanceMapper.to(
                balanceRepository.getByAccountIdAndStatus(accountId, status)
        );
    }

    public Balance save(Balance balance) {
        BalanceDbo dbo = balanceRepository.save(
                balanceMapper.from(balance)
        );
        return balanceMapper.to(dbo);
    }

}
