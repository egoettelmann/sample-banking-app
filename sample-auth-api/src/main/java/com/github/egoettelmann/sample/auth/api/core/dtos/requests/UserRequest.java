package com.github.egoettelmann.sample.auth.api.core.dtos.requests;

import com.github.egoettelmann.sample.auth.api.core.validation.ValidPassword;
import lombok.Data;

@Data
public class UserRequest {

    @ValidPassword
    private String password;
    private String address;

}
