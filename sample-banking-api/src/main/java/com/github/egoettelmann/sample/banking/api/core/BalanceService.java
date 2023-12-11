package com.github.egoettelmann.sample.banking.api.core;

import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;

import java.math.BigDecimal;
import java.util.Optional;

public interface BalanceService {

    Optional<Balance> getCurrentBalance(AppUser user, String accountNumber);

    void registerTransaction(AppUser user, String originAccountNumber, String beneficiaryAccountNumber, BigDecimal amount);

}
