package iuh.vn.orderserivce.controller;

import iuh.vn.orderserivce.dtos.req.OrderRequest;
import iuh.vn.orderserivce.dtos.req.UpdateStatus;
import iuh.vn.orderserivce.dtos.res.OrderResponse;
import iuh.vn.orderserivce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // POST /orders
    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request,
                                     @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        return orderService.createOrder(request, authorizationHeader);
    }

    // GET /orders
    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    // PATCH /orders/{id}/status
    @PatchMapping("/{id}/status")
    public OrderResponse updateStatusOrder(@PathVariable Long id,
                                           @RequestBody UpdateStatus request) {
        return orderService.updateOrderStatus(id, request);
    }
}