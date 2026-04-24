package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.notification.NotificationResponse;
import com.sulaks.TechSpark.models.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .targetRole(notification.getTargetRole())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .active(notification.isActive())
                .createdByUserId(
                        notification.getCreatedByUser() != null
                                ? notification.getCreatedByUser().getUserId()
                                : null
                )
                .createdByUserName(
                        notification.getCreatedByUser() != null
                                ? notification.getCreatedByUser().getFullName()
                                : null
                )
                .createdByUserEmail(
                        notification.getCreatedByUser() != null
                                ? notification.getCreatedByUser().getEmail()
                                : null
                )
                .createdAt(notification.getCreatedAt())
                .build();
    }
}