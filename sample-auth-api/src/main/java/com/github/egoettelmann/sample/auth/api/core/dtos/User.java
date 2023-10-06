package com.github.egoettelmann.sample.auth.api.core.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Set;

@Data
public class User {

    private String username;
    @JsonIgnore
    private String password;
    private String address;
    private Set<String> claims;

}
