package com.sulaks.TechSpark.repository;

import com.sulaks.TechSpark.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, Long> {

    List<Address> findByUser_UserIdOrderByIsDefaultDescAddressIdDesc(Long userId);

    Optional<Address> findByAddressIdAndUser_UserId(Long addressId, Long userId);

    Optional<Address> findByUser_UserIdAndIsDefaultTrue(Long userId);

    boolean existsByUser_UserId(Long userId);
}