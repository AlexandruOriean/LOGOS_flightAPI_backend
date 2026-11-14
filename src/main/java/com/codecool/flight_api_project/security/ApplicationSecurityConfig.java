package com.codecool.flight_api_project.security;

import com.codecool.flight_api_project.security.jwt.AuthEntryPointJwt;
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
public class ApplicationSecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public ApplicationSecurityConfig(
            AuthEntryPointJwt unauthorizedHandler, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.unauthorizedHandler = unauthorizedHandler;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // NOTE: rules are carried over unchanged from the pre-Boot-4 config.
        // Deny-by-default and the shadowed flight rules are tracked in REFACTORING_ROADMAP.md (task 7).
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/users/**")
                        .permitAll()
                        .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/flights/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/flights")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/flights")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/flights/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/flights/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/cities")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/cities")
                        .authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/cities/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/cities/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/airports")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/airports/{id}")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/airports/iata/{iata_code}")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/airports")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/airports/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/airports/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/airplanes")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/airplanes")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/airplanes/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/airplanes/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/airlines")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/airlines")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/airlines/{id}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/airlines/{id}")
                        .hasRole("ADMIN")
                        .anyRequest()
                        .permitAll())
                // Bearer tokens are validated by the JwtDecoder from JwtConfig
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                                .authenticationEntryPoint(unauthorizedHandler));

        return http.build();
    }
}
