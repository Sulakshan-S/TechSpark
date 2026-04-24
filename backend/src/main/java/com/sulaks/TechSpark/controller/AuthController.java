package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.auth.AuthResponse;
import com.sulaks.TechSpark.dto.auth.LoginRequest;
import com.sulaks.TechSpark.dto.auth.RegisterRequest;
import com.sulaks.TechSpark.dto.auth.UserResponse;
import com.sulaks.TechSpark.dto.auth.MessageResponse;
import com.sulaks.TechSpark.service.AuthService;
import com.sulaks.TechSpark.dto.auth.VerifyOtpRequest;
import com.sulaks.TechSpark.dto.auth.ResendOtpRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.authentication.BadCredentialsException("User is not authenticated");
        }

        return ResponseEntity.ok(authService.getCurrentUser(authentication.getName()));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<MessageResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<MessageResponse> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        return ResponseEntity.ok(authService.resendOtp(request));
    }
}