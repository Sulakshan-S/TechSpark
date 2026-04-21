package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.shipment.CreateShipmentRequest;
import com.sulaks.TechSpark.dto.shipment.ShipmentEventResponse;
import com.sulaks.TechSpark.dto.shipment.ShipmentResponse;
import com.sulaks.TechSpark.dto.shipment.UpdateShipmentStatusRequest;

import java.util.List;

public interface ShipmentService {

    ShipmentResponse createShipment(CreateShipmentRequest request, String adminEmail);

    ShipmentResponse updateShipmentStatus(Long shipmentId, UpdateShipmentStatusRequest request, String adminEmail);

    List<ShipmentResponse> getAllShipments();

    ShipmentResponse getShipmentById(Long shipmentId);

    List<ShipmentEventResponse> getShipmentEvents(Long shipmentId);

    ShipmentResponse getMyShipmentByOrderId(Long orderId, String userEmail);

    ShipmentResponse getShipmentByOrderId(Long orderId);
}