package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.variant_inventory.VariantInventoryRequest;
import com.sulaks.TechSpark.dto.variant_inventory.VariantInventoryResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.VariantInventoryMapper;
import com.sulaks.TechSpark.models.ProductVariant;
import com.sulaks.TechSpark.models.VariantInventory;
import com.sulaks.TechSpark.repository.VariantInventoryRepo;
import com.sulaks.TechSpark.service.VariantInventoryService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantInventoryServiceImpl implements VariantInventoryService {

    private final VariantInventoryRepo variantInventoryRepo;
    private final VariantInventoryMapper variantInventoryMapper;
    private final EntityManager entityManager;

    @Override
    public VariantInventoryResponse createVariantInventory(VariantInventoryRequest request) {
        ProductVariant productVariant = entityManager.find(ProductVariant.class, request.getProductVariantId());
        if (productVariant == null) {
            throw new ResourceNotFoundException(
                    "Product variant not found with id: " + request.getProductVariantId()
            );
        }

        if (variantInventoryRepo.existsById(request.getProductVariantId())) {
            throw new ResourceAlreadyExistsException(
                    "Variant inventory already exists for product variant id: " + request.getProductVariantId()
            );
        }

        VariantInventory variantInventory = variantInventoryMapper.toEntity(request, productVariant);

        VariantInventory savedInventory = variantInventoryRepo.save(variantInventory);
        return variantInventoryMapper.toResponse(savedInventory);
    }

    @Override
    public List<VariantInventoryResponse> getAllVariantInventories() {
        return variantInventoryRepo.findAll()
                .stream()
                .map(variantInventoryMapper::toResponse)
                .toList();
    }

    @Override
    public VariantInventoryResponse getVariantInventoryById(Long productVariantId) {
        VariantInventory variantInventory = variantInventoryRepo.findById(productVariantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant inventory not found for product variant id: " + productVariantId
                        )
                );

        return variantInventoryMapper.toResponse(variantInventory);
    }

    @Override
    public VariantInventoryResponse updateVariantInventory(
            Long productVariantId,
            VariantInventoryRequest request
    ) {
        VariantInventory existingInventory = variantInventoryRepo.findById(productVariantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant inventory not found for product variant id: " + productVariantId
                        )
                );

        existingInventory.setStockQty(request.getStockQty());
        existingInventory.setReservedQty(request.getReservedQty());

        VariantInventory updatedInventory = variantInventoryRepo.save(existingInventory);
        return variantInventoryMapper.toResponse(updatedInventory);
    }

    @Override
    public void deleteVariantInventory(Long productVariantId) {
        VariantInventory variantInventory = variantInventoryRepo.findById(productVariantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant inventory not found for product variant id: " + productVariantId
                        )
                );

        variantInventoryRepo.delete(variantInventory);
    }
}