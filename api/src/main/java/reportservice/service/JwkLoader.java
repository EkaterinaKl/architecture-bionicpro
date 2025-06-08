package reportservice.service;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class JwkLoader {

    private static final Logger log = LoggerFactory.getLogger(JwkLoader.class);

    private RestTemplate restTemplate = new RestTemplate();
    private Map<String, Jwt> jwkCache = new HashMap<>();

    public Jwt getJwkForKid(String kid, String jwksUri) {
        synchronized (this) {
            if (jwkCache.containsKey(kid)) {
                return jwkCache.get(kid);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<HashMap> response = restTemplate.exchange(URI.create(jwksUri), HttpMethod.GET, entity, HashMap.class);
            Map<String, Object> jwksMap = response.getBody();

            Map<String, Object> keys = (Map<String, Object>) jwksMap.get("keys");

            if (keys != null && !keys.isEmpty()) {
                for (Object obj : keys.values()) {
                    var keyData = (Map<String, Object>) obj;
                    String currentKid = (String) keyData.get("kid");
                    if (currentKid.equals(kid)) {
                        JwtParser parser = Jwts.parser()
//                                .setSigningKey(keyData.toString())
                                .build();
                        Jwt jwk = parser.parse(keyData.toString());
//                        Jwt jwk = Jwts.parseFromJson(keyData.toString());
                        jwkCache.put(currentKid, jwk);
                        return jwk;//
//                        jwkCache.put(currentKid, kid);
//                        return kid;
                    }
                }
            }

            throw new RuntimeException("No matching kid '" + kid + "' found in JWKS");
        }
    }
}
