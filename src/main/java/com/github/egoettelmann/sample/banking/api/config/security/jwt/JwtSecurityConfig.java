package com.github.egoettelmann.sample.banking.api.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Allows to bypass security by defining a user through a header.
 * To be used for development purpose only.
 */
@Configuration
@Profile("!no-auth")
public class JwtSecurityConfig {

    /**
     * The key to encrypt/decrypt JWT tokens
     */
    private static final String SECRET = "SecretJWTKey";

    /**
     * The 'JWT' authorization filter.
     *
     * @return the filter
     */
    @Bean("authorizationFilter")
    public Filter authorizationFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
                // Extracting the token from the headers
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null) {
                    chain.doFilter(request, response);
                    return;
                }
                String token = authHeader.replace("Bearer ", "");

                // Extracting username and userId from token
                DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token);
                String username = jwt.getSubject();
                Claim userIdClaim = jwt.getClaim("userId");
                if (username == null || userIdClaim.isNull()) {
                    chain.doFilter(request, response);
                    return;
                }

                // Building the user details
                AppUser user = new AppUser();
                user.setId(userIdClaim.asLong());
                user.setUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", Collections.emptyList());

                // Defining the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
        };
    }

}
