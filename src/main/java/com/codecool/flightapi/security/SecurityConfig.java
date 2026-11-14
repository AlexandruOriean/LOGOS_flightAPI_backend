package com.codecool.flightapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final ProblemDetailAuthenticationEntryPoint unauthorizedHandler;
    private final ProblemDetailAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public SecurityConfig(
            ProblemDetailAuthenticationEntryPoint unauthorizedHandler,
            ProblemDetailAccessDeniedHandler accessDeniedHandler,
            JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Built by Spring from the {@code UserDetailsService} and {@link PasswordEncoder} beans.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * The application's filter chain. Authorization is deny-by-default: every request must match one
     * of the rules below, otherwise it is rejected. Rules are evaluated top to bottom.
     *
     * <ul>
     *   <li>Public: login/registration, error page, health/info probes, and the flight search and
     *       flight details the frontend shows before login.
     *   <li>Any signed-in user: every other {@code GET /api/v1/**}.
     *   <li>ADMIN: the full flight list and every write ({@code POST/PUT/DELETE /api/v1/**}).
     *   <li>Everything else: denied.
     * </ul>
     *
     * <p>Dev-only tooling (H2 console, Swagger UI) has its own chain in {@link DevToolsSecurityConfig}.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                // stateless bearer-token API: no session, so no CSRF token to protect
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        ex -> ex.authenticationEntryPoint(unauthorizedHandler).accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        // --- public ---
                        .requestMatchers(HttpMethod.POST, "/users/register", "/users/login")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/logout")
                        .permitAll()
                        .requestMatchers("/error")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/health/**", "/actuator/info")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/flights/{id}", "/api/v1/flights/{from}/{to}/{date}")
                        .permitAll()
                        // --- admin-only reads ---
                        .requestMatchers(HttpMethod.GET, "/api/v1/flights")
                        .hasRole("ADMIN")
                        // --- any signed-in user ---
                        .requestMatchers(HttpMethod.GET, "/api/v1/**")
                        .authenticated()
                        // --- writes ---
                        .requestMatchers(HttpMethod.POST, "/api/v1/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/**")
                        .hasRole("ADMIN")
                        // --- deny by default ---
                        .anyRequest()
                        .denyAll())
                // Bearer tokens are validated by the JwtDecoder from JwtConfig
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                                .authenticationEntryPoint(unauthorizedHandler)
                                .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }
}
