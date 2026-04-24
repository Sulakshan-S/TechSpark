package com.sulaks.TechSpark.dto.user_activity_log;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserActivityLogResponse {

    private Long userActivityLogId;

    private Long userId;
    private String userName;
    private String userEmail;

    private String action;
    private String entityType;
    private Long entityId;
    private String details;

    private LocalDateTime createdAt;
}