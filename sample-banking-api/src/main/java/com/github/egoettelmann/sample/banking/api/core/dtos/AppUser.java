package com.github.egoettelmann.sample.banking.api.core.dtos;

import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
public class AppUser {

    private final String username;

    private final Set<AppAuthority> authorities;

    public static AppUser current() {
        if (SecurityContextHolder.getContext() == null
                || SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null
        ) {
            throw new UsernameNotFoundException("No user principal found");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof AppUser)) {
            throw new UsernameNotFoundException("Principal instance does not match");
        }
        return (AppUser) principal;
    }

    public static AppUser technical() {
        return new AppUser(
                "TECHNICAL",
                new HashSet<>(Arrays.asList(AppAuthority.values()))
        );
    }
}
