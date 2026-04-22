package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.refund.CreateRefundRequest;
import com.sulaks.TechSpark.dto.refund.RefundResponse;
import com.sulaks.TechSpark.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundResponse> createRefund(
            @Valid @RequestBody CreateRefundRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(refundService.createRefund(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<RefundResponse>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    @GetMapping("/{refundId}")
    public ResponseEntity<RefundResponse> getRefundById(@PathVariable Long refundId) {
        return ResponseEntity.ok(refundService.getRefundById(refundId));
    }

    @GetMapping("/return/{returnId}")
    public ResponseEntity<RefundResponse> getRefundByReturnId(@PathVariable Long returnId) {
        return ResponseEntity.ok(refundService.getRefundByReturnId(returnId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RefundResponse>> getMyRefunds(Authentication authentication) {
        return ResponseEntity.ok(refundService.getMyRefunds(authentication.getName()));
    }

    @GetMapping("/my/{refundId}")
    public ResponseEntity<RefundResponse> getMyRefundById(
            @PathVariable Long refundId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(refundService.getMyRefundById(refundId, authentication.getName()));
    }
}