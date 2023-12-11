package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.BankAccountService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import com.github.egoettelmann.sample.banking.api.core.exceptions.DataNotFoundException;
import com.github.egoettelmann.sample.banking.api.core.requests.BalanceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
class DefaultBalanceService implements BalanceService {

    private final BankAccountService bankAccountService;

    private final SqlBalanceRepositoryService sqlBalanceRepositoryService;

    @Autowired
    public DefaultBalanceService(
            BankAccountService bankAccountService,
            SqlBalanceRepositoryService sqlBalanceRepositoryService
    ) {
        this.bankAccountService = bankAccountService;
        this.sqlBalanceRepositoryService = sqlBalanceRepositoryService;
    }

    @Override
    public Optional<Balance> getCurrentBalance(AppUser user, String accountNumber) {
        // Checking user allowed to retrieve balance of account
        final Optional<BankAccount> bankAccount = this.bankAccountService.getAccount(user, accountNumber);
        if (bankAccount.isEmpty()) {
            return Optional.empty();
        }

        final BalanceFilter filter = BalanceFilter.builder()
                .accountNumber(accountNumber)
                .build();
        return sqlBalanceRepositoryService.findOne(filter);
    }

    @Override
    public void registerTransaction(AppUser user, String originAccountNumber, String beneficiaryAccountNumber, BigDecimal amount) {
        // Checking user allowed to register transaction on account
        final Optional<BankAccount> originBankAccount = this.bankAccountService.getAccount(user, originAccountNumber);
        if (originBankAccount.isEmpty()) {
            throw new DataNotFoundException("No bank account found with number " + originAccountNumber);
        }

        // Removing from origin account
        final Balance originBalance = getProvisionalBalance(originAccountNumber);
        originBalance.setValue(originBalance.getValue().subtract(amount));

        // Checking beneficiary account if it exists
        final Optional<BankAccount> beneficiaryAccount = this.bankAccountService.getAccount(AppUser.technical(), beneficiaryAccountNumber);
        if (beneficiaryAccount.isEmpty()) {
            sqlBalanceRepositoryService.save(originBalance);
            return;
        }

        // Beneficiary balance exists: saving both
        final Balance beneficiaryBalance = getProvisionalBalance(beneficiaryAccountNumber);
        beneficiaryBalance.setValue(beneficiaryBalance.getValue().add(amount));
        sqlBalanceRepositoryService.saveBalancesInTransaction(originBalance, beneficiaryBalance);
    }

    private Balance getProvisionalBalance(String accountNumber) {
        final BalanceFilter filter = BalanceFilter.builder()
                .accountNumber(accountNumber)
                .build();
        final Optional<Balance> balance = sqlBalanceRepositoryService.findOne(filter);
        if (balance.isEmpty()) {
            throw new DataNotFoundException("No balance found for account " + accountNumber);
        }

        // If PROVISIONAL balance already exists for date, returning it
        if (BalanceStatus.PROVISIONAL.equals(balance.get().getStatus())) {
            return balance.get();
        }

        // Retrieving the latest VALIDATED balance and creating PROVISIONAL out of it
        final Balance provisionalBalance = new Balance();
        provisionalBalance.setAccountNumber(accountNumber);
        provisionalBalance.setValueDate(LocalDate.now());
        provisionalBalance.setStatus(BalanceStatus.PROVISIONAL);
        provisionalBalance.setValue(balance.get().getValue());
        return provisionalBalance;
    }

}
