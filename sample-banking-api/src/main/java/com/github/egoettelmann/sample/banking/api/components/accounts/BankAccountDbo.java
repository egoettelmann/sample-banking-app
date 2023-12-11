package com.github.egoettelmann.sample.banking.api.components.accounts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
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

    @Version
    @Column(name = "VERSION")
    private Long version;

}
