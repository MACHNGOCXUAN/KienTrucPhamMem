package iuh.vn.orderserivce.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class UserServiceClient {
    private static final String USER_ME_URL = "http://172.16.54.7:8080/users/me";

    private final RestClient restClient;

    public UserServiceClient() {
        this.restClient = RestClient.create();
    }

    public void verifyUser(Long userId, String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Missing Authorization header");
        }

        String rawHeader = authorizationHeader.trim();
        String bearerToken = rawHeader.regionMatches(true, 0, "Bearer ", 0, 7)
                ? rawHeader
                : "Bearer " + rawHeader;

        try {
            JsonNode response = callUserVerify(bearerToken);

            if (response == null) {
                return;
            }

            if (response.has("valid") && !response.path("valid").asBoolean(true)) {
                throw new ResponseStatusException(UNAUTHORIZED, "Token is not valid for this user");
            }

            if (!response.hasNonNull("id")) {
                throw new ResponseStatusException(UNAUTHORIZED, "User service response missing id");
            }

            if (response.has("userId") && response.path("userId").canConvertToLong()) {
                long actualUserId = response.path("userId").asLong();
                if (actualUserId != userId) {
                    throw new ResponseStatusException(FORBIDDEN, "Token does not belong to the requested userId");
                }
            }
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == UNAUTHORIZED.value() && rawHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
                String tokenOnly = rawHeader.substring(7).trim();
                if (!tokenOnly.isEmpty()) {
                    try {
                        JsonNode fallbackResponse = callUserVerify(tokenOnly);
                        if (fallbackResponse == null) {
                            return;
                        }
                        if (!fallbackResponse.hasNonNull("id")) {
                            throw new ResponseStatusException(UNAUTHORIZED, "User service response missing id");
                        }
                        if (fallbackResponse.has("valid") && !fallbackResponse.path("valid").asBoolean(true)) {
                            throw new ResponseStatusException(UNAUTHORIZED, "Token is not valid for this user");
                        }
                        if (fallbackResponse.has("userId") && fallbackResponse.path("userId").canConvertToLong()) {
                            long actualUserId = fallbackResponse.path("userId").asLong();
                            if (actualUserId != userId) {
                                throw new ResponseStatusException(FORBIDDEN, "Token does not belong to the requested userId");
                            }
                        }
                        return;
                    } catch (RestClientResponseException fallbackEx) {
                        ex = fallbackEx;
                    }
                }
            }

            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            if (status == null) {
                throw new ResponseStatusException(SERVICE_UNAVAILABLE, "User service error");
            }
            if (status == UNAUTHORIZED || status == FORBIDDEN) {
                throw new ResponseStatusException(
                        status,
                        "User authorization failed at " + USER_ME_URL + ". Upstream: " + ex.getResponseBodyAsString()
                );
            }
            if (status == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(BAD_REQUEST, "User not found: " + userId);
            }
            throw new ResponseStatusException(BAD_GATEWAY, "Failed to validate user from User Service");
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    SERVICE_UNAVAILABLE,
                    "Cannot connect to User Service at " + USER_ME_URL + ". Cause: " + ex.getClass().getSimpleName()
            );
        }
    }

    private JsonNode callUserVerify(String authorizationValue) {
        return restClient.get()
                .uri(USER_ME_URL)
                .header(AuthHeaderUtil.headerName(), authorizationValue)
                .retrieve()
                .body(JsonNode.class);
    }
}
