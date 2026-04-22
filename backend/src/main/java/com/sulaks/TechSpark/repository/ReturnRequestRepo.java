package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnRequestRepo extends JpaRepository<ReturnRequest, Long> {

    List<ReturnRequest> findByUser_UserIdOrderByReturnIdDesc(Long userId);

    List<ReturnRequest> findAllByOrderByReturnIdDesc();
}