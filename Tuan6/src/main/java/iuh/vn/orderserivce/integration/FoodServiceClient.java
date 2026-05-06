package iuh.vn.orderserivce.integration;

import com.fasterxml.jackson.databind.JsonNode;
import iuh.vn.orderserivce.integration.dto.FoodInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Component
public class FoodServiceClient {

    private final RestClient restClient;
    private final String foodByIdPath;

    public FoodServiceClient(RestClient.Builder builder,
                             @Value("${app.services.food.base-url:http://localhost:8082}") String foodBaseUrl,
                             @Value("${app.services.food.detail-path:/foods/{foodId}}") String foodByIdPath) {
        this.restClient = builder.baseUrl(foodBaseUrl).build();
        this.foodByIdPath = foodByIdPath;
    }

    public FoodInfo getFoodById(Long foodId, String authorizationHeader) {
        String bearerToken = AuthHeaderUtil.normalizeBearerToken(authorizationHeader);

        try {
            JsonNode response = restClient.get()
                    .uri(foodByIdPath, foodId)
                    .header(AuthHeaderUtil.headerName(), bearerToken)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {
                throw new ResponseStatusException(BAD_GATEWAY, "Food Service returned empty response");
            }

            String name = readFoodName(response);
            Double price = readFoodPrice(response);

            return new FoodInfo(foodId, name, price);
        } catch (RestClientResponseException ex) {
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            if (status == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(BAD_REQUEST, "Food not found: " + foodId);
            }
            throw new ResponseStatusException(BAD_GATEWAY, "Failed to get food detail from Food Service");
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Cannot connect to Food Service");
        }
    }

    private String readFoodName(JsonNode response) {
        if (response.hasNonNull("foodName")) {
            return response.get("foodName").asText();
        }
        if (response.hasNonNull("name")) {
            return response.get("name").asText();
        }
        throw new ResponseStatusException(BAD_GATEWAY, "Food Service response missing food name");
    }

    private Double readFoodPrice(JsonNode response) {
        if (response.hasNonNull("price")) {
            return response.get("price").asDouble();
        }
        if (response.hasNonNull("unitPrice")) {
            return response.get("unitPrice").asDouble();
        }
        throw new ResponseStatusException(BAD_GATEWAY, "Food Service response missing food price");
    }
}
