package com.github.egoettelmann.sample.banking.api.components.validation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "FORBIDDEN_IBAN")
class ForbiddenIbanDbo {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "IBAN")
    private String iban;

    @Version
    @Column(name = "VERSION")
    private Long version;

}
