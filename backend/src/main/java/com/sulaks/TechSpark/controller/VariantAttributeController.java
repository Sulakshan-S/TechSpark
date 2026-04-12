package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.variant_attribute.VariantAttributeRequest;
import com.sulaks.TechSpark.dto.variant_attribute.VariantAttributeResponse;
import com.sulaks.TechSpark.service.VariantAttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variant-attributes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VariantAttributeController {

    private final VariantAttributeService variantAttributeService;

    @PostMapping
    public ResponseEntity<VariantAttributeResponse> createVariantAttribute(
            @Valid @RequestBody VariantAttributeRequest request
    ) {
        return ResponseEntity.ok(variantAttributeService.createVariantAttribute(request));
    }

    @GetMapping
    public ResponseEntity<List<VariantAttributeResponse>> getAllVariantAttributes() {
        return ResponseEntity.ok(variantAttributeService.getAllVariantAttributes());
    }

    @GetMapping("/{variantAttributeId}")
    public ResponseEntity<VariantAttributeResponse> getVariantAttributeById(
            @PathVariable Long variantAttributeId
    ) {
        return ResponseEntity.ok(variantAttributeService.getVariantAttributeById(variantAttributeId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<VariantAttributeResponse>> searchVariantAttributes(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(variantAttributeService.searchVariantAttributes(keyword));
    }

    @PutMapping("/{variantAttributeId}")
    public ResponseEntity<VariantAttributeResponse> updateVariantAttribute(
            @PathVariable Long variantAttributeId,
            @Valid @RequestBody VariantAttributeRequest request
    ) {
        return ResponseEntity.ok(
                variantAttributeService.updateVariantAttribute(variantAttributeId, request)
        );
    }

    @DeleteMapping("/{variantAttributeId}")
    public ResponseEntity<String> deleteVariantAttribute(@PathVariable Long variantAttributeId) {
        variantAttributeService.deleteVariantAttribute(variantAttributeId);
        return ResponseEntity.ok("Variant attribute deleted successfully");
    }
}