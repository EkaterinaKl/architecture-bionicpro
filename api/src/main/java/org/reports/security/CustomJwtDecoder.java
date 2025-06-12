package org.reports.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

public class CustomJwtDecoder implements JwtDecoder {

    private final NimbusJwtDecoder delegate;

    public CustomJwtDecoder(String jwkSetUri) {
        this.delegate = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        Jwt jwt = delegate.decode(token);
        // Игнорируем валидацию iss
        return jwt;
    }
}