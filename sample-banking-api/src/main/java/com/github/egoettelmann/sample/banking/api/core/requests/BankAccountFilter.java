package com.github.egoettelmann.sample.banking.api.core.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountFilter {

    private String partialName;
    private String accountNumber;
    private Long ownerId;

}
