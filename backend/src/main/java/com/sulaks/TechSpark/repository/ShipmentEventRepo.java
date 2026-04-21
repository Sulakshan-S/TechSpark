package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.ShipmentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentEventRepo extends JpaRepository<ShipmentEvent, Long> {

    List<ShipmentEvent> findByShipment_ShipmentIdOrderByEventTimeAsc(Long shipmentId);
}