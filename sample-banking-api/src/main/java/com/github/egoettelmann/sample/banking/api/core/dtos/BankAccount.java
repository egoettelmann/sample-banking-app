package com.github.egoettelmann.sample.banking.api.core.dtos;

import lombok.Data;

@Data
public class BankAccount {

    private String number;
    private String name;
    private String currency;
    private String owner;
    private Long version;

}
