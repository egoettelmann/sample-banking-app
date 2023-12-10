package com.github.egoettelmann.sample.banking.api.components.validation;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "forbidden_iban")
class ForbiddenIbanDbo {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "iban")
    private String iban;

}
