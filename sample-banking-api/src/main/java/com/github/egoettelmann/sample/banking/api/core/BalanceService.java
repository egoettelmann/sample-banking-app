package com.github.egoettelmann.sample.banking.api.core;

import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;

import java.math.BigDecimal;

public interface BalanceService {

    Balance getEndOfDayBalanceForAccount(Long accountId);

    void addAmountToBalance(BigDecimal amount, BankAccount account);

    void subtractAmountFromBalance(BigDecimal amount, BankAccount account);

}
