package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.stock_movement.StockMovementRequest;
import com.sulaks.TechSpark.dto.stock_movement.StockMovementResponse;

import java.util.List;

public interface StockMovementService {

    StockMovementResponse createStockMovement(StockMovementRequest request);

    List<StockMovementResponse> getAllStockMovements();

    StockMovementResponse getStockMovementById(Long stockMovementId);

    List<StockMovementResponse> getStockMovementsByProductVariant(Long productVariantId);
}