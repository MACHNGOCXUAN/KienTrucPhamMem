package iuh.vn.orderserivce.service.impl;

import iuh.vn.orderserivce.dtos.req.OrderRequest;
import iuh.vn.orderserivce.dtos.req.UpdateStatus;
import iuh.vn.orderserivce.dtos.res.OrderItemResponse;
import iuh.vn.orderserivce.dtos.res.OrderResponse;
import iuh.vn.orderserivce.integration.FoodServiceClient;
import iuh.vn.orderserivce.integration.UserServiceClient;
import iuh.vn.orderserivce.integration.dto.FoodInfo;
import iuh.vn.orderserivce.model.Order;
import iuh.vn.orderserivce.model.OrderItem;
import iuh.vn.orderserivce.repository.OrderRepository;
import iuh.vn.orderserivce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "PENDING",
            "CONFIRMED",
            "PAID",
            "CANCELLED",
            "DELIVERED"
    );

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;
    private final FoodServiceClient foodServiceClient;

    @Override
    public OrderResponse createOrder(OrderRequest request, String authorizationHeader) {
        if (request == null || request.getUserId() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "userId is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "items must not be empty");
        }

//        userServiceClient.verifyUser(request.getUserId(), authorizationHeader);

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("PENDING");

        List<OrderItem> items = request.getItems().stream().map(i -> {
            if (i.getFoodId() == null || i.getQuantity() == null || i.getQuantity() <= 0) {
                throw new ResponseStatusException(BAD_REQUEST, "Each item must include valid foodId and quantity > 0");
            }

//            FoodInfo foodInfo = foodServiceClient.getFoodById(i.getFoodId(), authorizationHeader);

            OrderItem item = new OrderItem();
            item.setFoodId(i.getFoodId());
            item.setQuantity(i.getQuantity());
            item.setFoodName(String.valueOf(UUID.randomUUID()));
            item.setPrice(10000.0);

            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);

        double total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        order.setTotalPrice(total);

        Order saved = orderRepository.save(order);

        return mapToResponse(saved);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, UpdateStatus request) {
        if (orderId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "orderId is required");
        }
        if (request == null || request.getStatus() == null || request.getStatus().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "status is required");
        }

        String normalizedStatus = request.getStatus().trim().toUpperCase();
        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid status: " + request.getStatus());
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Order not found: " + orderId));

        order.setStatus(normalizedStatus);
        Order saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    // 🔥 Mapper (entity → response)
    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream().map(i ->
                        OrderItemResponse.builder()
                                .foodId(i.getFoodId())
                                .foodName(i.getFoodName())
                                .price(i.getPrice())
                                .quantity(i.getQuantity())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }
}
