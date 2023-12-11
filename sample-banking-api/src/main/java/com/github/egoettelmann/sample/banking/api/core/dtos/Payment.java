package com.github.egoettelmann.sample.banking.api.core.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class Payment {

    private String reference;
    private BigDecimal amount;
    private String currency;
    private String originAccountNumber;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String communication;
    private ZonedDateTime creationDate;
    private PaymentStatus status;
    private Long version;

}
