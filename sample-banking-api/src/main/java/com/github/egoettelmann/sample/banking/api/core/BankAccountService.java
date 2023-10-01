package com.github.egoettelmann.sample.banking.api.core;

import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import com.github.egoettelmann.sample.banking.api.core.requests.BankAccountFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BankAccountService {

    Page<BankAccount> searchAccounts(AppUser user, BankAccountFilter filter, Pageable pageable);

    Optional<BankAccount> getAccount(AppUser user, String accountNumber);

}
