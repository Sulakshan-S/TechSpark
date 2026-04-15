package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.address.AddressRequest;
import com.sulaks.TechSpark.dto.address.AddressResponse;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.AddressMapper;
import com.sulaks.TechSpark.models.Address;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.repository.AddressRepo;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final UserRepo userRepo;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressResponse addAddress(AddressRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);

        boolean hasAnyAddress = addressRepo.existsByUser_UserId(user.getUserId());

        Address address = addressMapper.toAddress(request, user);

        if (!hasAnyAddress) {
            address.setDefault(true);
        } else if (Boolean.TRUE.equals(request.getIsDefault())) {
            clearDefaultAddress(user.getUserId());
        }

        Address savedAddress = addressRepo.save(address);
        return addressMapper.toResponse(savedAddress);
    }

    @Override
    public List<AddressResponse> getMyAddresses(String userEmail) {
        User user = getUserByEmail(userEmail);

        return addressRepo.findByUser_UserIdOrderByIsDefaultDescAddressIdDesc(user.getUserId())
                .stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Override
    public AddressResponse getMyAddressById(Long addressId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Address address = getAddressByIdAndUser(addressId, user.getUserId());

        return addressMapper.toResponse(address);
    }

    @Override
    public AddressResponse getMyDefaultAddress(String userEmail) {
        User user = getUserByEmail(userEmail);

        Address address = addressRepo.findByUser_UserIdAndIsDefaultTrue(user.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Default address not found")
                );

        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(Long addressId, AddressRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);
        Address address = getAddressByIdAndUser(addressId, user.getUserId());

        address.setFullName(request.getFullName().trim());
        address.setPhone(request.getPhone().trim());
        address.setLine1(request.getLine1().trim());
        address.setLine2(request.getLine2() != null ? request.getLine2().trim() : null);
        address.setCity(request.getCity().trim());
        address.setDistrict(request.getDistrict().trim());
        address.setPostalCode(request.getPostalCode().trim());
        address.setCountry(
                request.getCountry() != null && !request.getCountry().trim().isEmpty()
                        ? request.getCountry().trim()
                        : "Sri Lanka"
        );

        if (Boolean.TRUE.equals(request.getIsDefault()) && !address.isDefault()) {
            clearDefaultAddress(user.getUserId());
            address.setDefault(true);
        }

        Address updatedAddress = addressRepo.save(address);
        return addressMapper.toResponse(updatedAddress);
    }

    @Override
    @Transactional
    public AddressResponse setDefaultAddress(Long addressId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Address address = getAddressByIdAndUser(addressId, user.getUserId());

        clearDefaultAddress(user.getUserId());
        address.setDefault(true);

        Address updatedAddress = addressRepo.save(address);
        return addressMapper.toResponse(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Address address = getAddressByIdAndUser(addressId, user.getUserId());

        boolean wasDefault = address.isDefault();

        addressRepo.delete(address);

        if (wasDefault) {
            List<Address> remainingAddresses = addressRepo.findByUser_UserIdOrderByIsDefaultDescAddressIdDesc(user.getUserId());

            if (!remainingAddresses.isEmpty()) {
                Address nextDefault = remainingAddresses.get(0);
                nextDefault.setDefault(true);
                addressRepo.save(nextDefault);
            }
        }
    }

    private User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );
    }

    private Address getAddressByIdAndUser(Long addressId, Long userId) {
        return addressRepo.findByAddressIdAndUser_UserId(addressId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found with id: " + addressId)
                );
    }

    private void clearDefaultAddress(Long userId) {
        addressRepo.findByUser_UserIdAndIsDefaultTrue(userId)
                .ifPresent(address -> {
                    address.setDefault(false);
                    addressRepo.save(address);
                });
    }
}