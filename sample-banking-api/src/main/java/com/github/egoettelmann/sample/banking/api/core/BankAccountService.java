package com.github.egoettelmann.sample.banking.api.core;

import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BankAccountService {

    Page<BankAccount> getBankAccountsForUser(AppUser user, Pageable pageable);

    Optional<BankAccount> getBankAccountForUserById(AppUser user, Long bankAccountId);

    Optional<BankAccount> getBankAccountByAccountNumber(String accountNumber);

}
