package com.github.egoettelmann.sample.auth.api.components.users;

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
@Table(name = "CLAIM")
class ClaimDbo {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "VALUE")
    private String value;

    @Version
    @Column(name = "VERSION")
    private Long version;

}
