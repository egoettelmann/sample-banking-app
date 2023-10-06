package com.github.egoettelmann.sample.banking.api.components.accounts;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Data
@FieldNameConstants
@Entity
@Table(name = "bank_account")
class BankAccountDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "name")
    private String name;

    @Column(name = "currency")
    private String currency;

    @Column(name = "owner")
    private String owner;

}
