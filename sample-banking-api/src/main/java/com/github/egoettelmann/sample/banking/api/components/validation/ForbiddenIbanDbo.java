package com.github.egoettelmann.sample.banking.api.components.validation;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
