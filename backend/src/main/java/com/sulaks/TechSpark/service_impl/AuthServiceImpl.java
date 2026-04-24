package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.auth.*;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.AuthMapper;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.security.JwtService;
import com.sulaks.TechSpark.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sulaks.TechSpark.enums.UserStatus;
import com.sulaks.TechSpark.models.EmailVerificationToken;
import com.sulaks.TechSpark.repository.EmailVerificationTokenRepo;
import com.sulaks.TechSpark.service.EmailService;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final EmailVerificationTokenRepo emailVerificationTokenRepo;
    private final EmailService emailService;

    @Override
    public MessageResponse register(RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        User user = authMapper.toUser(request, passwordEncoder);
        User savedUser = userRepo.save(user);

        String otp = generateOtp();

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(otp)
                .user(savedUser)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        emailVerificationTokenRepo.save(verificationToken);
        emailService.sendVerificationOtp(savedUser.getEmail(), otp);

        return MessageResponse.builder()
                .message("Registration successful. Please verify your email using the OTP sent to your email.")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getStatus() == UserStatus.PENDING_VERIFICATION || Boolean.FALSE.equals(user.getEmailVerified())) {
            throw new BadCredentialsException("Please verify your email before logging in");
        }

        String token = jwtService.generateToken(user.getEmail());

        return authMapper.toAuthResponse(user, token);
    }

    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return authMapper.toUserResponse(user);
    }

    @Override
    public MessageResponse verifyOtp(VerifyOtpRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        EmailVerificationToken verificationToken = emailVerificationTokenRepo.findByToken(request.getOtp())
                .orElseThrow(() -> new BadCredentialsException("Invalid OTP"));

        if (!verificationToken.getUser().getUserId().equals(user.getUserId())) {
            throw new BadCredentialsException("Invalid OTP");
        }

        if (Boolean.TRUE.equals(verificationToken.getUsed()) ||
                verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("OTP is invalid or expired");
        }

        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        userRepo.save(user);

        verificationToken.setUsed(true);
        emailVerificationTokenRepo.save(verificationToken);

        return MessageResponse.builder()
                .message("Email verified successfully. You can now log in.")
                .build();
    }

    @Override
    public MessageResponse resendOtp(ResendOtpRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (Boolean.TRUE.equals(user.getEmailVerified()) || user.getStatus() == UserStatus.ACTIVE) {
            return MessageResponse.builder()
                    .message("Email is already verified.")
                    .build();
        }

        String otp = generateOtp();

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(otp)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        emailVerificationTokenRepo.save(verificationToken);
        emailService.sendVerificationOtp(user.getEmail(), otp);

        return MessageResponse.builder()
                .message("A new OTP has been sent to your email.")
                .build();
    }

    private String generateOtp() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }
}