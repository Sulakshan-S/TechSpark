package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.notification.NotificationRequest;
import com.sulaks.TechSpark.dto.notification.NotificationResponse;
import com.sulaks.TechSpark.enums.NotificationTargetRole;
import com.sulaks.TechSpark.enums.UserRole;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.NotificationMapper;
import com.sulaks.TechSpark.models.Notification;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.repository.NotificationRepo;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final UserRepo userRepo;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse createNotification(NotificationRequest request, String adminEmail) {
        User admin = userRepo.findByEmail(adminEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found with email: " + adminEmail)
                );

        Notification notification = Notification.builder()
                .targetRole(request.getTargetRole())
                .title(request.getTitle())
                .message(request.getMessage())
                .isActive(true)
                .createdByUser(admin)
                .build();

        Notification savedNotification = notificationRepo.save(notification);

        return notificationMapper.toResponse(savedNotification);
    }

    @Override
    public List<NotificationResponse> getMyNotifications(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + userEmail)
                );

        NotificationTargetRole userTargetRole = mapUserRoleToNotificationTargetRole(user.getRole());

        return notificationRepo.findByTargetRoleInAndIsActiveTrueOrderByNotificationIdDesc(
                        List.of(NotificationTargetRole.ALL, userTargetRole)
                )
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepo.findAll()
                .stream()
                .sorted((a, b) -> b.getNotificationId().compareTo(a.getNotificationId()))
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> getNotificationsByTargetRole(NotificationTargetRole targetRole) {
        return notificationRepo.findByTargetRoleOrderByNotificationIdDesc(targetRole)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    public NotificationResponse updateNotification(Long notificationId, NotificationRequest request) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found with id: " + notificationId)
                );

        notification.setTargetRole(request.getTargetRole());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());

        Notification updatedNotification = notificationRepo.save(notification);

        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    public NotificationResponse deactivateNotification(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found with id: " + notificationId)
                );

        notification.setActive(false);

        Notification updatedNotification = notificationRepo.save(notification);

        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    public NotificationResponse activateNotification(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found with id: " + notificationId)
                );

        notification.setActive(true);

        Notification updatedNotification = notificationRepo.save(notification);

        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found with id: " + notificationId)
                );

        notificationRepo.delete(notification);
    }

    private NotificationTargetRole mapUserRoleToNotificationTargetRole(UserRole userRole) {
        if (userRole == UserRole.ADMIN) {
            return NotificationTargetRole.ADMIN;
        }

        return NotificationTargetRole.CUSTOMER;
    }
}