package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.notification.NotificationRequest;
import com.sulaks.TechSpark.dto.notification.NotificationResponse;
import com.sulaks.TechSpark.enums.NotificationTargetRole;
import com.sulaks.TechSpark.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                notificationService.createNotification(request, authentication.getName())
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(Authentication authentication) {
        return ResponseEntity.ok(
                notificationService.getMyNotifications(authentication.getName())
        );
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/target-role/{targetRole}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByTargetRole(
            @PathVariable NotificationTargetRole targetRole
    ) {
        return ResponseEntity.ok(
                notificationService.getNotificationsByTargetRole(targetRole)
        );
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> updateNotification(
            @PathVariable Long notificationId,
            @Valid @RequestBody NotificationRequest request
    ) {
        return ResponseEntity.ok(
                notificationService.updateNotification(notificationId, request)
        );
    }

    @PatchMapping("/{notificationId}/deactivate")
    public ResponseEntity<NotificationResponse> deactivateNotification(@PathVariable Long notificationId) {
        return ResponseEntity.ok(
                notificationService.deactivateNotification(notificationId)
        );
    }

    @PatchMapping("/{notificationId}/activate")
    public ResponseEntity<NotificationResponse> activateNotification(@PathVariable Long notificationId) {
        return ResponseEntity.ok(
                notificationService.activateNotification(notificationId)
        );
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("Notification deleted successfully");
    }
}