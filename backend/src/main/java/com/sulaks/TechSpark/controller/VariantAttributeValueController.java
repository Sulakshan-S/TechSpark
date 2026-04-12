package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.variant_attribute_value.VariantAttributeValueRequest;
import com.sulaks.TechSpark.dto.variant_attribute_value.VariantAttributeValueResponse;
import com.sulaks.TechSpark.service.VariantAttributeValueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variant-attribute-values")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VariantAttributeValueController {

    private final VariantAttributeValueService variantAttributeValueService;

    @PostMapping
    public ResponseEntity<VariantAttributeValueResponse> createVariantAttributeValue(
            @Valid @RequestBody VariantAttributeValueRequest request
    ) {
        return ResponseEntity.ok(variantAttributeValueService.createVariantAttributeValue(request));
    }

    @GetMapping
    public ResponseEntity<List<VariantAttributeValueResponse>> getAllVariantAttributeValues() {
        return ResponseEntity.ok(variantAttributeValueService.getAllVariantAttributeValues());
    }

    @GetMapping("/{variantAttributeValueId}")
    public ResponseEntity<VariantAttributeValueResponse> getVariantAttributeValueById(
            @PathVariable Long variantAttributeValueId
    ) {
        return ResponseEntity.ok(
                variantAttributeValueService.getVariantAttributeValueById(variantAttributeValueId)
        );
    }

    @GetMapping("/product-variant/{productVariantId}")
    public ResponseEntity<List<VariantAttributeValueResponse>> getValuesByProductVariant(
            @PathVariable Long productVariantId
    ) {
        return ResponseEntity.ok(
                variantAttributeValueService.getValuesByProductVariant(productVariantId)
        );
    }

    @GetMapping("/variant-attribute/{variantAttributeId}")
    public ResponseEntity<List<VariantAttributeValueResponse>> getValuesByVariantAttribute(
            @PathVariable Long variantAttributeId
    ) {
        return ResponseEntity.ok(
                variantAttributeValueService.getValuesByVariantAttribute(variantAttributeId)
        );
    }

    @PutMapping("/{variantAttributeValueId}")
    public ResponseEntity<VariantAttributeValueResponse> updateVariantAttributeValue(
            @PathVariable Long variantAttributeValueId,
            @Valid @RequestBody VariantAttributeValueRequest request
    ) {
        return ResponseEntity.ok(
                variantAttributeValueService.updateVariantAttributeValue(variantAttributeValueId, request)
        );
    }

    @DeleteMapping("/{variantAttributeValueId}")
    public ResponseEntity<String> deleteVariantAttributeValue(
            @PathVariable Long variantAttributeValueId
    ) {
        variantAttributeValueService.deleteVariantAttributeValue(variantAttributeValueId);
        return ResponseEntity.ok("Variant attribute value deleted successfully");
    }
}