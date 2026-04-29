package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.enums.NotificationTargetRole;
import com.sulaks.TechSpark.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification> findByIsActiveTrueOrderByNotificationIdDesc();

    List<Notification> findByTargetRoleInAndIsActiveTrueOrderByNotificationIdDesc(
            List<NotificationTargetRole> targetRoles
    );

    List<Notification> findByTargetRoleOrderByNotificationIdDesc(NotificationTargetRole targetRole);
}