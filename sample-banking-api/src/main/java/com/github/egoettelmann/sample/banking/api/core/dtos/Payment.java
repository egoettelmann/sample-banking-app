package com.github.egoettelmann.sample.banking.api.core.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class Payment {

    private Long id;
    private BigDecimal amount;
    private String currency;

    private BankAccount giverAccount;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String communication;

    private ZonedDateTime creationDate;

    private PaymentStatus status;
}
