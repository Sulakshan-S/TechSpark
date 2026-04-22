package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.return_request.ReturnItemResponse;
import com.sulaks.TechSpark.dto.return_request.ReturnResponse;
import com.sulaks.TechSpark.models.OrderItem;
import com.sulaks.TechSpark.models.ReturnItem;
import com.sulaks.TechSpark.models.ReturnRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReturnMapper {

    public ReturnItemResponse toReturnItemResponse(ReturnItem returnItem) {
        OrderItem orderItem = returnItem.getOrderItem();

        Long productVariantId = null;
        String productName = null;

        if (orderItem.getProduct_variant() != null) {
            productVariantId = orderItem.getProduct_variant().getProductVariantId();

            if (orderItem.getProduct_variant().getProduct() != null) {
                productName = orderItem.getProduct_variant().getProduct().getName();
            }
        }

        return ReturnItemResponse.builder()
                .returnItemId(returnItem.getReturnItemId())
                .orderItemId(orderItem.getOrderItemId())
                .productVariantId(productVariantId)
                .productName(productName)
                .quantity(returnItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .lineTotal(orderItem.getLineTotal())
                .conditionStatus(returnItem.getConditionStatus())
                .build();
    }

    public ReturnResponse toReturnResponse(ReturnRequest returnRequest, List<ReturnItem> returnItems) {
        return ReturnResponse.builder()
                .returnId(returnRequest.getReturnId())
                .orderId(returnRequest.getOrder().getOrderId())
                .orderStatus(returnRequest.getOrder().getStatus())
                .userId(returnRequest.getUser().getUserId())
                .userName(returnRequest.getUser().getFullName())
                .userEmail(returnRequest.getUser().getEmail())
                .status(returnRequest.getStatus())
                .reason(returnRequest.getReason())
                .comments(returnRequest.getComments())
                .reviewedByUserId(
                        returnRequest.getReviewedByUser() != null ? returnRequest.getReviewedByUser().getUserId() : null
                )
                .reviewedByUserName(
                        returnRequest.getReviewedByUser() != null ? returnRequest.getReviewedByUser().getFullName() : null
                )
                .requestedAt(returnRequest.getRequestedAt())
                .reviewedAt(returnRequest.getReviewedAt())
                .items(returnItems.stream().map(this::toReturnItemResponse).toList())
                .build();
    }
}