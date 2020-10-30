package com.github.egoettelmann.sample.banking.api.core.dtos;

import lombok.Data;

@Data
public class BankAccount {

    private Long id;
    private String accountNumber;
    private String accountName;

}
