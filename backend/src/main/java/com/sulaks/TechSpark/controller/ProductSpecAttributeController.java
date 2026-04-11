package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.product_spec_attribute.ProductSpecAttributeRequest;
import com.sulaks.TechSpark.dto.product_spec_attribute.ProductSpecAttributeResponse;
import com.sulaks.TechSpark.service.ProductSpecAttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-spec-attributes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductSpecAttributeController {

    private final ProductSpecAttributeService productSpecAttributeService;

    @PostMapping
    public ResponseEntity<ProductSpecAttributeResponse> createProductSpecAttribute(
            @Valid @RequestBody ProductSpecAttributeRequest request
    ) {
        return ResponseEntity.ok(productSpecAttributeService.createProductSpecAttribute(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductSpecAttributeResponse>> getAllProductSpecAttributes() {
        return ResponseEntity.ok(productSpecAttributeService.getAllProductSpecAttributes());
    }

    @GetMapping("/{productSpecAttributeId}")
    public ResponseEntity<ProductSpecAttributeResponse> getProductSpecAttributeById(
            @PathVariable Long productSpecAttributeId
    ) {
        return ResponseEntity.ok(productSpecAttributeService.getProductSpecAttributeById(productSpecAttributeId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductSpecAttributeResponse>> getProductSpecAttributesByCategory(
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(productSpecAttributeService.getProductSpecAttributesByCategory(categoryId));
    }

    @GetMapping("/category/{categoryId}/active")
    public ResponseEntity<List<ProductSpecAttributeResponse>> getActiveProductSpecAttributesByCategory(
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(productSpecAttributeService.getActiveProductSpecAttributesByCategory(categoryId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductSpecAttributeResponse>> searchProductSpecAttributes(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(productSpecAttributeService.searchProductSpecAttributes(keyword));
    }

    @PutMapping("/{productSpecAttributeId}")
    public ResponseEntity<ProductSpecAttributeResponse> updateProductSpecAttribute(
            @PathVariable Long productSpecAttributeId,
            @Valid @RequestBody ProductSpecAttributeRequest request
    ) {
        return ResponseEntity.ok(
                productSpecAttributeService.updateProductSpecAttribute(productSpecAttributeId, request)
        );
    }

    @DeleteMapping("/{productSpecAttributeId}")
    public ResponseEntity<String> deleteProductSpecAttribute(@PathVariable Long productSpecAttributeId) {
        productSpecAttributeService.deleteProductSpecAttribute(productSpecAttributeId);
        return ResponseEntity.ok("Product spec attribute deleted successfully");
    }
}