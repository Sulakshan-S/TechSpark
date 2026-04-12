package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.variant_inventory.VariantInventoryRequest;
import com.sulaks.TechSpark.dto.variant_inventory.VariantInventoryResponse;
import com.sulaks.TechSpark.service.VariantInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variant-inventories")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VariantInventoryController {

    private final VariantInventoryService variantInventoryService;

    @PostMapping
    public ResponseEntity<VariantInventoryResponse> createVariantInventory(
            @Valid @RequestBody VariantInventoryRequest request
    ) {
        return ResponseEntity.ok(variantInventoryService.createVariantInventory(request));
    }

    @GetMapping
    public ResponseEntity<List<VariantInventoryResponse>> getAllVariantInventories() {
        return ResponseEntity.ok(variantInventoryService.getAllVariantInventories());
    }

    @GetMapping("/{productVariantId}")
    public ResponseEntity<VariantInventoryResponse> getVariantInventoryById(
            @PathVariable Long productVariantId
    ) {
        return ResponseEntity.ok(variantInventoryService.getVariantInventoryById(productVariantId));
    }

    @PutMapping("/{productVariantId}")
    public ResponseEntity<VariantInventoryResponse> updateVariantInventory(
            @PathVariable Long productVariantId,
            @Valid @RequestBody VariantInventoryRequest request
    ) {
        return ResponseEntity.ok(
                variantInventoryService.updateVariantInventory(productVariantId, request)
        );
    }

    @DeleteMapping("/{productVariantId}")
    public ResponseEntity<String> deleteVariantInventory(@PathVariable Long productVariantId) {
        variantInventoryService.deleteVariantInventory(productVariantId);
        return ResponseEntity.ok("Variant inventory deleted successfully");
    }
}