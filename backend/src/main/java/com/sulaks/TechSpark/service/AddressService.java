package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.address.AddressRequest;
import com.sulaks.TechSpark.dto.address.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(AddressRequest request, String userEmail);

    List<AddressResponse> getMyAddresses(String userEmail);

    AddressResponse getMyAddressById(Long addressId, String userEmail);

    AddressResponse getMyDefaultAddress(String userEmail);

    AddressResponse updateAddress(Long addressId, AddressRequest request, String userEmail);

    AddressResponse setDefaultAddress(Long addressId, String userEmail);

    void deleteAddress(Long addressId, String userEmail);
}