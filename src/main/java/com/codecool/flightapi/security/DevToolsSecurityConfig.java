package com.codecool.flightapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Local development tooling: the H2 console and Swagger UI / OpenAPI docs. Only active in the
 * {@code dev} Spring profile; in production this chain does not exist, so those paths fall
 * through to the main chain and are denied.
 *
 * <p>Ordered before the main chain and limited to the tooling paths. The H2 console is rendered
 * in frames, so framing is allowed for these paths only ({@code SAMEORIGIN}); the API keeps the
 * default {@code X-Frame-Options: DENY}.
 */
@Configuration
@Profile("dev")
class DevToolsSecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain devToolsSecurityFilterChain(
            HttpSecurity http, @Value("${spring.h2.console.path:/h2-console}") String h2ConsolePath) throws Exception {
        http.securityMatcher(
                        h2ConsolePath,
                        h2ConsolePath + "/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // the H2 console posts forms without CSRF tokens
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }
}
