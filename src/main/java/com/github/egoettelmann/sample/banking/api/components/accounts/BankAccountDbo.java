package com.github.egoettelmann.sample.banking.api.components.accounts;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
class BankAccountDbo {

    @Id
    private Long id;
    private String accountNumber;
    private String accountName;

    private Long userId;

}
