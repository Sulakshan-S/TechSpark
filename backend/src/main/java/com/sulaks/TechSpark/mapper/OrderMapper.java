package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.order.OrderCouponResponse;
import com.sulaks.TechSpark.dto.order.OrderItemResponse;
import com.sulaks.TechSpark.dto.order.OrderResponse;
import com.sulaks.TechSpark.models.Order;
import com.sulaks.TechSpark.models.OrderCoupon;
import com.sulaks.TechSpark.models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productVariantId(orderItem.getProduct_variant().getProductVariantId())
                .sku(orderItem.getProduct_variant().getSku())
                .variantName(orderItem.getProduct_variant().getVariantName())
                .productId(orderItem.getProduct_variant().getProduct().getProductId())
                .productName(orderItem.getProduct_variant().getProduct().getName())
                .productSlug(orderItem.getProduct_variant().getProduct().getSlug())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .lineTotal(orderItem.getLineTotal())
                .build();
    }

    public OrderCouponResponse toOrderCouponResponse(OrderCoupon orderCoupon) {
        return OrderCouponResponse.builder()
                .couponId(orderCoupon.getCoupon().getCouponId())
                .code(orderCoupon.getCoupon().getCode())
                .discountAmount(orderCoupon.getDiscountAmount())
                .build();
    }

    public OrderResponse toOrderResponse(
            Order order,
            List<OrderItem> orderItems,
            List<OrderCoupon> orderCoupons
    ) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())

                .userId(order.getUser().getUserId())
                .userName(order.getUser().getFullName())
                .userEmail(order.getUser().getEmail())

                .shippingAddressId(order.getShippingAddress().getAddressId())
                .shippingFullName(order.getShippingAddress().getFullName())
                .shippingPhone(order.getShippingAddress().getPhone())
                .shippingLine1(order.getShippingAddress().getLine1())
                .shippingLine2(order.getShippingAddress().getLine2())
                .shippingCity(order.getShippingAddress().getCity())
                .shippingDistrict(order.getShippingAddress().getDistrict())
                .shippingPostalCode(order.getShippingAddress().getPostalCode())
                .shippingCountry(order.getShippingAddress().getCountry())

                .subtotal(order.getSubtotal())
                .discountTotal(order.getDiscountTotal())
                .shippingFee(order.getShippingFee())
                .grandTotal(order.getGrandTotal())
                .status(order.getStatus())

                .items(orderItems.stream().map(this::toOrderItemResponse).toList())
                .coupons(orderCoupons.stream().map(this::toOrderCouponResponse).toList())

                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}