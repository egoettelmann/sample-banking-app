package com.github.egoettelmann.sample.auth.api.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.egoettelmann.sample.auth.api.config.AppProperties;
import com.github.egoettelmann.sample.auth.api.core.dtos.TokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
     * The user details service
     */
    private final UserDetailsService userDetailsService;

    /**
     * The JWT token generator
     */
    private final JwtTokenGenerator jwtTokenGenerator;

    /**
     * The security errors handler
     */
    private final SecurityProblemSupport securityProblemSupport;

    /**
     * The object mapper
     */
    private final ObjectMapper objectMapper;

    /**
     * The app properties
     */
    private final AppProperties appProperties;

    /**
     * Instantiates the Rest Security Config.
     *
     * @param userDetailsService     the user details service
     * @param jwtTokenGenerator      the JWT token generation
     * @param securityProblemSupport the security errors handler
     * @param objectMapper           the object mapper
     * @param appProperties          the application properties
     */
    @Autowired
    public SecurityConfig(
            final UserDetailsService userDetailsService,
            final JwtTokenGenerator jwtTokenGenerator,
            final SecurityProblemSupport securityProblemSupport,
            final ObjectMapper objectMapper,
            final AppProperties appProperties
    ) {
        super();
        this.userDetailsService = userDetailsService;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.securityProblemSupport = securityProblemSupport;
        this.objectMapper = objectMapper;
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

        // Disabling CSRF: useless with JWT authentication
        http.csrf().disable();
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
                try {
                    TokenHolder token = jwtTokenGenerator.buildToken(authentication);
                    String jwtResponse = objectMapper.writeValueAsString(token);
                    response.setContentType("application/json");
                    response.getWriter().write(jwtResponse);
                    clearAuthenticationAttributes(request);
                } catch (Exception e) {
                    log.error("Problem generating token: ", e);
                    securityProblemSupport.commence(request, response, new AuthenticationServiceException("Could not generate token", e));
                }
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

    /**
     * Configures the authentication manager by providing the user details service and the password encoder.
     *
     * @param authenticationManagerBuilder the authentication manager builder
     * @throws Exception thrown if configuration fails
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * The password encoder: BCrypt.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
