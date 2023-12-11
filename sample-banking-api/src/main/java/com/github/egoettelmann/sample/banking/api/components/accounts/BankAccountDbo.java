package com.github.egoettelmann.sample.banking.api.components.accounts;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
@Entity
@Table(name = "BANK_ACCOUNT")
class BankAccountDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "OWNER")
    private String owner;

}
