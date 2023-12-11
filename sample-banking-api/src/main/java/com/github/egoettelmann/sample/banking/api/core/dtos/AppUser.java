package com.github.egoettelmann.sample.banking.api.core.dtos;

import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
public class AppUser {

    private final String username;

    private final Set<AppAuthority> authorities;

    public static AppUser technical() {
        return new AppUser(
                "TECHNICAL",
                new HashSet<>(Arrays.asList(AppAuthority.values()))
        );
    }
}
