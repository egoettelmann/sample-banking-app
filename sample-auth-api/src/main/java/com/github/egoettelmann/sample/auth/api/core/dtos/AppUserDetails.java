package com.github.egoettelmann.sample.auth.api.core.dtos;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

    public static AppUserDetails current() {
        if (SecurityContextHolder.getContext() == null
                || SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null
        ) {
            throw new UsernameNotFoundException("No user principal found");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof AppUserDetails)) {
            throw new UsernameNotFoundException("Principal instance does not match");
        }
        return (AppUserDetails) principal;
    }

}
