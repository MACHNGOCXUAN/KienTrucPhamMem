package iuh.vn.orderserivce.service;

import iuh.vn.orderserivce.dtos.req.OrderRequest;
import iuh.vn.orderserivce.dtos.req.UpdateStatus;
import iuh.vn.orderserivce.dtos.res.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request, String authorizationHeader);
    OrderResponse updateOrderStatus(Long orderId, UpdateStatus request);
    List<OrderResponse> getAllOrders();
}
