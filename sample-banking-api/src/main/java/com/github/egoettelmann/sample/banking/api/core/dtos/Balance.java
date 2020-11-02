package com.github.egoettelmann.sample.banking.api.core.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Balance {

    private Long id;
    private BigDecimal amount;
    private String currency;

    private BankAccount account;

    private BalanceStatus status;
}
