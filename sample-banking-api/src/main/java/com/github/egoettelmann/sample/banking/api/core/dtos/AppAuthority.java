package com.github.egoettelmann.sample.banking.api.core.dtos;

import org.springframework.security.core.GrantedAuthority;

public enum AppAuthority implements GrantedAuthority {
    /**
     * Accounts related authorities
     */
    ACCOUNTS_VIEW,
    ACCOUNTS_VIEW_ALL,

    /**
     * Balances related authorities
     */
    BALANCES_VIEW,

    /**
     * Payments related authorities
     */
    PAYMENTS_VIEW,
    PAYMENTS_CREATE,
    ;

    @Override
    public String getAuthority() {
        return name();
    }
}
