package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.stock_movement.StockMovementRequest;
import com.sulaks.TechSpark.dto.stock_movement.StockMovementResponse;
import com.sulaks.TechSpark.service.StockMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @PostMapping
    public ResponseEntity<StockMovementResponse> createStockMovement(
            @Valid @RequestBody StockMovementRequest request
    ) {
        return ResponseEntity.ok(stockMovementService.createStockMovement(request));
    }

    @GetMapping
    public ResponseEntity<List<StockMovementResponse>> getAllStockMovements() {
        return ResponseEntity.ok(stockMovementService.getAllStockMovements());
    }

    @GetMapping("/{stockMovementId}")
    public ResponseEntity<StockMovementResponse> getStockMovementById(
            @PathVariable Long stockMovementId
    ) {
        return ResponseEntity.ok(stockMovementService.getStockMovementById(stockMovementId));
    }

    @GetMapping("/variant/{productVariantId}")
    public ResponseEntity<List<StockMovementResponse>> getStockMovementsByProductVariant(
            @PathVariable Long productVariantId
    ) {
        return ResponseEntity.ok(
                stockMovementService.getStockMovementsByProductVariant(productVariantId)
        );
    }
}