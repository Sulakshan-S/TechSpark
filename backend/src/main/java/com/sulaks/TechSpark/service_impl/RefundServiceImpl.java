package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.refund.CreateRefundRequest;
import com.sulaks.TechSpark.dto.refund.RefundResponse;
import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.enums.PaymentStatus;
import com.sulaks.TechSpark.enums.RefundStatus;
import com.sulaks.TechSpark.enums.ReturnStatus;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.RefundMapper;
import com.sulaks.TechSpark.models.Order;
import com.sulaks.TechSpark.models.OrderStatusHistory;
import com.sulaks.TechSpark.models.Payment;
import com.sulaks.TechSpark.models.PaymentEvent;
import com.sulaks.TechSpark.models.Refund;
import com.sulaks.TechSpark.models.ReturnRequest;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.repository.OrderRepo;
import com.sulaks.TechSpark.repository.OrderStatusHistoryRepo;
import com.sulaks.TechSpark.repository.PaymentEventRepo;
import com.sulaks.TechSpark.repository.PaymentRepo;
import com.sulaks.TechSpark.repository.RefundRepo;
import com.sulaks.TechSpark.repository.ReturnRequestRepo;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.RoundingMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundServiceImpl implements RefundService {

    private final RefundRepo refundRepo;
    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;
    private final PaymentEventRepo paymentEventRepo;
    private final ReturnRequestRepo returnRequestRepo;
    private final OrderStatusHistoryRepo orderStatusHistoryRepo;
    private final UserRepo userRepo;
    private final RefundMapper refundMapper;

    @Override
    public RefundResponse createRefund(CreateRefundRequest request, String adminEmail) {
        User admin = getUserByEmail(adminEmail);

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + request.getOrderId()
                ));

        ReturnRequest returnRequest = returnRequestRepo.findById(request.getReturnId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Return request not found with id: " + request.getReturnId()
                ));

        if (!returnRequest.getOrder().getOrderId().equals(order.getOrderId())) {
            throw new IllegalArgumentException("Return request does not belong to the given order");
        }

        if (returnRequest.getStatus() != ReturnStatus.RECEIVED) {
            throw new IllegalArgumentException("Refund is allowed only when return status is RECEIVED");
        }

        Payment payment = paymentRepo.findByOrder_OrderId(order.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found for order id: " + order.getOrderId()
                ));

        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new IllegalArgumentException("Refund is allowed only for PAID payments");
        }

        if (refundRepo.existsByReturnRequest_ReturnIdAndStatus(returnRequest.getReturnId(), RefundStatus.SUCCESS)) {
            throw new IllegalArgumentException("This return request is already refunded");
        }

        BigDecimal refundAmount = request.getAmount().setScale(2, RoundingMode.HALF_UP);
        BigDecimal paidAmount = payment.getAmount().setScale(2, RoundingMode.HALF_UP);

        if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Refund amount must be greater than 0");
        }

        if (refundAmount.compareTo(paidAmount) > 0) {
            throw new IllegalArgumentException("Refund amount cannot exceed paid amount");
        }

        Refund refund = Refund.builder()
                .order(order)
                .payment(payment)
                .returnRequest(returnRequest)
                .method(request.getMethod())
                .status(RefundStatus.SUCCESS)
                .amount(refundAmount)
                .reference(
                        request.getReference() != null && !request.getReference().trim().isEmpty()
                                ? request.getReference().trim()
                                : null
                )
                .processedByUser(admin)
                .processedAt(LocalDateTime.now())
                .build();

        Refund savedRefund = refundRepo.save(refund);

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepo.save(payment);

        PaymentEvent paymentEvent = PaymentEvent.builder()
                .payment(payment)
                .eventType("PAYMENT_REFUNDED")
                .message("Refund processed successfully")
                .eventTime(LocalDateTime.now())
                .build();

        paymentEventRepo.save(paymentEvent);

        returnRequest.setStatus(ReturnStatus.REFUNDED);
        order.setStatus(OrderStatus.REFUNDED);

        returnRequestRepo.save(returnRequest);
        orderRepo.save(order);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status(OrderStatus.REFUNDED)
                .note("Order refunded successfully")
                .changedByUser(admin)
                .changedAt(LocalDateTime.now())
                .build();

        orderStatusHistoryRepo.save(history);

        return refundMapper.toRefundResponse(savedRefund);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getAllRefunds() {
        return refundRepo.findAllByOrderByRefundIdDesc()
                .stream()
                .map(refundMapper::toRefundResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RefundResponse getRefundById(Long refundId) {
        Refund refund = refundRepo.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Refund not found with id: " + refundId
                ));

        return refundMapper.toRefundResponse(refund);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getMyRefunds(String userEmail) {
        User user = getUserByEmail(userEmail);

        return refundRepo.findByOrder_User_UserIdOrderByRefundIdDesc(user.getUserId())
                .stream()
                .map(refundMapper::toRefundResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RefundResponse getMyRefundById(Long refundId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Refund refund = refundRepo.findByRefundIdAndOrder_User_UserId(refundId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Refund not found with id: " + refundId
                ));

        return refundMapper.toRefundResponse(refund);
    }

    @Override
    @Transactional(readOnly = true)
    public RefundResponse getRefundByReturnId(Long returnId) {
        Refund refund = refundRepo.findByReturnRequest_ReturnId(returnId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Refund not found for return id: " + returnId
                ));

        return refundMapper.toRefundResponse(refund);
    }

    private User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email
                ));
    }
}