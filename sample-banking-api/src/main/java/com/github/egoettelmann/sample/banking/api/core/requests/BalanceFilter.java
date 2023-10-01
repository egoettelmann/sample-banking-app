package com.github.egoettelmann.sample.banking.api.core.requests;

import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceFilter {

    private String accountNumber;
    private LocalDate valueDate;
    private BalanceStatus status;

}
