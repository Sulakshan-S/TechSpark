package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.return_request.CreateReturnRequest;
import com.sulaks.TechSpark.dto.return_request.ReturnResponse;
import com.sulaks.TechSpark.service.ReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReturnController {

    private final ReturnService returnService;

    @PostMapping
    public ResponseEntity<ReturnResponse> createReturnRequest(
            @Valid @RequestBody CreateReturnRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                returnService.createReturnRequest(request, authentication.getName())
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReturnResponse>> getMyReturns(Authentication authentication) {
        return ResponseEntity.ok(
                returnService.getMyReturns(authentication.getName())
        );
    }

    @GetMapping("/my/{returnId}")
    public ResponseEntity<ReturnResponse> getMyReturnById(
            @PathVariable Long returnId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                returnService.getMyReturnById(returnId, authentication.getName())
        );
    }

    @GetMapping
    public ResponseEntity<List<ReturnResponse>> getAllReturns() {
        return ResponseEntity.ok(returnService.getAllReturns());
    }

    @GetMapping("/{returnId}")
    public ResponseEntity<ReturnResponse> getReturnById(@PathVariable Long returnId) {
        return ResponseEntity.ok(returnService.getReturnById(returnId));
    }

    @PatchMapping("/{returnId}/approve")
    public ResponseEntity<ReturnResponse> approveReturn(
            @PathVariable Long returnId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                returnService.approveReturn(returnId, authentication.getName())
        );
    }

    @PatchMapping("/{returnId}/reject")
    public ResponseEntity<ReturnResponse> rejectReturn(
            @PathVariable Long returnId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                returnService.rejectReturn(returnId, authentication.getName())
        );
    }

    @PatchMapping("/{returnId}/picked-up")
    public ResponseEntity<ReturnResponse> markPickedUp(
            @PathVariable Long returnId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                returnService.markPickedUp(returnId, authentication.getName())
        );
    }

    @PatchMapping("/{returnId}/received")
    public ResponseEntity<ReturnResponse> markReceived(
            @PathVariable Long returnId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                returnService.markReceived(returnId, authentication.getName())
        );
    }

    @PatchMapping("/{returnId}/close")
    public ResponseEntity<ReturnResponse> closeReturn(
            @PathVariable Long returnId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                returnService.closeReturn(returnId, authentication.getName())
        );
    }
}