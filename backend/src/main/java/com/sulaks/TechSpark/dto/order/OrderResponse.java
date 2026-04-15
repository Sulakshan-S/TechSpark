package com.sulaks.TechSpark.dto.order;

import com.sulaks.TechSpark.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponse {

    private Long orderId;

    private Long userId;
    private String userName;
    private String userEmail;

    private Long shippingAddressId;
    private String shippingFullName;
    private String shippingPhone;
    private String shippingLine1;
    private String shippingLine2;
    private String shippingCity;
    private String shippingDistrict;
    private String shippingPostalCode;
    private String shippingCountry;

    private BigDecimal subtotal;
    private BigDecimal discountTotal;
    private BigDecimal shippingFee;
    private BigDecimal grandTotal;

    private OrderStatus status;

    private List<OrderItemResponse> items;
    private List<OrderCouponResponse> coupons;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}