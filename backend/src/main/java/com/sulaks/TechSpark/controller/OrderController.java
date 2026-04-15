package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.order.OrderResponse;
import com.sulaks.TechSpark.dto.order.PlaceOrderRequest;
import com.sulaks.TechSpark.dto.order.UpdateOrderStatusRequest;
import com.sulaks.TechSpark.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody PlaceOrderRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                orderService.placeOrder(request, authentication.getName())
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(
                orderService.getMyOrders(authentication.getName())
        );
    }

    @GetMapping("/my/{orderId}")
    public ResponseEntity<OrderResponse> getMyOrderById(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                orderService.getMyOrderById(orderId, authentication.getName())
        );
    }

    @PatchMapping("/my/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelMyOrder(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                orderService.cancelMyOrder(orderId, authentication.getName())
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, request, authentication.getName())
        );
    }
}