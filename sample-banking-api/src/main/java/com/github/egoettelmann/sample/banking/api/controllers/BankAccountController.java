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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasAuthority(T(com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority).ACCOUNTS_VIEW.name())")
    public Page<BankAccount> findBankAccounts(@ModelAttribute BankAccountFilter filter, Pageable pageable, @AuthenticationPrincipal AppUser appUser) {
        return bankAccountService.searchAccounts(appUser, filter, pageable);
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasAuthority(T(com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority).ACCOUNTS_VIEW.name())")
    public BankAccount getBankAccount(@PathVariable("accountNumber") String accountNumber, @AuthenticationPrincipal AppUser appUser) {
        return bankAccountService.getAccount(appUser, accountNumber)
                .orElseThrow(() -> new DataNotFoundException("No bank account found for number " + accountNumber));
    }

}
