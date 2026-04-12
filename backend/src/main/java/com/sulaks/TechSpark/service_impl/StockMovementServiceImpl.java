package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.stock_movement.StockMovementRequest;
import com.sulaks.TechSpark.dto.stock_movement.StockMovementResponse;
import com.sulaks.TechSpark.enums.StockMovementType;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.StockMovementMapper;
import com.sulaks.TechSpark.models.ProductVariant;
import com.sulaks.TechSpark.models.StockMovement;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.models.VariantInventory;
import com.sulaks.TechSpark.repository.StockMovementRepo;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.repository.VariantInventoryRepo;
import com.sulaks.TechSpark.service.StockMovementService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepo stockMovementRepo;
    private final StockMovementMapper stockMovementMapper;
    private final VariantInventoryRepo variantInventoryRepo;
    private final UserRepo userRepo;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public StockMovementResponse createStockMovement(StockMovementRequest request) {
        ProductVariant productVariant = entityManager.find(ProductVariant.class, request.getProductVariantId());
        if (productVariant == null) {
            throw new ResourceNotFoundException(
                    "Product variant not found with id: " + request.getProductVariantId()
            );
        }

        validateQuantity(request);

        VariantInventory inventory = variantInventoryRepo.findById(request.getProductVariantId())
                .orElseGet(() -> {
                    VariantInventory newInventory = new VariantInventory();
                    newInventory.setProductVariant(productVariant);
                    newInventory.setStockQty(0);
                    newInventory.setReservedQty(0);
                    return newInventory;
                });

        applyInventoryChange(inventory, request);

        variantInventoryRepo.save(inventory);

        User currentUser = getCurrentUser();

        StockMovement stockMovement = stockMovementMapper.toEntity(
                request,
                productVariant,
                currentUser
        );

        StockMovement savedStockMovement = stockMovementRepo.save(stockMovement);
        return stockMovementMapper.toResponse(savedStockMovement);
    }

    @Override
    public List<StockMovementResponse> getAllStockMovements() {
        return stockMovementRepo.findAll()
                .stream()
                .map(stockMovementMapper::toResponse)
                .toList();
    }

    @Override
    public StockMovementResponse getStockMovementById(Long stockMovementId) {
        StockMovement stockMovement = stockMovementRepo.findById(stockMovementId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Stock movement not found with id: " + stockMovementId
                        )
                );

        return stockMovementMapper.toResponse(stockMovement);
    }

    @Override
    public List<StockMovementResponse> getStockMovementsByProductVariant(Long productVariantId) {
        ProductVariant productVariant = entityManager.find(ProductVariant.class, productVariantId);
        if (productVariant == null) {
            throw new ResourceNotFoundException(
                    "Product variant not found with id: " + productVariantId
            );
        }

        return stockMovementRepo.findByProductVariant_ProductVariantIdOrderByCreatedAtDesc(productVariantId)
                .stream()
                .map(stockMovementMapper::toResponse)
                .toList();
    }

    private void validateQuantity(StockMovementRequest request) {
        if (request.getMovementType() == StockMovementType.ADJUST) {
            if (request.getQuantity() == 0) {
                throw new IllegalArgumentException("Adjustment quantity cannot be 0");
            }
            return;
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    private void applyInventoryChange(VariantInventory inventory, StockMovementRequest request) {
        int stockQty = inventory.getStockQty() == null ? 0 : inventory.getStockQty();
        int reservedQty = inventory.getReservedQty() == null ? 0 : inventory.getReservedQty();
        int quantity = request.getQuantity();

        switch (request.getMovementType()) {
            case IN -> stockQty += quantity;

            case OUT -> {
                if (stockQty < quantity) {
                    throw new IllegalArgumentException("Not enough stock to move out");
                }
                stockQty -= quantity;

                if (reservedQty > stockQty) {
                    reservedQty = stockQty;
                }
            }

            case RESERVE -> {
                int availableQty = stockQty - reservedQty;
                if (availableQty < quantity) {
                    throw new IllegalArgumentException("Not enough available stock to reserve");
                }
                reservedQty += quantity;
            }

            case RELEASE -> {
                if (reservedQty < quantity) {
                    throw new IllegalArgumentException("Not enough reserved stock to release");
                }
                reservedQty -= quantity;
            }

            case ADJUST -> {
                int newStockQty = stockQty + quantity;
                if (newStockQty < 0) {
                    throw new IllegalArgumentException("Adjusted stock cannot be negative");
                }
                if (newStockQty < reservedQty) {
                    throw new IllegalArgumentException("Adjusted stock cannot be less than reserved stock");
                }
                stockQty = newStockQty;
            }
        }

        inventory.setStockQty(stockQty);
        inventory.setReservedQty(reservedQty);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        return userRepo.findByEmail(authentication.getName()).orElse(null);
    }
}