package com.github.egoettelmann.sample.banking.api.config.security;

import com.github.egoettelmann.sample.banking.api.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.servlet.Filter;
import java.util.Arrays;

@Slf4j
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Access control
        http.authorizeRequests()
                .antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated();

        // Adding auth filters and disabling session
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(authorizationFilter, BasicAuthenticationFilter.class);

        // Custom exception handling
        http.exceptionHandling()
                .authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport);

        // Enabling CORS
        http.cors();
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
