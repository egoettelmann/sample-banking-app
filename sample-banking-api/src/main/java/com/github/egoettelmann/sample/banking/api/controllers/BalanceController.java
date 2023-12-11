package com.github.egoettelmann.sample.banking.api.controllers;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.exceptions.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balances")
public class BalanceController {

    private final BalanceService balanceService;

    @Autowired
    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/{accountNumber}/current")
    @PreAuthorize("hasAuthority(T(com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority).BALANCES_VIEW.name())")
    public Balance getCurrentBalance(@PathVariable("accountNumber") String accountNumber, @AuthenticationPrincipal AppUser appUser) {
        return balanceService.getCurrentBalance(appUser, accountNumber)
                .orElseThrow(() -> new DataNotFoundException("No current balance found for account number " + accountNumber));
    }

}
