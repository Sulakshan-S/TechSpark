package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.user_activity_log.UserActivityLogResponse;

import java.util.List;

public interface UserActivityLogService {

    void logActivity(
            String userEmail,
            String action,
            String entityType,
            Long entityId,
            String details
    );

    List<UserActivityLogResponse> getAllLogs();

    List<UserActivityLogResponse> getLogsByUser(Long userId);

    List<UserActivityLogResponse> getLogsByAction(String action);

    List<UserActivityLogResponse> getLogsByEntityType(String entityType);

    void deleteLog(Long logId);
}