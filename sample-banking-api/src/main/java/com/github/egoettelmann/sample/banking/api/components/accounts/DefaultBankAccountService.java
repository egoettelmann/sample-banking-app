package com.github.egoettelmann.sample.banking.api.components.accounts;

import com.github.egoettelmann.sample.banking.api.core.BankAccountService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class DefaultBankAccountService implements BankAccountService {

    private final SqlBankAccountRepositoryService sqlBankAccountRepositoryService;

    @Autowired
    public DefaultBankAccountService(SqlBankAccountRepositoryService sqlBankAccountRepositoryService) {
        this.sqlBankAccountRepositoryService = sqlBankAccountRepositoryService;
    }

    @Override
    public Page<BankAccount> getBankAccountsForUser(AppUser user, Pageable pageable) {
        return sqlBankAccountRepositoryService.getAllForUserId(user.getId(), pageable);
    }

    @Override
    public Optional<BankAccount> getBankAccountForUserById(AppUser user, Long bankAccountId) {
        return Optional.ofNullable(
                sqlBankAccountRepositoryService.getOneForUserId(bankAccountId, user.getId())
        );
    }

    @Override
    public Optional<BankAccount> getBankAccountByAccountNumber(String accountNumber) {
        return Optional.ofNullable(
                sqlBankAccountRepositoryService.getOneByAccountNumber(accountNumber)
        );
    }
}
