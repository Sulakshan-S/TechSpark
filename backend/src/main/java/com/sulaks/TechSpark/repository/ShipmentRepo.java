package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepo extends JpaRepository<Shipment, Long> {

    boolean existsByOrder_OrderId(Long orderId);

    Optional<Shipment> findByOrder_OrderId(Long orderId);

    List<Shipment> findAllByOrderByShipmentIdDesc();
}