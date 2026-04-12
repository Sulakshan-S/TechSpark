package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.product_variant.ProductVariantRequest;
import com.sulaks.TechSpark.dto.product_variant.ProductVariantResponse;
import com.sulaks.TechSpark.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-variants")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping
    public ResponseEntity<ProductVariantResponse> createProductVariant(
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.ok(productVariantService.createProductVariant(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantResponse>> getAllProductVariants() {
        return ResponseEntity.ok(productVariantService.getAllProductVariants());
    }

    @GetMapping("/{productVariantId}")
    public ResponseEntity<ProductVariantResponse> getProductVariantById(
            @PathVariable Long productVariantId
    ) {
        return ResponseEntity.ok(productVariantService.getProductVariantById(productVariantId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductVariantResponse>> searchProductVariants(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(productVariantService.searchProductVariants(keyword));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductVariantResponse>> getProductVariantsByProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(productVariantService.getProductVariantsByProduct(productId));
    }

    @PutMapping("/{productVariantId}")
    public ResponseEntity<ProductVariantResponse> updateProductVariant(
            @PathVariable Long productVariantId,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.ok(
                productVariantService.updateProductVariant(productVariantId, request)
        );
    }

    @DeleteMapping("/{productVariantId}")
    public ResponseEntity<String> deleteProductVariant(@PathVariable Long productVariantId) {
        productVariantService.deleteProductVariant(productVariantId);
        return ResponseEntity.ok("Product variant deleted successfully");
    }
}