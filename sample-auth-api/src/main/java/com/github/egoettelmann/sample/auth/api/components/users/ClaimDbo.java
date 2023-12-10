package com.github.egoettelmann.sample.auth.api.components.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

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
