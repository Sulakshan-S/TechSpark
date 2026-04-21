package com.sulaks.TechSpark.dto.shipment;

import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.enums.PaymentMethod;
import com.sulaks.TechSpark.enums.PaymentStatus;
import com.sulaks.TechSpark.enums.ShipmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ShipmentResponse {

    private Long shipmentId;

    private Long orderId;
    private OrderStatus orderStatus;

    private Long userId;
    private String userName;
    private String userEmail;

    private Long paymentId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private String carrier;
    private String trackingNumber;
    private ShipmentStatus status;
    private LocalDate estimatedDeliveryDate;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;

    private List<ShipmentEventResponse> events;
}