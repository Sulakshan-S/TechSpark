package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.user_activity_log.UserActivityLogResponse;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.UserActivityLogMapper;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.models.UserActivityLog;
import com.sulaks.TechSpark.repository.UserActivityLogRepo;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.service.UserActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityLogServiceImpl implements UserActivityLogService {

    private final UserActivityLogRepo userActivityLogRepo;
    private final UserRepo userRepo;
    private final UserActivityLogMapper userActivityLogMapper;

    @Override
    public void logActivity(
            String userEmail,
            String action,
            String entityType,
            Long entityId,
            String details
    ) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + userEmail)
                );

        UserActivityLog log = UserActivityLog.builder()
                .user(user)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .build();

        userActivityLogRepo.save(log);
    }

    @Override
    public List<UserActivityLogResponse> getAllLogs() {
        return userActivityLogRepo.findAll()
                .stream()
                .sorted((a, b) -> b.getUserActivityLogId().compareTo(a.getUserActivityLogId()))
                .map(userActivityLogMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserActivityLogResponse> getLogsByUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return userActivityLogRepo.findByUser_UserIdOrderByUserActivityLogIdDesc(userId)
                .stream()
                .map(userActivityLogMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserActivityLogResponse> getLogsByAction(String action) {
        return userActivityLogRepo.findByActionContainingIgnoreCaseOrderByUserActivityLogIdDesc(action)
                .stream()
                .map(userActivityLogMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserActivityLogResponse> getLogsByEntityType(String entityType) {
        return userActivityLogRepo.findByEntityTypeIgnoreCaseOrderByUserActivityLogIdDesc(entityType)
                .stream()
                .map(userActivityLogMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteLog(Long logId) {
        UserActivityLog log = userActivityLogRepo.findById(logId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User activity log not found with id: " + logId)
                );

        userActivityLogRepo.delete(log);
    }
}