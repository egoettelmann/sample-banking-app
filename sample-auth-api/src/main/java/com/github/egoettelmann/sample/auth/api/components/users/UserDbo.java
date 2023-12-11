package com.github.egoettelmann.sample.auth.api.components.users;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Set;

@Data
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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private Set<ClaimDbo> claims;
}
