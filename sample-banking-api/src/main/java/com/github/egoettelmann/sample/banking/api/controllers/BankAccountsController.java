package com.github.egoettelmann.sample.banking.api.controllers;

import com.github.egoettelmann.sample.banking.api.core.BankAccountsService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import org.springdoc.core.converters.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bank-accounts")
public class BankAccountsController {

    private final BankAccountsService bankAccountsService;

    @Autowired
    public BankAccountsController(BankAccountsService bankAccountsService) {
        this.bankAccountsService = bankAccountsService;
    }

    @GetMapping
    @PageableAsQueryParam
    public Page<BankAccount> findAllBankAccounts(Pageable pageable) {
        return bankAccountsService.getBankAccountsForUser(AppUser.current(), pageable);
    }

}
