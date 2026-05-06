package iuh.vn.orderserivce.dtos.req;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long foodId;
    private Integer quantity;
}
