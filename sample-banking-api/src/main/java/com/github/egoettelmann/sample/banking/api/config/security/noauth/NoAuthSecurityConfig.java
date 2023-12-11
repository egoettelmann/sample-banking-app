package com.github.egoettelmann.sample.banking.api.config.security.noauth;

import com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * Allows to bypass security by defining a user through a header.
 * To be used for development purpose only.
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "banking-api.auth", havingValue = "none")
public class NoAuthSecurityConfig {

    /**
     * The 'no-auth' authorization filter.
     *
     * @return the filter
     */
    @Bean("authorizationFilter")
    public Filter authorizationFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
                // Extracting username from headers
                String username = "user1@test.com";
                String headerValue = request.getHeader("username");
                if (headerValue != null) {
                    username = headerValue;
                }

                // Building the user details
                AppUser user = new AppUser(
                        username,
                        Set.of(
                                AppAuthority.ACCOUNTS_VIEW,
                                AppAuthority.BALANCES_VIEW,
                                AppAuthority.PAYMENTS_VIEW,
                                AppAuthority.PAYMENTS_CREATE
                        )
                );
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());

                // Defining the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
        };
    }

}
