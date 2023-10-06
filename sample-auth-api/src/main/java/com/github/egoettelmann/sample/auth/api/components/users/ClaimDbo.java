package com.github.egoettelmann.sample.auth.api.components.users;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "claim")
class ClaimDbo {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

}
