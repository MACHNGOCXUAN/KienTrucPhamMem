package iuh.vn.orderserivce.dtos.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private Double totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
