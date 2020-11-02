package com.github.egoettelmann.sample.auth.api.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.egoettelmann.sample.auth.api.core.dtos.AppUserDetails;
import com.github.egoettelmann.sample.auth.api.core.dtos.TokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
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

    public AppUserDetails decodeToken(String token) {
        String username;
        Claim userIdClaim;
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token);
            username = jwt.getSubject();
            userIdClaim = jwt.getClaim("userId");
        } catch (Exception e) {
            log.error("Could not verify token: {}", token, e);
            return null;
        }
        if (username == null || userIdClaim.isNull()) {
            return null;
        }

        // Building the user details
        return new AppUserDetails(userIdClaim.asLong(), username, "");
    }
}
