package com.sulaks.TechSpark.dto.return_request;

import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.enums.ReturnStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReturnResponse {

    private Long returnId;

    private Long orderId;
    private OrderStatus orderStatus;

    private Long userId;
    private String userName;
    private String userEmail;

    private ReturnStatus status;
    private String reason;
    private String comments;

    private Long reviewedByUserId;
    private String reviewedByUserName;
    private LocalDateTime requestedAt;
    private LocalDateTime reviewedAt;

    private List<ReturnItemResponse> items;
}