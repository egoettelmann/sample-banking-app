package com.github.egoettelmann.sample.auth.api.components.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CLAIM")
class ClaimDbo {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "VALUE")
    private String value;

}
