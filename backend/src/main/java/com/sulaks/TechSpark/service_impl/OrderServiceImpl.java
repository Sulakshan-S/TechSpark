package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.order.OrderResponse;
import com.sulaks.TechSpark.dto.order.PlaceOrderRequest;
import com.sulaks.TechSpark.dto.order.UpdateOrderStatusRequest;
import com.sulaks.TechSpark.enums.CartStatus;
import com.sulaks.TechSpark.enums.DiscountType;
import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.enums.PaymentStatus;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.OrderMapper;
import com.sulaks.TechSpark.models.*;
import com.sulaks.TechSpark.repository.*;
import com.sulaks.TechSpark.service.OrderService;
import com.sulaks.TechSpark.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final OrderCouponRepo orderCouponRepo;
    private final OrderStatusHistoryRepo orderStatusHistoryRepo;
    private final PaymentRepo paymentRepo;
    private final PaymentEventRepo paymentEventRepo;
    private final UserRepo userRepo;
    private final AddressRepo addressRepo;
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final CouponRepo couponRepo;
    private final VariantInventoryRepo variantInventoryRepo;
    private final OrderMapper orderMapper;
    private final PaymentService paymentService;

    @Override
    public OrderResponse placeOrder(PlaceOrderRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);

        Address shippingAddress = addressRepo.findByAddressIdAndUser_UserId(
                        request.getShippingAddressId(),
                        user.getUserId()
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found with id: " + request.getShippingAddressId()
                ));

        Cart cart = cartRepo.findByUser_UserIdAndStatus(user.getUserId(), CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found"));

        List<CartItem> cartItems = cartItemRepo.findByCart_CartId(cart.getCartId());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot place order because cart is empty");
        }

        validateStockForCartItems(cartItems);

        BigDecimal subtotal = cartItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.valueOf(350);

        BigDecimal discountTotal = BigDecimal.ZERO;
        Coupon appliedCoupon = null;

        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            appliedCoupon = couponRepo.findByCodeIgnoreCase(request.getCouponCode().trim())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Coupon not found with code: " + request.getCouponCode()
                    ));

            discountTotal = calculateCouponDiscount(appliedCoupon, subtotal);
        }

        BigDecimal grandTotal = subtotal
                .subtract(discountTotal)
                .add(shippingFee);

        if (grandTotal.compareTo(BigDecimal.ZERO) < 0) {
            grandTotal = BigDecimal.ZERO;
        }

        Order order = Order.builder()
                .user(user)
                .shippingAddress(shippingAddress)
                .subtotal(subtotal)
                .discountTotal(discountTotal)
                .shippingFee(shippingFee)
                .grandTotal(grandTotal)
                .status(OrderStatus.PLACED)
                .build();

        Order savedOrder = orderRepo.save(order);

        List<OrderItem> savedOrderItems = cartItems.stream()
                .map(cartItem -> {
                    BigDecimal lineTotal = cartItem.getUnitPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                    OrderItem orderItem = OrderItem.builder()
                            .order(savedOrder)
                            .product_variant(cartItem.getProductVariant())
                            .quantity(cartItem.getQuantity())
                            .unitPrice(cartItem.getUnitPrice())
                            .lineTotal(lineTotal)
                            .build();

                    return orderItemRepo.save(orderItem);
                })
                .toList();

        if (appliedCoupon != null && discountTotal.compareTo(BigDecimal.ZERO) > 0) {
            OrderCoupon orderCoupon = OrderCoupon.builder()
                    .order(savedOrder)
                    .coupon(appliedCoupon)
                    .discountAmount(discountTotal)
                    .build();

            orderCouponRepo.save(orderCoupon);
        }

        reduceStock(cartItems);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(savedOrder)
                .status(OrderStatus.PLACED)
                .note("Order placed successfully")
                .changedByUser(user)
                .changedAt(LocalDateTime.now())
                .build();

        orderStatusHistoryRepo.save(history);

        Payment savedPayment = paymentService.createPaymentForOrder(
                savedOrder,
                request.getPaymentMethod()
        );

        cartItemRepo.deleteAll(cartItems);

        List<OrderCoupon> savedCoupons = orderCouponRepo.findByOrder_OrderId(savedOrder.getOrderId());

        return orderMapper.toOrderResponse(savedOrder, savedOrderItems, savedCoupons, savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(String userEmail) {
        User user = getUserByEmail(userEmail);

        return orderRepo.findByUser_UserIdOrderByOrderIdDesc(user.getUserId())
                .stream()
                .map(this::buildOrderResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getMyOrderById(Long orderId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Order order = orderRepo.findByOrderIdAndUser_UserId(orderId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        return buildOrderResponse(order);
    }

    @Override
    public OrderResponse cancelMyOrder(Long orderId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Order order = orderRepo.findByOrderIdAndUser_UserId(orderId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Order is already cancelled");
        }

        if (order.getStatus() == OrderStatus.SHIPPED
                || order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.REFUNDED) {
            throw new IllegalArgumentException("This order cannot be cancelled now");
        }

        Payment payment = paymentRepo.findByOrder_OrderId(order.getOrderId()).orElse(null);

        if (payment != null && payment.getStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException("Paid orders cannot be cancelled directly. Use refund flow.");
        }

        restoreStock(order);

        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepo.save(order);

        cancelPendingPaymentForOrder(updatedOrder, "Payment cancelled because order was cancelled by user");

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(updatedOrder)
                .status(OrderStatus.CANCELLED)
                .note("Order cancelled by user")
                .changedByUser(user)
                .changedAt(LocalDateTime.now())
                .build();

        orderStatusHistoryRepo.save(history);

        return buildOrderResponse(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .sorted((a, b) -> Long.compare(b.getOrderId(), a.getOrderId()))
                .map(this::buildOrderResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        return buildOrderResponse(order);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request, String adminEmail) {
        User admin = getUserByEmail(adminEmail);

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        if (oldStatus == newStatus) {
            throw new IllegalArgumentException("Order already has status: " + newStatus);
        }

        if (oldStatus == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cancelled order status cannot be changed");
        }

        if (newStatus == OrderStatus.CANCELLED) {
            Payment payment = paymentRepo.findByOrder_OrderId(order.getOrderId()).orElse(null);

            if (payment != null && payment.getStatus() == PaymentStatus.PAID) {
                throw new IllegalArgumentException("Paid orders cannot be cancelled directly. Use refund flow.");
            }

            restoreStock(order);
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepo.save(order);

        if (newStatus == OrderStatus.CANCELLED) {
            cancelPendingPaymentForOrder(updatedOrder, "Payment cancelled because order was cancelled by admin");
        }

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(updatedOrder)
                .status(newStatus)
                .note(request.getNote() != null && !request.getNote().trim().isEmpty()
                        ? request.getNote().trim()
                        : "Order status updated to " + newStatus)
                .changedByUser(admin)
                .changedAt(LocalDateTime.now())
                .build();

        orderStatusHistoryRepo.save(history);

        return buildOrderResponse(updatedOrder);
    }

    private void cancelPendingPaymentForOrder(Order order, String message) {
        Payment payment = paymentRepo.findByOrder_OrderId(order.getOrderId()).orElse(null);

        if (payment == null) {
            return;
        }

        if (payment.getStatus() == PaymentStatus.PENDING) {
            payment.setStatus(PaymentStatus.CANCELLED);
            paymentRepo.save(payment);

            PaymentEvent event = PaymentEvent.builder()
                    .payment(payment)
                    .eventType("PAYMENT_CANCELLED")
                    .message(message)
                    .eventTime(LocalDateTime.now())
                    .build();

            paymentEventRepo.save(event);
        }
    }

    private OrderResponse buildOrderResponse(Order order) {
        List<OrderItem> orderItems = orderItemRepo.findByOrder_OrderId(order.getOrderId());
        List<OrderCoupon> orderCoupons = orderCouponRepo.findByOrder_OrderId(order.getOrderId());
        Payment payment = paymentRepo.findByOrder_OrderId(order.getOrderId()).orElse(null);

        return orderMapper.toOrderResponse(order, orderItems, orderCoupons, payment);
    }

    private User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private void validateStockForCartItems(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            VariantInventory inventory = variantInventoryRepo.findById(
                            cartItem.getProductVariant().getProductVariantId()
                    )
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product variant id: "
                                    + cartItem.getProductVariant().getProductVariantId()
                    ));

            if (inventory.getStockQty() < cartItem.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for variant: " + cartItem.getProductVariant().getVariantName()
                );
            }
        }
    }

    private void reduceStock(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            VariantInventory inventory = variantInventoryRepo.findById(
                            cartItem.getProductVariant().getProductVariantId()
                    )
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product variant id: "
                                    + cartItem.getProductVariant().getProductVariantId()
                    ));

            int newStock = inventory.getStockQty() - cartItem.getQuantity();

            if (newStock < 0) {
                throw new IllegalArgumentException(
                        "Insufficient stock for variant: " + cartItem.getProductVariant().getVariantName()
                );
            }

            inventory.setStockQty(newStock);
            variantInventoryRepo.save(inventory);
        }
    }

    private void restoreStock(Order order) {
        List<OrderItem> orderItems = orderItemRepo.findByOrder_OrderId(order.getOrderId());

        for (OrderItem orderItem : orderItems) {
            VariantInventory inventory = variantInventoryRepo.findById(
                            orderItem.getProduct_variant().getProductVariantId()
                    )
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product variant id: "
                                    + orderItem.getProduct_variant().getProductVariantId()
                    ));

            inventory.setStockQty(inventory.getStockQty() + orderItem.getQuantity());
            variantInventoryRepo.save(inventory);
        }
    }

    private BigDecimal calculateCouponDiscount(Coupon coupon, BigDecimal subtotal) {
        LocalDateTime now = LocalDateTime.now();

        if (!coupon.isActive()) {
            throw new IllegalArgumentException("Coupon is inactive");
        }

        if (coupon.getStartsAt() != null && now.isBefore(coupon.getStartsAt())) {
            throw new IllegalArgumentException("Coupon is not active yet");
        }

        if (coupon.getEndsAt() != null && now.isAfter(coupon.getEndsAt())) {
            throw new IllegalArgumentException("Coupon has expired");
        }

        if (subtotal.compareTo(coupon.getMinOrderValue()) < 0) {
            throw new IllegalArgumentException(
                    "Minimum order value required is " + coupon.getMinOrderValue()
            );
        }

        BigDecimal discountAmount;

        if (coupon.getDiscountType() == DiscountType.PERCENT) {
            discountAmount = subtotal
                    .multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discountAmount = coupon.getDiscountValue();
        }

        if (discountAmount.compareTo(subtotal) > 0) {
            discountAmount = subtotal;
        }

        return discountAmount;
    }
}