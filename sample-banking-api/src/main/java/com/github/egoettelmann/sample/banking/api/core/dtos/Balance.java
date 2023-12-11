package com.github.egoettelmann.sample.banking.api.core.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Balance {

    private String accountNumber;
    private BigDecimal value;
    private LocalDate valueDate;
    private BalanceStatus status;
    private Long version;

}
