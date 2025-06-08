package reportservice.service;

import io.jsonwebtoken.*;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.security.Key;
import java.time.Instant;
import java.util.*;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    public static final String PROTHETIC_USER = "prothetic_user";
    public static final String REALM_ACCESS = "realm_access";
    public static final String ROLES = "roles";

    @Value("${KEYCLOAK_URL:http://localhost}")
    private String keycloakUrl;

    @Value("${KEYCLOAK_REALM:realm}")
    private String keycloakRealm;

    private final JwkLoader jwkLoader = new JwkLoader();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> tokenOptional = Optional.ofNullable(request.getHeader("Authorization")).filter(header -> header.startsWith("Bearer "));

        if (tokenOptional.isPresent()) {
            String token = tokenOptional.map(authHeader -> authHeader.replace("Bearer ", "")).orElse("");

            String jwksUri = keycloakUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/certs";

            try {
                String kid = Jwts.parser().build().parse(token).getHeader().get("kid").toString();
                Jwt jwk = jwkLoader.getJwkForKid(kid, jwksUri);

                Claims claims = Jwts.parser()
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                if (claims.getExpiration().before(Date.from(Instant.now()))) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                    return;
                }

                boolean hasProtheticRole = ((List) claims
                        .get(REALM_ACCESS, Map.class)
                        .getOrDefault(ROLES, Collections.emptyList()))
                        .contains(PROTHETIC_USER);

                if (!hasProtheticRole) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient role");
                    return;
                }

                request.setAttribute("user_claims", claims);

            } catch (Exception e) {
                log.error("JWT Validation failed:", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}