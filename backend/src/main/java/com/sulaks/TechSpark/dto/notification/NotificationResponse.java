package com.sulaks.TechSpark.dto.notification;

import com.sulaks.TechSpark.enums.NotificationTargetRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private Long notificationId;
    private NotificationTargetRole targetRole;
    private String title;
    private String message;
    private boolean active;

    private Long createdByUserId;
    private String createdByUserName;
    private String createdByUserEmail;

    private LocalDateTime createdAt;
}