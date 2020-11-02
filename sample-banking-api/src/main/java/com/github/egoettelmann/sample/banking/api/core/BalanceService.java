package com.github.egoettelmann.sample.banking.api.core;

import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceService {

    List<Balance> getBalancesForUserAndAccount(AppUser user, Long accountId);

    Balance getEndOfDayBalanceForAccount(Long accountId);

    void addAmountToBalance(BigDecimal amount, BankAccount account);

    void subtractAmountFromBalance(BigDecimal amount, BankAccount account);

}
