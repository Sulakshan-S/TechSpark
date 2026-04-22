package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.return_request.CreateReturnItemRequest;
import com.sulaks.TechSpark.dto.return_request.CreateReturnRequest;
import com.sulaks.TechSpark.dto.return_request.ReturnResponse;
import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.enums.ReturnStatus;
import com.sulaks.TechSpark.enums.ShipmentStatus;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.ReturnMapper;
import com.sulaks.TechSpark.models.Order;
import com.sulaks.TechSpark.models.OrderItem;
import com.sulaks.TechSpark.models.ReturnItem;
import com.sulaks.TechSpark.models.ReturnRequest;
import com.sulaks.TechSpark.models.Shipment;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.repository.OrderItemRepo;
import com.sulaks.TechSpark.repository.OrderRepo;
import com.sulaks.TechSpark.repository.ReturnItemRepo;
import com.sulaks.TechSpark.repository.ReturnRequestRepo;
import com.sulaks.TechSpark.repository.ShipmentRepo;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.service.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ReturnServiceImpl implements ReturnService {

    private final ReturnRequestRepo returnRequestRepo;
    private final ReturnItemRepo returnItemRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ShipmentRepo shipmentRepo;
    private final UserRepo userRepo;
    private final ReturnMapper returnMapper;

    @Override
    public ReturnResponse createReturnRequest(CreateReturnRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);

        Order order = orderRepo.findByOrderIdAndUser_UserId(request.getOrderId(), user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + request.getOrderId()
                ));

        validateOrderIsReturnable(order);
        validateShipmentDelivered(order.getOrderId());
        validateRequestItems(request.getItems());

        List<OrderItem> orderItems = orderItemRepo.findByOrder_OrderId(order.getOrderId());

        ReturnRequest returnRequest = ReturnRequest.builder()
                .order(order)
                .user(user)
                .status(ReturnStatus.REQUESTED)
                .reason(request.getReason().trim())
                .comments(
                        request.getComments() != null && !request.getComments().trim().isEmpty()
                                ? request.getComments().trim()
                                : null
                )
                .requestedAt(LocalDateTime.now())
                .build();

        ReturnRequest savedReturnRequest = returnRequestRepo.save(returnRequest);

        for (CreateReturnItemRequest itemRequest : request.getItems()) {
            OrderItem orderItem = orderItems.stream()
                    .filter(item -> item.getOrderItemId().equals(itemRequest.getOrderItemId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Order item does not belong to this order: " + itemRequest.getOrderItemId()
                    ));

            int alreadyReturnedQty = getAlreadyReturnedQuantity(orderItem.getOrderItemId());

            int remainingQty = orderItem.getQuantity() - alreadyReturnedQty;

            if (itemRequest.getQuantity() > remainingQty) {
                throw new IllegalArgumentException(
                        "Return quantity exceeds remaining returnable quantity for order item id: "
                                + orderItem.getOrderItemId()
                );
            }

            ReturnItem returnItem = ReturnItem.builder()
                    .returnRequest(savedReturnRequest)
                    .orderItem(orderItem)
                    .quantity(itemRequest.getQuantity())
                    .conditionStatus(itemRequest.getConditionStatus())
                    .build();

            returnItemRepo.save(returnItem);
        }

        return buildReturnResponse(savedReturnRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnResponse> getMyReturns(String userEmail) {
        User user = getUserByEmail(userEmail);

        return returnRequestRepo.findByUser_UserIdOrderByReturnIdDesc(user.getUserId())
                .stream()
                .map(this::buildReturnResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReturnResponse getMyReturnById(Long returnId, String userEmail) {
        User user = getUserByEmail(userEmail);

        ReturnRequest returnRequest = returnRequestRepo.findById(returnId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Return request not found with id: " + returnId
                ));

        if (!returnRequest.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("You are not allowed to access this return request");
        }

        return buildReturnResponse(returnRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnResponse> getAllReturns() {
        return returnRequestRepo.findAllByOrderByReturnIdDesc()
                .stream()
                .map(this::buildReturnResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReturnResponse getReturnById(Long returnId) {
        ReturnRequest returnRequest = returnRequestRepo.findById(returnId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Return request not found with id: " + returnId
                ));

        return buildReturnResponse(returnRequest);
    }

    @Override
    public ReturnResponse approveReturn(Long returnId, String adminEmail) {
        User admin = getUserByEmail(adminEmail);

        ReturnRequest returnRequest = getReturnRequest(returnId);

        if (returnRequest.getStatus() != ReturnStatus.REQUESTED) {
            throw new IllegalArgumentException("Only REQUESTED return requests can be approved");
        }

        returnRequest.setStatus(ReturnStatus.APPROVED);
        returnRequest.setReviewedByUser(admin);
        returnRequest.setReviewedAt(LocalDateTime.now());

        return buildReturnResponse(returnRequestRepo.save(returnRequest));
    }

    @Override
    public ReturnResponse rejectReturn(Long returnId, String adminEmail) {
        User admin = getUserByEmail(adminEmail);

        ReturnRequest returnRequest = getReturnRequest(returnId);

        if (returnRequest.getStatus() != ReturnStatus.REQUESTED) {
            throw new IllegalArgumentException("Only REQUESTED return requests can be rejected");
        }

        returnRequest.setStatus(ReturnStatus.REJECTED);
        returnRequest.setReviewedByUser(admin);
        returnRequest.setReviewedAt(LocalDateTime.now());

        return buildReturnResponse(returnRequestRepo.save(returnRequest));
    }

    @Override
    public ReturnResponse markPickedUp(Long returnId, String adminEmail) {
        getUserByEmail(adminEmail);

        ReturnRequest returnRequest = getReturnRequest(returnId);

        if (returnRequest.getStatus() != ReturnStatus.APPROVED) {
            throw new IllegalArgumentException("Only APPROVED return requests can be marked as PICKED_UP");
        }

        returnRequest.setStatus(ReturnStatus.PICKED_UP);

        return buildReturnResponse(returnRequestRepo.save(returnRequest));
    }

    @Override
    public ReturnResponse markReceived(Long returnId, String adminEmail) {
        getUserByEmail(adminEmail);

        ReturnRequest returnRequest = getReturnRequest(returnId);

        if (returnRequest.getStatus() != ReturnStatus.PICKED_UP) {
            throw new IllegalArgumentException("Only PICKED_UP return requests can be marked as RECEIVED");
        }

        returnRequest.setStatus(ReturnStatus.RECEIVED);

        return buildReturnResponse(returnRequestRepo.save(returnRequest));
    }

    @Override
    public ReturnResponse closeReturn(Long returnId, String adminEmail) {
        getUserByEmail(adminEmail);

        ReturnRequest returnRequest = getReturnRequest(returnId);

        if (returnRequest.getStatus() != ReturnStatus.REFUNDED) {
            throw new IllegalArgumentException("Only REFUNDED return requests can be CLOSED");
        }

        returnRequest.setStatus(ReturnStatus.CLOSED);

        return buildReturnResponse(returnRequestRepo.save(returnRequest));
    }

    private ReturnRequest getReturnRequest(Long returnId) {
        return returnRequestRepo.findById(returnId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Return request not found with id: " + returnId
                ));
    }

    private void validateOrderIsReturnable(Order order) {
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Only DELIVERED orders can be returned");
        }
    }

    private void validateShipmentDelivered(Long orderId) {
        Shipment shipment = shipmentRepo.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment not found for order id: " + orderId
                ));

        if (shipment.getStatus() != ShipmentStatus.DELIVERED || shipment.getDeliveredAt() == null) {
            throw new IllegalArgumentException("Return is allowed only after shipment is delivered");
        }
    }

    private void validateRequestItems(List<CreateReturnItemRequest> items) {
        Set<Long> uniqueOrderItemIds = new HashSet<>();

        for (CreateReturnItemRequest item : items) {
            if (!uniqueOrderItemIds.add(item.getOrderItemId())) {
                throw new IllegalArgumentException(
                        "Duplicate order item found in request: " + item.getOrderItemId()
                );
            }
        }
    }

    private int getAlreadyReturnedQuantity(Long orderItemId) {
        List<ReturnItem> existingReturnItems = returnItemRepo.findByOrderItem_OrderItemId(orderItemId);

        int total = 0;

        for (ReturnItem returnItem : existingReturnItems) {
            ReturnStatus status = returnItem.getReturnRequest().getStatus();

            if (status != ReturnStatus.REJECTED) {
                total += returnItem.getQuantity();
            }
        }

        return total;
    }

    private ReturnResponse buildReturnResponse(ReturnRequest returnRequest) {
        List<ReturnItem> items = returnItemRepo.findByReturnRequest_ReturnId(returnRequest.getReturnId());
        return returnMapper.toReturnResponse(returnRequest, items);
    }

    private User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email
                ));
    }
}