package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.notification.NotificationRequest;
import com.sulaks.TechSpark.dto.notification.NotificationResponse;
import com.sulaks.TechSpark.enums.NotificationTargetRole;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request, String adminEmail);

    List<NotificationResponse> getMyNotifications(String userEmail);

    List<NotificationResponse> getAllNotifications();

    List<NotificationResponse> getNotificationsByTargetRole(NotificationTargetRole targetRole);

    NotificationResponse updateNotification(Long notificationId, NotificationRequest request);

    NotificationResponse deactivateNotification(Long notificationId);

    NotificationResponse activateNotification(Long notificationId);

    void deleteNotification(Long notificationId);
}