package com.github.egoettelmann.sample.banking.api.config.security.noauth;

import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
@Profile("no-auth")
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
                long userId = 1L;
                String headerValue = request.getHeader("userId");
                if (headerValue != null) {
                    try {
                        userId = Long.parseLong(headerValue);
                    } catch (Exception e) {
                        log.error("Impossible to extract 'userId' from headers: ", e);
                        chain.doFilter(request, response);
                        return;
                    }
                }

                // Building the user details
                AppUser user = new AppUser();
                user.setId(userId);
                user.setUsername("no-auth");
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", Collections.emptyList());

                // Defining the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
        };
    }

}
