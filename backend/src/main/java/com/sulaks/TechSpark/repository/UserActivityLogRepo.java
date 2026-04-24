package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActivityLogRepo extends JpaRepository<UserActivityLog, Long> {

    List<UserActivityLog> findByUser_UserIdOrderByUserActivityLogIdDesc(Long userId);

    List<UserActivityLog> findByActionContainingIgnoreCaseOrderByUserActivityLogIdDesc(String action);

    List<UserActivityLog> findByEntityTypeIgnoreCaseOrderByUserActivityLogIdDesc(String entityType);
}