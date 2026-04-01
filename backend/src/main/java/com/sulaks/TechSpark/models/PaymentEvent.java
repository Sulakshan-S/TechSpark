package com.sulaks.TechSpark.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_events")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_event_id")
    private Long paymentEventId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "event_type", nullable = false, length = 60)
    private String eventType;

    @Column(length = 255)
    private String message;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime = LocalDateTime.now();
}