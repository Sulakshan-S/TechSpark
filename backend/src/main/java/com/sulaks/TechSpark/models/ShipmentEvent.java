package com.sulaks.TechSpark.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_events")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ShipmentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_event_id")
    private Long shipmentEventId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @Column(nullable = false, length = 40)
    private String status;

    @Column(length = 255)
    private String message;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime = LocalDateTime.now();
}