package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
class DefaultBalanceService implements BalanceService {

    private final SqlBalanceRepositoryService sqlBalanceRepositoryService;

    @Autowired
    public DefaultBalanceService(SqlBalanceRepositoryService sqlBalanceRepositoryService) {
        this.sqlBalanceRepositoryService = sqlBalanceRepositoryService;
    }

    @Override
    public List<Balance> getBalancesForUserAndAccount(AppUser user, Long accountId) {
        return sqlBalanceRepositoryService.getBalancesForUserIdAndAccountId(user.getId(), accountId);
    }

    @Override
    public Balance getEndOfDayBalanceForAccount(Long accountId) {
        // Get the end of day balance if it exists
        Balance endOfDay = sqlBalanceRepositoryService.getBalanceForAccountIdAndStatus(accountId, BalanceStatus.END_OF_DAY);
        if (endOfDay != null) {
            return endOfDay;
        }

        // Otherwise we build a new one from the available balance
        Balance availableBalance = sqlBalanceRepositoryService.getBalanceForAccountIdAndStatus(accountId, BalanceStatus.AVAILABLE);
        return buildEndOfDayBalance(availableBalance);
    }

    @Override
    public void addAmountToBalance(BigDecimal amount, BankAccount account) {
        // Retrieving the 'end_of_day' balance of the giver and updating amount
        Balance balance = getEndOfDayBalanceForAccount(account.getId());
        BigDecimal newGiverBalance = balance.getAmount().add(amount);
        balance.setAmount(newGiverBalance);
        sqlBalanceRepositoryService.save(balance);
    }

    @Override
    public void subtractAmountFromBalance(BigDecimal amount, BankAccount account) {
        // Retrieving the 'end_of_day' balance of the giver and updating amount
        Balance balance = getEndOfDayBalanceForAccount(account.getId());
        BigDecimal newGiverBalance = balance.getAmount().subtract(amount);
        balance.setAmount(newGiverBalance);
        sqlBalanceRepositoryService.save(balance);
    }

    private Balance buildEndOfDayBalance(Balance availableBalance) {
        Balance balance = new Balance();
        balance.setAmount(availableBalance.getAmount());
        balance.setCurrency(availableBalance.getCurrency());
        balance.setAccount(availableBalance.getAccount());
        balance.setStatus(BalanceStatus.END_OF_DAY);
        return balance;
    }

}
