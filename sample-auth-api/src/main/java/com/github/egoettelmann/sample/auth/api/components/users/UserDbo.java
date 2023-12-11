package com.github.egoettelmann.sample.auth.api.components.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "USER")
class UserDbo {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ADDRESS")
    private String address;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private Set<ClaimDbo> claims;
}
