package com.github.egoettelmann.sample.banking.api.core.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull
    private BigDecimal amount;
    private String currency;

    @NotNull
    private Long giverAccountId;

    @NotNull
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String communication;

}
