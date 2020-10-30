package com.github.egoettelmann.sample.banking.api.components.accounts;

import com.github.egoettelmann.sample.banking.api.core.BankAccountsService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class DefaultBankAccountsService implements BankAccountsService {

    private final SqlBankAccountRepositoryService sqlBankAccountRepositoryService;

    @Autowired
    public DefaultBankAccountsService(SqlBankAccountRepositoryService sqlBankAccountRepositoryService) {
        this.sqlBankAccountRepositoryService = sqlBankAccountRepositoryService;
    }

    @Override
    public Page<BankAccount> getBankAccountsForUser(AppUser user, Pageable pageable) {
        return sqlBankAccountRepositoryService.getBankAccountsForUserId(user.getId(), pageable);
    }
}
