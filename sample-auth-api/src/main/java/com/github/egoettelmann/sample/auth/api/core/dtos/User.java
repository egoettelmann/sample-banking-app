package com.github.egoettelmann.sample.auth.api.core.dtos;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String username;
    private String password;
    private String address;

}
