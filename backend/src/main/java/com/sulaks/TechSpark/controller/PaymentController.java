package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.payment.PaymentResponse;
import com.sulaks.TechSpark.dto.payment.SimulateCardPaymentFailureRequest;
import com.sulaks.TechSpark.dto.payment.SimulateCardPaymentRequest;
import com.sulaks.TechSpark.dto.payment.UpdatePaymentStatusRequest;
import com.sulaks.TechSpark.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(Authentication authentication) {
        return ResponseEntity.ok(paymentService.getMyPayments(authentication.getName()));
    }

    @GetMapping("/my/{paymentId}")
    public ResponseEntity<PaymentResponse> getMyPaymentById(
            @PathVariable Long paymentId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(paymentService.getMyPaymentById(paymentId, authentication.getName()));
    }

    @GetMapping("/my/order/{orderId}")
    public ResponseEntity<PaymentResponse> getMyPaymentByOrderId(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(paymentService.getMyPaymentByOrderId(orderId, authentication.getName()));
    }

    @PostMapping("/my/{paymentId}/card/start")
    public ResponseEntity<PaymentResponse> simulateMyCardPaymentStart(
            @PathVariable Long paymentId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                paymentService.simulateMyCardPaymentStart(paymentId, authentication.getName())
        );
    }

    @PostMapping("/my/{paymentId}/card/fail")
    public ResponseEntity<PaymentResponse> simulateMyCardPaymentFailure(
            @PathVariable Long paymentId,
            @Valid @RequestBody SimulateCardPaymentFailureRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                paymentService.simulateMyCardPaymentFailure(
                        paymentId,
                        request,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/my/{paymentId}/card/success")
    public ResponseEntity<PaymentResponse> simulateMyCardPaymentSuccess(
            @PathVariable Long paymentId,
            @Valid @RequestBody SimulateCardPaymentRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                paymentService.simulateMyCardPaymentSuccess(
                        paymentId,
                        request,
                        authentication.getName()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @PatchMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable Long paymentId,
            @Valid @RequestBody UpdatePaymentStatusRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                paymentService.updatePaymentStatus(paymentId, request, authentication.getName())
        );
    }
}