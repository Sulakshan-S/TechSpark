package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.order.OrderResponse;
import com.sulaks.TechSpark.dto.order.PlaceOrderRequest;
import com.sulaks.TechSpark.dto.order.UpdateOrderStatusRequest;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(PlaceOrderRequest request, String userEmail);

    List<OrderResponse> getMyOrders(String userEmail);

    OrderResponse getMyOrderById(Long orderId, String userEmail);

    OrderResponse cancelMyOrder(Long orderId, String userEmail);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long orderId);

    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request, String adminEmail);
}