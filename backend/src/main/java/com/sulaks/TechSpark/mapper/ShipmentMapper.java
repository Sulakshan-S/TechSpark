package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.shipment.ShipmentEventResponse;
import com.sulaks.TechSpark.dto.shipment.ShipmentResponse;
import com.sulaks.TechSpark.models.Payment;
import com.sulaks.TechSpark.models.Shipment;
import com.sulaks.TechSpark.models.ShipmentEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipmentMapper {

    public ShipmentEventResponse toShipmentEventResponse(ShipmentEvent event) {
        return ShipmentEventResponse.builder()
                .shipmentEventId(event.getShipmentEventId())
                .status(event.getStatus())
                .message(event.getMessage())
                .eventTime(event.getEventTime())
                .build();
    }

    public ShipmentResponse toShipmentResponse(
            Shipment shipment,
            Payment payment,
            List<ShipmentEvent> events
    ) {
        return ShipmentResponse.builder()
                .shipmentId(shipment.getShipmentId())
                .orderId(shipment.getOrder().getOrderId())
                .orderStatus(shipment.getOrder().getStatus())
                .userId(shipment.getOrder().getUser().getUserId())
                .userName(shipment.getOrder().getUser().getFullName())
                .userEmail(shipment.getOrder().getUser().getEmail())
                .paymentId(payment != null ? payment.getPaymentId() : null)
                .paymentMethod(payment != null ? payment.getMethod() : null)
                .paymentStatus(payment != null ? payment.getStatus() : null)
                .carrier(shipment.getCarrier())
                .trackingNumber(shipment.getTrackingNumber())
                .status(shipment.getStatus())
                .estimatedDeliveryDate(shipment.getEstimatedDeliveryDate())
                .shippedAt(shipment.getShippedAt())
                .deliveredAt(shipment.getDeliveredAt())
                .createdAt(shipment.getCreatedAt())
                .events(events.stream().map(this::toShipmentEventResponse).toList())
                .build();
    }
}