package iuh.vn.orderserivce.integration;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public final class AuthHeaderUtil {

    private AuthHeaderUtil() {
    }

    public static String normalizeBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Missing Authorization header");
        }

        String value = authorizationHeader.trim();
        if (value.regionMatches(true, 0, "Bearer ", 0, 7)) {
            String token = value.substring(7).trim();
            if (token.isEmpty()) {
                throw new ResponseStatusException(UNAUTHORIZED, "Invalid bearer token");
            }
            return "Bearer " + token;
        }

        throw new ResponseStatusException(UNAUTHORIZED, "Authorization header must be Bearer token");
    }

    public static String headerName() {
        return HttpHeaders.AUTHORIZATION;
    }
}
