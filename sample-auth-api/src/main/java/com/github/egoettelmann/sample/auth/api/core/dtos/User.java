package com.github.egoettelmann.sample.auth.api.core.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User {

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String address;

}
