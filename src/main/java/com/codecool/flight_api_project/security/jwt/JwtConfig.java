package com.codecool.flight_api_project.security.jwt;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * HS512-signed JWTs issued and validated by Spring Security's Nimbus-based JOSE support.
 */
@Configuration
public class JwtConfig {

    static final MacAlgorithm ALGORITHM = MacAlgorithm.HS512;
    static final String ROLES_CLAIM = "roles";

    /** HS512 requires a key of at least 512 bits. */
    private static final int MIN_KEY_BYTES = 64;

    /**
     * @param base64Secret Base64-encoded HMAC key, e.g. generated with {@code openssl rand -base64 64}
     */
    @Bean
    SecretKey jwtSigningKey(@Value("${app.jwt.secret}") String base64Secret) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
        if (keyBytes.length < MIN_KEY_BYTES) {
            throw new IllegalStateException("app.jwt.secret must decode to at least " + MIN_KEY_BYTES + " bytes for "
                    + ALGORITHM.getName() + ", got " + keyBytes.length);
        }
        return new SecretKeySpec(keyBytes, "HmacSHA512");
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey jwtSigningKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSigningKey));
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey jwtSigningKey) {
        return NimbusJwtDecoder.withSecretKey(jwtSigningKey)
                .macAlgorithm(ALGORITHM)
                .build();
    }

    /**
     * Maps the {@code roles} claim (already {@code ROLE_*}-prefixed) straight to granted authorities,
     * so {@code hasRole("ADMIN")} keeps working.
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName(ROLES_CLAIM);
        authorities.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }
}
