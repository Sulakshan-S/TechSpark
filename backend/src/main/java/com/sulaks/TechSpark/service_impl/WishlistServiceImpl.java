package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.wishlist.WishlistRequest;
import com.sulaks.TechSpark.dto.wishlist.WishlistResponse;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.WishlistMapper;
import com.sulaks.TechSpark.models.Product;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.models.WishlistItem;
import com.sulaks.TechSpark.repository.ProductRepo;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.repository.WishlistItemRepo;
import com.sulaks.TechSpark.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistItemRepo wishlistItemRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final WishlistMapper wishlistMapper;

    @Override
    public WishlistResponse addToWishlist(WishlistRequest request, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + userEmail)
                );

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + request.getProductId())
                );

        boolean alreadyExists = wishlistItemRepo.existsByUser_UserIdAndProduct_ProductId(
                user.getUserId(),
                product.getProductId()
        );

        if (alreadyExists) {
            throw new ResourceAlreadyExistsException("Product already exists in wishlist");
        }

        WishlistItem wishlistItem = WishlistItem.builder()
                .user(user)
                .product(product)
                .build();

        WishlistItem savedWishlistItem = wishlistItemRepo.save(wishlistItem);
        return wishlistMapper.toResponse(savedWishlistItem);
    }

    @Override
    public List<WishlistResponse> getMyWishlist(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + userEmail)
                );

        return wishlistItemRepo.findByUser_UserId(user.getUserId())
                .stream()
                .map(wishlistMapper::toResponse)
                .toList();
    }

    @Override
    public void removeWishlistItem(Long wishlistItemId, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + userEmail)
                );

        WishlistItem wishlistItem = wishlistItemRepo
                .findByWishlistItemIdAndUser_UserId(wishlistItemId, user.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist item not found with id: " + wishlistItemId)
                );

        wishlistItemRepo.delete(wishlistItem);
    }

    @Override
    public void removeFromWishlistByProduct(Long productId, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + userEmail)
                );

        WishlistItem wishlistItem = wishlistItemRepo
                .findByUser_UserIdAndProduct_ProductId(user.getUserId(), productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist item not found for product id: " + productId)
                );

        wishlistItemRepo.delete(wishlistItem);
    }

    @Override
    public boolean isProductInWishlist(Long productId, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + userEmail)
                );

        return wishlistItemRepo.existsByUser_UserIdAndProduct_ProductId(user.getUserId(), productId);
    }
}