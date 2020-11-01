package com.github.egoettelmann.sample.banking.api.core.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    private BigDecimal amount;
    private String currency;

    private Long giverAccountId;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String communication;

}
