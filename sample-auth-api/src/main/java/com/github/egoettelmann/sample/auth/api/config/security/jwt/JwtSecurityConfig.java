package com.github.egoettelmann.sample.auth.api.config.security.jwt;

import com.github.egoettelmann.sample.auth.api.core.dtos.AppUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Allows to bypass security by defining a user through a header.
 * To be used for development purpose only.
 */
@Slf4j
@Configuration
public class JwtSecurityConfig {

    private final JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    public JwtSecurityConfig(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

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

                // Extracting user details from token
                AppUserDetails userDetails = jwtTokenGenerator.decodeToken(token);
                if (userDetails == null) {
                    chain.doFilter(request, response);
                    return;
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList());

                // Defining the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
        };
    }

}
