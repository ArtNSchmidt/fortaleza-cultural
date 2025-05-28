package com.fortaleza_cultural.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuration class for Spring Security settings.
 * Enables WebFlux security and defines beans for password encoding and
 * the security filter chain.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Provides a PasswordEncoder bean for hashing passwords.
     * Uses BCrypt algorithm.
     *
     * @return PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for HTTP requests.
     * - Disables CSRF (common for stateless APIs).
     * - Disables HTTP Basic and Form Login (as we'll handle auth differently, likely JWT later).
     * - Allows public access to the user registration endpoint.
     * - Requires authentication for other user-related endpoints for now.
     * - For now, permit all other unspecified paths to avoid blocking upcoming features. This will be tightened.
     *
     * @param http ServerHttpSecurity to configure.
     * @return SecurityWebFilterChain.
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // Disable HTTP Basic
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // Disable Form Login
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.POST, "/usuarios/registrar").permitAll() // Allow registration
                .pathMatchers("/usuarios/**").authenticated() // Secure other /usuarios/** paths
                // .anyExchange().authenticated() // Example: Secure all other unspecified paths
                .anyExchange().permitAll() // For now, permit all other unspecified paths to avoid blocking upcoming features. This will be tightened.
            );
        return http.build();
    }
}
