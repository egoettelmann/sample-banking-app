package com.github.egoettelmann.sample.auth.api.core.dtos;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

public class AppUserDetails extends User {

    public AppUserDetails(String username, String password, Set<String> claims) {
        super(username, password, claims.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
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
