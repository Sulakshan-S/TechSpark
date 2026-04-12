package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepo extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByProductVariant_ProductVariantIdOrderByCreatedAtDesc(Long productVariantId);
}