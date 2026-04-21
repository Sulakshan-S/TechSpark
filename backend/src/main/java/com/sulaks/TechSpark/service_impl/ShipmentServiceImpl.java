package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.shipment.CreateShipmentRequest;
import com.sulaks.TechSpark.dto.shipment.ShipmentEventResponse;
import com.sulaks.TechSpark.dto.shipment.ShipmentResponse;
import com.sulaks.TechSpark.dto.shipment.UpdateShipmentStatusRequest;
import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.enums.PaymentMethod;
import com.sulaks.TechSpark.enums.PaymentStatus;
import com.sulaks.TechSpark.enums.ShipmentStatus;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.ShipmentMapper;
import com.sulaks.TechSpark.models.*;
import com.sulaks.TechSpark.repository.*;
import com.sulaks.TechSpark.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepo shipmentRepo;
    private final ShipmentEventRepo shipmentEventRepo;
    private final OrderRepo orderRepo;
    private final OrderStatusHistoryRepo orderStatusHistoryRepo;
    private final PaymentRepo paymentRepo;
    private final UserRepo userRepo;
    private final ShipmentMapper shipmentMapper;

    @Override
    public ShipmentResponse createShipment(CreateShipmentRequest request, String adminEmail) {
        User admin = getUserByEmail(adminEmail);

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + request.getOrderId()
                ));

        if (shipmentRepo.existsByOrder_OrderId(order.getOrderId())) {
            throw new ResourceAlreadyExistsException(
                    "Shipment already exists for order id: " + order.getOrderId()
            );
        }

        if (order.getStatus() != OrderStatus.PACKED) {
            throw new IllegalArgumentException("Shipment can only be created when order status is PACKED");
        }

        if (order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.REFUNDED) {
            throw new IllegalArgumentException("Shipment cannot be created for this order");
        }

        Payment payment = paymentRepo.findByOrder_OrderId(order.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found for order id: " + order.getOrderId()
                ));

        if (payment.getMethod() == PaymentMethod.CARD && payment.getStatus() != PaymentStatus.PAID) {
            throw new IllegalArgumentException("Card payment must be completed before creating shipment");
        }

        Shipment shipment = Shipment.builder()
                .order(order)
                .carrier(request.getCarrier().trim())
                .trackingNumber(request.getTrackingNumber().trim())
                .status(ShipmentStatus.PENDING)
                .estimatedDeliveryDate(request.getEstimatedDeliveryDate())
                .build();

        Shipment savedShipment = shipmentRepo.save(shipment);

        saveShipmentEvent(
                savedShipment,
                ShipmentStatus.PENDING.name(),
                request.getMessage() != null && !request.getMessage().trim().isEmpty()
                        ? request.getMessage().trim()
                        : "Shipment created"
        );

        return buildShipmentResponse(savedShipment);
    }

    @Override
    public ShipmentResponse updateShipmentStatus(
            Long shipmentId,
            UpdateShipmentStatusRequest request,
            String adminEmail
    ) {
        User admin = getUserByEmail(adminEmail);

        Shipment shipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment not found with id: " + shipmentId
                ));

        ShipmentStatus oldStatus = shipment.getStatus();
        ShipmentStatus newStatus = request.getStatus();

        if (oldStatus == newStatus) {
            throw new IllegalArgumentException("Shipment already has status: " + newStatus);
        }

        validateStatusTransition(oldStatus, newStatus);

        shipment.setStatus(newStatus);

        if (newStatus == ShipmentStatus.SHIPPED) {
            shipment.setShippedAt(LocalDateTime.now());
            updateOrderStatus(
                    shipment.getOrder(),
                    OrderStatus.SHIPPED,
                    admin,
                    "Order marked as SHIPPED from shipment module"
            );
        } else if (newStatus == ShipmentStatus.DELIVERED) {
            shipment.setDeliveredAt(LocalDateTime.now());
            updateOrderStatus(
                    shipment.getOrder(),
                    OrderStatus.DELIVERED,
                    admin,
                    "Order marked as DELIVERED from shipment module"
            );
        }

        Shipment updatedShipment = shipmentRepo.save(shipment);

        saveShipmentEvent(
                updatedShipment,
                newStatus.name(),
                request.getMessage() != null && !request.getMessage().trim().isEmpty()
                        ? request.getMessage().trim()
                        : getDefaultShipmentMessage(newStatus)
        );

        return buildShipmentResponse(updatedShipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentResponse> getAllShipments() {
        return shipmentRepo.findAllByOrderByShipmentIdDesc()
                .stream()
                .map(this::buildShipmentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentById(Long shipmentId) {
        Shipment shipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment not found with id: " + shipmentId
                ));

        return buildShipmentResponse(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentEventResponse> getShipmentEvents(Long shipmentId) {
        Shipment shipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment not found with id: " + shipmentId
                ));

        return shipmentEventRepo.findByShipment_ShipmentIdOrderByEventTimeAsc(shipment.getShipmentId())
                .stream()
                .map(shipmentMapper::toShipmentEventResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getMyShipmentByOrderId(Long orderId, String userEmail) {
        User user = getUserByEmail(userEmail);

        Order order = orderRepo.findByOrderIdAndUser_UserId(orderId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId
                ));

        Shipment shipment = shipmentRepo.findByOrder_OrderId(order.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment not found for order id: " + orderId
                ));

        return buildShipmentResponse(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentByOrderId(Long orderId) {
        Shipment shipment = shipmentRepo.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment not found for order id: " + orderId
                ));

        return buildShipmentResponse(shipment);
    }

    private void validateStatusTransition(ShipmentStatus oldStatus, ShipmentStatus newStatus) {
        boolean valid =
                (oldStatus == ShipmentStatus.PENDING && newStatus == ShipmentStatus.SHIPPED)
                        || (oldStatus == ShipmentStatus.SHIPPED && newStatus == ShipmentStatus.DELIVERED)
                        || (oldStatus == ShipmentStatus.PENDING && newStatus == ShipmentStatus.RETURNED)
                        || (oldStatus == ShipmentStatus.SHIPPED && newStatus == ShipmentStatus.RETURNED);

        if (!valid) {
            throw new IllegalArgumentException(
                    "Invalid shipment status transition from " + oldStatus + " to " + newStatus
            );
        }
    }

    private void updateOrderStatus(
            Order order,
            OrderStatus newStatus,
            User admin,
            String note
    ) {
        if (order.getStatus() != newStatus) {
            order.setStatus(newStatus);
            orderRepo.save(order);

            OrderStatusHistory history = OrderStatusHistory.builder()
                    .order(order)
                    .status(newStatus)
                    .note(note)
                    .changedByUser(admin)
                    .changedAt(LocalDateTime.now())
                    .build();

            orderStatusHistoryRepo.save(history);
        }
    }

    private void saveShipmentEvent(Shipment shipment, String status, String message) {
        ShipmentEvent event = ShipmentEvent.builder()
                .shipment(shipment)
                .status(status)
                .message(message)
                .eventTime(LocalDateTime.now())
                .build();

        shipmentEventRepo.save(event);
    }

    private ShipmentResponse buildShipmentResponse(Shipment shipment) {
        Payment payment = paymentRepo.findByOrder_OrderId(shipment.getOrder().getOrderId()).orElse(null);
        List<ShipmentEvent> events = shipmentEventRepo.findByShipment_ShipmentIdOrderByEventTimeAsc(
                shipment.getShipmentId()
        );

        return shipmentMapper.toShipmentResponse(shipment, payment, events);
    }

    private String getDefaultShipmentMessage(ShipmentStatus status) {
        return switch (status) {
            case SHIPPED -> "Shipment dispatched";
            case DELIVERED -> "Shipment delivered";
            case RETURNED -> "Shipment returned";
            default -> "Shipment status updated";
        };
    }

    private User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email
                ));
    }
}