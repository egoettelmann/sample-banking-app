package com.github.egoettelmann.sample.banking.api.components.validation;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "FORBIDDEN_IBAN")
class ForbiddenIbanDbo {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "IBAN")
    private String iban;

}
