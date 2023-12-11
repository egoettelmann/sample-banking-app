package com.github.egoettelmann.sample.banking.api.config.security;

import com.github.egoettelmann.sample.banking.api.config.AppProperties;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * The security errors handler
     */
    private final SecurityProblemSupport securityProblemSupport;

    /**
     * The app properties
     */
    private final AppProperties appProperties;

    /**
     * The authorization filter
     */
    private final Filter authorizationFilter;

    /**
     * Instantiates the Rest Security Config.
     *
     * @param securityProblemSupport the security errors handler
     * @param appProperties          the application properties
     * @param authorizationFilter    the authorization filter
     */
    @Autowired
    public SecurityConfig(
            final SecurityProblemSupport securityProblemSupport,
            final AppProperties appProperties,
            @Qualifier("authorizationFilter") final Filter authorizationFilter
    ) {
        super();
        this.securityProblemSupport = securityProblemSupport;
        this.appProperties = appProperties;
        this.authorizationFilter = authorizationFilter;
    }

    /**
     * Security configuration setup.
     *
     * @param http the http security configuration object
     * @throws Exception configuration exception
     */
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Access control
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
        );

        // Disabling session
        http.sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Adding auth filter
        http.addFilterBefore(authorizationFilter, BasicAuthenticationFilter.class);

        // Custom exception handling
        http.exceptionHandling(handler -> handler
                .authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport)
        );

        // Enabling CORS
        http.cors(Customizer.withDefaults());

        // Disabling CSRF: useless with JWT authentication
        http.csrf(CsrfConfigurer::disable);

        // Building and returning filter chain
        return http.build();
    }

    /**
     * The CORS configuration.
     *
     * @return the CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(appProperties.getAllowedOrigins());
        corsConfig.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
        corsConfig.setAllowCredentials(true);
        corsConfig.applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

}
