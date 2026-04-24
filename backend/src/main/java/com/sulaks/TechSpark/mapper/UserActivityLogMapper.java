package com.sulaks.TechSpark.mapper;

import com.sulaks.TechSpark.dto.user_activity_log.UserActivityLogResponse;
import com.sulaks.TechSpark.models.UserActivityLog;
import org.springframework.stereotype.Component;

@Component
public class UserActivityLogMapper {

    public UserActivityLogResponse toResponse(UserActivityLog log) {
        return UserActivityLogResponse.builder()
                .userActivityLogId(log.getUserActivityLogId())
                .userId(log.getUser().getUserId())
                .userName(log.getUser().getFullName())
                .userEmail(log.getUser().getEmail())
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .details(log.getDetails())
                .createdAt(log.getCreatedAt())
                .build();
    }
}