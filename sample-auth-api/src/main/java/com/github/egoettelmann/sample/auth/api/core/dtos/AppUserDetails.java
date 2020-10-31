package com.github.egoettelmann.sample.auth.api.core.dtos;

import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class AppUserDetails extends User {

    private final Long userId;

    public AppUserDetails(Long userId, String username, String password) {
        super(username, password, Collections.emptyList());
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

}
