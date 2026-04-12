package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.variant_inventory.VariantInventoryRequest;
import com.sulaks.TechSpark.dto.variant_inventory.VariantInventoryResponse;

import java.util.List;

public interface VariantInventoryService {

    VariantInventoryResponse createVariantInventory(VariantInventoryRequest request);

    List<VariantInventoryResponse> getAllVariantInventories();

    VariantInventoryResponse getVariantInventoryById(Long productVariantId);

    VariantInventoryResponse updateVariantInventory(Long productVariantId, VariantInventoryRequest request);

    void deleteVariantInventory(Long productVariantId);
}