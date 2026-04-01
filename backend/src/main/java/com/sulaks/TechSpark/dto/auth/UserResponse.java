package com.sulaks.TechSpark.dto.auth;

import com.sulaks.TechSpark.enums.UserRole;
import com.sulaks.TechSpark.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private UserRole role;
    private UserStatus status;
}
