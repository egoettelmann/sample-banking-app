package com.github.egoettelmann.sample.banking.api.components.accounts;

import com.github.egoettelmann.sample.banking.api.core.BankAccountService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import com.github.egoettelmann.sample.banking.api.core.requests.BankAccountFilter;
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
    public Page<BankAccount> searchAccounts(AppUser user, BankAccountFilter filter, Pageable pageable) {
        // TODO: check which user is currently provided:
        //  - admin: no filter
        //  - user: add filter
        filter.setOwnerId(user.getId());
        return sqlBankAccountRepositoryService.findAll(filter, pageable);
    }

    @Override
    public Optional<BankAccount> getAccount(AppUser user, String accountNumber) {
        // TODO: check which user is currently provided:
        //  - admin: no filter
        //  - user: add filter
        final BankAccountFilter filter = BankAccountFilter.builder()
                .ownerId(user.getId())
                .accountNumber(accountNumber)
                .build();
        return sqlBankAccountRepositoryService.findOne(filter);
    }

}
