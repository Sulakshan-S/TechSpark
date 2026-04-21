package com.sulaks.TechSpark.dto.shipment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShipmentEventResponse {

    private Long shipmentEventId;
    private String status;
    private String message;
    private LocalDateTime eventTime;
}