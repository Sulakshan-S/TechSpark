package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.productimage.ProductImageRequest;
import com.sulaks.TechSpark.dto.productimage.ProductImageResponse;
import com.sulaks.TechSpark.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-images")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<ProductImageResponse> createProductImage(
            @Valid @RequestBody ProductImageRequest request
    ) {
        return ResponseEntity.ok(productImageService.createProductImage(request));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImageResponse>> getImagesByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(productImageService.getImagesByProductId(productId));
    }

    @GetMapping("/{productImageId}")
    public ResponseEntity<ProductImageResponse> getProductImageById(@PathVariable Long productImageId) {
        return ResponseEntity.ok(productImageService.getProductImageById(productImageId));
    }

    @GetMapping("/product/{productId}/primary")
    public ResponseEntity<ProductImageResponse> getPrimaryImageByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(productImageService.getPrimaryImageByProductId(productId));
    }

    @PutMapping("/{productImageId}")
    public ResponseEntity<ProductImageResponse> updateProductImage(
            @PathVariable Long productImageId,
            @Valid @RequestBody ProductImageRequest request
    ) {
        return ResponseEntity.ok(productImageService.updateProductImage(productImageId, request));
    }

    @DeleteMapping("/{productImageId}")
    public ResponseEntity<String> deleteProductImage(@PathVariable Long productImageId) {
        productImageService.deleteProductImage(productImageId);
        return ResponseEntity.ok("Product image deleted successfully");
    }
}