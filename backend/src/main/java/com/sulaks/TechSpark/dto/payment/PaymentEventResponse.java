package com.sulaks.TechSpark.dto.payment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentEventResponse {

    private Long paymentEventId;
    private String eventType;
    private String message;
    private LocalDateTime eventTime;
}