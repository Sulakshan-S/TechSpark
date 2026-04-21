package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.payment.PaymentResponse;
import com.sulaks.TechSpark.dto.payment.SimulateCardPaymentFailureRequest;
import com.sulaks.TechSpark.dto.payment.SimulateCardPaymentRequest;
import com.sulaks.TechSpark.dto.payment.UpdatePaymentStatusRequest;
import com.sulaks.TechSpark.enums.PaymentMethod;
import com.sulaks.TechSpark.models.Order;
import com.sulaks.TechSpark.models.Payment;

import java.util.List;

public interface PaymentService {

    Payment createPaymentForOrder(Order order, PaymentMethod paymentMethod);

    List<PaymentResponse> getMyPayments(String userEmail);

    PaymentResponse getMyPaymentById(Long paymentId, String userEmail);

    PaymentResponse getMyPaymentByOrderId(Long orderId, String userEmail);

    PaymentResponse simulateMyCardPaymentStart(Long paymentId, String userEmail);

    PaymentResponse simulateMyCardPaymentFailure(
            Long paymentId,
            SimulateCardPaymentFailureRequest request,
            String userEmail
    );

    PaymentResponse simulateMyCardPaymentSuccess(
            Long paymentId,
            SimulateCardPaymentRequest request,
            String userEmail
    );

    List<PaymentResponse> getAllPayments();

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse updatePaymentStatus(
            Long paymentId,
            UpdatePaymentStatusRequest request,
            String adminEmail
    );
}