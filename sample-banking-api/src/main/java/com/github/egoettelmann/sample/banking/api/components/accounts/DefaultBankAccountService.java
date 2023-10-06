package com.github.egoettelmann.sample.banking.api.components.accounts;

import com.github.egoettelmann.sample.banking.api.core.BankAccountService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority;
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
        // Filtering by owner
        if (!user.getAuthorities().contains(AppAuthority.ACCOUNTS_VIEW_ALL)) {
            filter.setOwner(user.getUsername());
        }
        return sqlBankAccountRepositoryService.findAll(filter, pageable);
    }

    @Override
    public Optional<BankAccount> getAccount(AppUser user, String accountNumber) {
        final BankAccountFilter filter = BankAccountFilter.builder()
                .accountNumber(accountNumber)
                .build();
        // Filtering by owner
        if (!user.getAuthorities().contains(AppAuthority.ACCOUNTS_VIEW_ALL)) {
            filter.setOwner(user.getUsername());
        }
        return sqlBankAccountRepositoryService.findOne(filter);
    }

}
