package com.github.egoettelmann.sample.auth.api.config.security;

import com.github.egoettelmann.sample.auth.api.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
     * Instantiates the Rest Security Config.
     *
     * @param securityProblemSupport the security errors handler
     * @param appProperties          the application properties
     */
    @Autowired
    public SecurityConfig(
            final SecurityProblemSupport securityProblemSupport,
            final AppProperties appProperties
    ) {
        super();
        this.securityProblemSupport = securityProblemSupport;
        this.appProperties = appProperties;
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

        // Custom exception handling for returning RestError
        http.exceptionHandling()
                .authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport);

        // Login
        http.formLogin()
                .loginProcessingUrl("/api/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureHandler(loginFailureHandler())
                .successHandler(loginSuccessHandler())
                .permitAll();

        // Logout
        http.logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(logoutSuccessHandler());

        // Enabling CORS
        http.cors();
    }

    /**
     * The login failure handler.
     * Triggered when the authentication failed (like a bad credentials exception).
     *
     * @return the authentication failure handler
     */
    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            log.warn("Authentication failure '{}'", exception.getMessage());
            securityProblemSupport.commence(request, response, exception);
        };
    }

    /**
     * The login success handler.
     *
     * @return the authentication success handler
     */
    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                log.info("Login successful");
                clearAuthenticationAttributes(request);
            }
        };
    }

    /**
     * The logout success handler.
     *
     * @return the logout success handler
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new SimpleUrlLogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                log.info("Logout successful");
            }
        };
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
