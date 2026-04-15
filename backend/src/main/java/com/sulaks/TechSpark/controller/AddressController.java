package com.sulaks.TechSpark.controller;

import com.sulaks.TechSpark.dto.address.AddressRequest;
import com.sulaks.TechSpark.dto.address.AddressResponse;
import com.sulaks.TechSpark.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            @Valid @RequestBody AddressRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                addressService.addAddress(request, authentication.getName())
        );
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getMyAddresses(Authentication authentication) {
        return ResponseEntity.ok(
                addressService.getMyAddresses(authentication.getName())
        );
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getMyAddressById(
            @PathVariable Long addressId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                addressService.getMyAddressById(addressId, authentication.getName())
        );
    }

    @GetMapping("/default")
    public ResponseEntity<AddressResponse> getMyDefaultAddress(Authentication authentication) {
        return ResponseEntity.ok(
                addressService.getMyDefaultAddress(authentication.getName())
        );
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                addressService.updateAddress(addressId, request, authentication.getName())
        );
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<AddressResponse> setDefaultAddress(
            @PathVariable Long addressId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                addressService.setDefaultAddress(addressId, authentication.getName())
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long addressId,
            Authentication authentication
    ) {
        addressService.deleteAddress(addressId, authentication.getName());
        return ResponseEntity.ok("Address deleted successfully");
    }
}