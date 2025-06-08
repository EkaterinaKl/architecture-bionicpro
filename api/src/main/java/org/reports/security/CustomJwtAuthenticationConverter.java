package org.reports.security;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class CustomJwtAuthenticationConverter extends JwtAuthenticationConverter {

    public static final String REALM_ACCESS = "realm_access";

    @Override
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> roles = jwt.getClaimAsMap(REALM_ACCESS);

        List<GrantedAuthority> result = new ArrayList<>();

        for (Map.Entry<String, Object> entry : roles.entrySet()) {
            if (entry.getValue() instanceof JSONArray) {
                result.addAll(((JSONArray) entry.getValue()).stream().map(it -> new SimpleGrantedAuthority("ROLE_" + it.toString())).toList());
            }
        }

        return result;
    }
}