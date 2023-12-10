package com.github.egoettelmann.sample.auth.api.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.egoettelmann.sample.auth.api.config.AppProperties;
import com.github.egoettelmann.sample.auth.api.config.security.jwt.JwtTokenGenerator;
import com.github.egoettelmann.sample.auth.api.core.dtos.TokenHolder;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Arrays;

@Slf4j
@Configuration
public class SecurityConfig {

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
     * The authorization filter
     */
    private final Filter authorizationFilter;

    /**
     * Instantiates the Rest Security Config.
     *
     * @param jwtTokenGenerator      the JWT token generation
     * @param securityProblemSupport the security errors handler
     * @param objectMapper           the object mapper
     * @param appProperties          the application properties
     * @param authorizationFilter    the authorization filter
     */
    @Autowired
    public SecurityConfig(
            final JwtTokenGenerator jwtTokenGenerator,
            final SecurityProblemSupport securityProblemSupport,
            final ObjectMapper objectMapper,
            final AppProperties appProperties,
            @Qualifier("authorizationFilter") final Filter authorizationFilter
    ) {
        super();
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.securityProblemSupport = securityProblemSupport;
        this.objectMapper = objectMapper;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Access control
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
        );

        // Custom exception handling for returning RestError
        http.exceptionHandling(handler -> handler
                .authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport)
        );

        // Disabling session
        http.sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Adding auth filters
        http.addFilterBefore(authorizationFilter, BasicAuthenticationFilter.class);

        // Login
        http.formLogin(login -> login
                .loginProcessingUrl("/api/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureHandler(loginFailureHandler())
                .successHandler(loginSuccessHandler())
                .permitAll()
        );

        // Logout
        http.logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
        );

        // Enabling CORS
        http.cors(Customizer.withDefaults());

        // Disabling CSRF: useless with JWT authentication
        http.csrf(CsrfConfigurer::disable);

        // Building and returning filter chain
        return http.build();
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
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
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
     * The password encoder: BCrypt.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
