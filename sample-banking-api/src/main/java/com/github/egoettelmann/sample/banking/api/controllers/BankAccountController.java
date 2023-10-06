package com.github.egoettelmann.sample.banking.api.controllers;

import com.github.egoettelmann.sample.banking.api.core.BankAccountService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import com.github.egoettelmann.sample.banking.api.core.exceptions.DataNotFoundException;
import com.github.egoettelmann.sample.banking.api.core.requests.BankAccountFilter;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(
            BankAccountService bankAccountService
    ) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    @PageableAsQueryParam
    public Page<BankAccount> findBankAccounts(@ModelAttribute BankAccountFilter filter, Pageable pageable) {
        return bankAccountService.searchAccounts(AppUser.current(), filter, pageable);
    }

    @GetMapping("/{accountNumber}")
    public BankAccount getBankAccount(@PathVariable("accountNumber") String accountNumber) {
        return bankAccountService.getAccount(AppUser.current(), accountNumber)
                .orElseThrow(() -> new DataNotFoundException("No bank account found for number " + accountNumber));
    }

}
