package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.shipment.CreateShipmentRequest;
import com.sulaks.TechSpark.dto.shipment.ShipmentEventResponse;
import com.sulaks.TechSpark.dto.shipment.ShipmentResponse;
import com.sulaks.TechSpark.dto.shipment.UpdateShipmentStatusRequest;
import com.sulaks.TechSpark.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(
            @Valid @RequestBody CreateShipmentRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                shipmentService.createShipment(request, authentication.getName())
        );
    }

    @PatchMapping("/{shipmentId}/status")
    public ResponseEntity<ShipmentResponse> updateShipmentStatus(
            @PathVariable Long shipmentId,
            @Valid @RequestBody UpdateShipmentStatusRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                shipmentService.updateShipmentStatus(shipmentId, request, authentication.getName())
        );
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<ShipmentResponse> getShipmentById(@PathVariable Long shipmentId) {
        return ResponseEntity.ok(shipmentService.getShipmentById(shipmentId));
    }

    @GetMapping("/{shipmentId}/events")
    public ResponseEntity<List<ShipmentEventResponse>> getShipmentEvents(@PathVariable Long shipmentId) {
        return ResponseEntity.ok(shipmentService.getShipmentEvents(shipmentId));
    }

    @GetMapping("/my/order/{orderId}")
    public ResponseEntity<ShipmentResponse> getMyShipmentByOrderId(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                shipmentService.getMyShipmentByOrderId(orderId, authentication.getName())
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShipmentResponse> getShipmentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(shipmentService.getShipmentByOrderId(orderId));
    }
}