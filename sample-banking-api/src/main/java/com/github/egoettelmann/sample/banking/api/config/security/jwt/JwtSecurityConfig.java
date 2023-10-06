package com.github.egoettelmann.sample.banking.api.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Allows to bypass security by defining a user through a header.
 * To be used for development purpose only.
 */
@Slf4j
@Configuration
@Profile("!no-auth")
public class JwtSecurityConfig {

    /**
     * The key to encrypt/decrypt JWT tokens
     */
    private static final String SECRET = "SecretJWTKey";

    /**
     * The claim name for authorities.
     */
    public static final String AUTHORITIES_CLAIM = "https://sample.auth.api/authorities";

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
                String username;
                Set<AppAuthority> authorities;
                try {
                    DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                            .build()
                            .verify(token);
                    username = jwt.getSubject();
                    final String authoritiesClaim = jwt.getClaim(AUTHORITIES_CLAIM).asString();
                    authorities = mapToAppAuthorities(StringUtils.split(authoritiesClaim, ","));
                } catch (Exception e) {
                    log.error("Could not verify token: {}", token, e);
                    chain.doFilter(request, response);
                    return;
                }
                if (username == null) {
                    chain.doFilter(request, response);
                    return;
                }

                // Building the user details
                AppUser user = new AppUser(
                        username,
                        authorities
                );
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());

                // Defining the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
        };
    }

    private Set<AppAuthority> mapToAppAuthorities(String[] authorities) {
        final Set<AppAuthority> appAuthorities = new HashSet<>();
        final List<String> values = Arrays.asList(authorities);

        if (values.contains("CUSTOMER")) {
            appAuthorities.add(AppAuthority.ACCOUNTS_VIEW);
            appAuthorities.add(AppAuthority.BALANCES_VIEW);
            appAuthorities.add(AppAuthority.PAYMENTS_VIEW);
            appAuthorities.add(AppAuthority.PAYMENTS_CREATE);
        }

        if (values.contains("ADMIN")) {
            appAuthorities.add(AppAuthority.ACCOUNTS_VIEW_ALL);
        }

        return appAuthorities;
    }

}
