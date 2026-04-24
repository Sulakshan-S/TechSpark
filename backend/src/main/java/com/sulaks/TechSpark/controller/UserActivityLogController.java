package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.user_activity_log.UserActivityLogResponse;
import com.sulaks.TechSpark.service.UserActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-activity-logs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserActivityLogController {

    private final UserActivityLogService userActivityLogService;

    @GetMapping
    public ResponseEntity<List<UserActivityLogResponse>> getAllLogs() {
        return ResponseEntity.ok(userActivityLogService.getAllLogs());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserActivityLogResponse>> getLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userActivityLogService.getLogsByUser(userId));
    }

    @GetMapping("/action")
    public ResponseEntity<List<UserActivityLogResponse>> getLogsByAction(@RequestParam String action) {
        return ResponseEntity.ok(userActivityLogService.getLogsByAction(action));
    }

    @GetMapping("/entity-type")
    public ResponseEntity<List<UserActivityLogResponse>> getLogsByEntityType(@RequestParam String entityType) {
        return ResponseEntity.ok(userActivityLogService.getLogsByEntityType(entityType));
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<String> deleteLog(@PathVariable Long logId) {
        userActivityLogService.deleteLog(logId);
        return ResponseEntity.ok("User activity log deleted successfully");
    }
}