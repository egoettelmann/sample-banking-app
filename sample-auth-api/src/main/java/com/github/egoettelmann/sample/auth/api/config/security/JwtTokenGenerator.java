package com.github.egoettelmann.sample.auth.api.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.egoettelmann.sample.auth.api.core.dtos.AppUserDetails;
import com.github.egoettelmann.sample.auth.api.core.dtos.TokenHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenGenerator {

    /**
     * The key to encrypt/decrypt JWT tokens
     */
    private static final String SECRET = "SecretJWTKey";

    /**
     * The validity of the token: 1 hour
     */
    public static final long VALIDITY_IN_MS = 3_600_000;

    /**
     * Builds a JWT token from an authentication object.
     *
     * @param authentication the authentication object
     * @return the token holder
     */
    public TokenHolder buildToken(Authentication authentication) {
        AppUserDetails principal = (AppUserDetails) authentication.getPrincipal();
        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + VALIDITY_IN_MS))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withSubject(principal.getUsername())
                .withClaim("userId", principal.getUserId())
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        return new TokenHolder(token);
    }

}
