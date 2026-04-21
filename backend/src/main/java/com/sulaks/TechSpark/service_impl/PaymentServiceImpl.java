package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.payment.PaymentResponse;
import com.sulaks.TechSpark.dto.payment.SimulateCardPaymentFailureRequest;
import com.sulaks.TechSpark.dto.payment.SimulateCardPaymentRequest;
import com.sulaks.TechSpark.dto.payment.UpdatePaymentStatusRequest;
import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.enums.PaymentMethod;
import com.sulaks.TechSpark.enums.PaymentStatus;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.PaymentMapper;
import com.sulaks.TechSpark.models.*;
import com.sulaks.TechSpark.repository.*;
import com.sulaks.TechSpark.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final PaymentEventRepo paymentEventRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final VariantInventoryRepo variantInventoryRepo;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment createPaymentForOrder(Order order, PaymentMethod paymentMethod) {
        if (paymentRepo.existsByOrder_OrderId(order.getOrderId())) {
            throw new IllegalArgumentException("Payment already exists for order id: " + order.getOrderId());
        }

        Payment payment = Payment.builder()
                .order(order)
                .method(paymentMethod)
                .status(PaymentStatus.PENDING)
                .amount(order.getGrandTotal())
                .build();

        Payment savedPayment = paymentRepo.save(payment);

        savePaymentEvent(
                savedPayment,
                "PAYMENT_CREATED",
                "Payment created with method " + paymentMethod
        );

        return savedPayment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getMyPayments(String userEmail) {
        User user = getUserByEmail(userEmail);

        return paymentRepo.findByOrder_User_UserIdOrderByPaymentIdDesc(user.getUserId())
                .stream()
                .map(this::buildPaymentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getMyPaymentById(Long paymentId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Payment payment = paymentRepo.findByPaymentIdAndOrder_User_UserId(paymentId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        return buildPaymentResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getMyPaymentByOrderId(Long orderId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Payment payment = paymentRepo.findByOrder_OrderIdAndOrder_User_UserId(orderId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order id: " + orderId));

        return buildPaymentResponse(payment);
    }

    @Override
    public PaymentResponse simulateMyCardPaymentStart(Long paymentId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Payment payment = paymentRepo.findByPaymentIdAndOrder_User_UserId(paymentId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getMethod() != PaymentMethod.CARD) {
            throw new IllegalArgumentException("This payment is not a CARD payment");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING payments can start a card payment attempt");
        }

        savePaymentEvent(payment, "CARD_PAYMENT_ATTEMPT_STARTED", "User started card payment attempt");

        return buildPaymentResponse(payment);
    }

    @Override
    public PaymentResponse simulateMyCardPaymentFailure(
            Long paymentId,
            SimulateCardPaymentFailureRequest request,
            String userEmail
    ) {
        User user = getUserByEmail(userEmail);

        Payment payment = paymentRepo.findByPaymentIdAndOrder_User_UserId(paymentId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getMethod() != PaymentMethod.CARD) {
            throw new IllegalArgumentException("This payment is not a CARD payment");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING payments can log card payment failure");
        }

        savePaymentEvent(payment, "CARD_PAYMENT_ATTEMPT_FAILED", request.getMessage().trim());

        return buildPaymentResponse(payment);
    }

    @Override
    public PaymentResponse simulateMyCardPaymentSuccess(
            Long paymentId,
            SimulateCardPaymentRequest request,
            String userEmail
    ) {
        User user = getUserByEmail(userEmail);

        Payment payment = paymentRepo.findByPaymentIdAndOrder_User_UserId(paymentId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getMethod() != PaymentMethod.CARD) {
            throw new IllegalArgumentException("This payment is not a CARD payment");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING payments can be completed");
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionRef(request.getTransactionRef().trim());
        payment.setPaidAt(LocalDateTime.now());

        Payment updatedPayment = paymentRepo.save(payment);

        savePaymentEvent(
                updatedPayment,
                "CARD_PAYMENT_SUCCESS",
                "Card payment completed successfully"
        );

        return buildPaymentResponse(updatedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepo.findAll()
                .stream()
                .sorted((a, b) -> Long.compare(b.getPaymentId(), a.getPaymentId()))
                .map(this::buildPaymentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        return buildPaymentResponse(payment);
    }

    @Override
    public PaymentResponse updatePaymentStatus(
            Long paymentId,
            UpdatePaymentStatusRequest request,
            String adminEmail
    ) {
        getUserByEmail(adminEmail);

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        PaymentStatus oldStatus = payment.getStatus();
        PaymentStatus newStatus = request.getStatus();

        if (oldStatus == newStatus) {
            throw new IllegalArgumentException("Payment already has status: " + newStatus);
        }

        if (oldStatus == PaymentStatus.REFUNDED) {
            throw new IllegalArgumentException("Refunded payment cannot be changed");
        }

        payment.setStatus(newStatus);

        if (request.getTransactionRef() != null && !request.getTransactionRef().trim().isEmpty()) {
            payment.setTransactionRef(request.getTransactionRef().trim());
        }

        if (newStatus == PaymentStatus.PAID) {
            payment.setPaidAt(LocalDateTime.now());
        }

        Payment updatedPayment = paymentRepo.save(payment);

        if (newStatus == PaymentStatus.CANCELLED) {
            cancelOrderAndRestoreStock(updatedPayment.getOrder());
            savePaymentEvent(
                    updatedPayment,
                    "PAYMENT_CANCELLED",
                    request.getMessage() != null && !request.getMessage().trim().isEmpty()
                            ? request.getMessage().trim()
                            : "Payment cancelled"
            );
        } else if (newStatus == PaymentStatus.PAID) {
            savePaymentEvent(
                    updatedPayment,
                    "PAYMENT_MARKED_AS_PAID",
                    request.getMessage() != null && !request.getMessage().trim().isEmpty()
                            ? request.getMessage().trim()
                            : "Payment marked as paid"
            );
        } else if (newStatus == PaymentStatus.REFUNDED) {
            savePaymentEvent(
                    updatedPayment,
                    "PAYMENT_REFUNDED",
                    request.getMessage() != null && !request.getMessage().trim().isEmpty()
                            ? request.getMessage().trim()
                            : "Payment refunded"
            );
        }

        return buildPaymentResponse(updatedPayment);
    }

    private void cancelOrderAndRestoreStock(Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.REFUNDED) {
            return;
        }

        restoreStock(order);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
    }

    private void restoreStock(Order order) {
        List<OrderItem> orderItems = orderItemRepo.findByOrder_OrderId(order.getOrderId());

        for (OrderItem orderItem : orderItems) {
            VariantInventory inventory = variantInventoryRepo.findById(
                    orderItem.getProduct_variant().getProductVariantId()
            ).orElseThrow(() -> new ResourceNotFoundException(
                    "Inventory not found for product variant id: "
                            + orderItem.getProduct_variant().getProductVariantId()
            ));

            inventory.setStockQty(inventory.getStockQty() + orderItem.getQuantity());
            variantInventoryRepo.save(inventory);
        }
    }

    private void savePaymentEvent(Payment payment, String eventType, String message) {
        PaymentEvent event = PaymentEvent.builder()
                .payment(payment)
                .eventType(eventType)
                .message(message)
                .eventTime(LocalDateTime.now())
                .build();

        paymentEventRepo.save(event);
    }

    private PaymentResponse buildPaymentResponse(Payment payment) {
        List<PaymentEvent> events = paymentEventRepo.findByPayment_PaymentIdOrderByEventTimeAsc(
                payment.getPaymentId()
        );

        return paymentMapper.toPaymentResponse(payment, events);
    }

    private User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}