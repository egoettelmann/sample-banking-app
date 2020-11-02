package com.github.egoettelmann.sample.auth.api.core.dtos.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRequest {

    @NotNull
    private String password;
    private String address;

}
