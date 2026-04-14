package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.cart.CartAddRequest;
import com.sulaks.TechSpark.dto.cart.CartResponse;
import com.sulaks.TechSpark.dto.cart.CartUpdateQuantityRequest;
import com.sulaks.TechSpark.enums.CartStatus;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.CartMapper;
import com.sulaks.TechSpark.models.Cart;
import com.sulaks.TechSpark.models.CartItem;
import com.sulaks.TechSpark.models.ProductVariant;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.models.VariantInventory;
import com.sulaks.TechSpark.repository.CartItemRepo;
import com.sulaks.TechSpark.repository.CartRepo;
import com.sulaks.TechSpark.repository.ProductVariantRepo;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.repository.VariantInventoryRepo;
import com.sulaks.TechSpark.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final UserRepo userRepo;
    private final ProductVariantRepo productVariantRepo;
    private final VariantInventoryRepo variantInventoryRepo;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getMyCart(String userEmail) {
        User user = getUserByEmail(userEmail);
        Cart cart = getOrCreateActiveCart(user);
        List<CartItem> cartItems = cartItemRepo.findByCart_CartId(cart.getCartId());

        return cartMapper.toCartResponse(cart, cartItems);
    }

    @Override
    public CartResponse addItemToCart(CartAddRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);
        Cart cart = getOrCreateActiveCart(user);

        ProductVariant productVariant = productVariantRepo.findById(request.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product variant not found with id: " + request.getProductVariantId()
                ));

        VariantInventory inventory = variantInventoryRepo.findById(productVariant.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for product variant id: " + productVariant.getProductVariantId()
                ));

        int availableStock = inventory.getStockQty() - inventory.getReservedQty();
        if (availableStock <= 0) {
            throw new IllegalArgumentException("This variant is out of stock");
        }

        BigDecimal unitPrice = productVariant.getDiscountPrice() != null
                ? productVariant.getDiscountPrice()
                : productVariant.getPrice();

        if (unitPrice == null) {
            throw new IllegalArgumentException("Product variant price is not available");
        }

        CartItem cartItem = cartItemRepo
                .findByCart_CartIdAndProductVariant_ProductVariantId(
                        cart.getCartId(),
                        productVariant.getProductVariantId()
                )
                .orElse(null);

        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + request.getQuantity();

            if (newQuantity > availableStock) {
                throw new IllegalArgumentException(
                        "Requested quantity exceeds available stock. Available stock: " + availableStock
                );
            }

            cartItem.setQuantity(newQuantity);
            cartItem.setUnitPrice(unitPrice);
            cartItemRepo.save(cartItem);
        } else {
            if (request.getQuantity() > availableStock) {
                throw new IllegalArgumentException(
                        "Requested quantity exceeds available stock. Available stock: " + availableStock
                );
            }

            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(productVariant)
                    .quantity(request.getQuantity())
                    .unitPrice(unitPrice)
                    .build();

            cartItemRepo.save(newCartItem);
        }

        List<CartItem> cartItems = cartItemRepo.findByCart_CartId(cart.getCartId());
        return cartMapper.toCartResponse(cart, cartItems);
    }

    @Override
    public CartResponse updateCartItemQuantity(Long cartItemId, CartUpdateQuantityRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);
        Cart cart = getOrCreateActiveCart(user);

        CartItem cartItem = cartItemRepo.findByCartItemIdAndCart_CartId(cartItemId, cart.getCartId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found with id: " + cartItemId)
                );

        VariantInventory inventory = variantInventoryRepo.findById(
                        cartItem.getProductVariant().getProductVariantId()
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for product variant id: "
                                + cartItem.getProductVariant().getProductVariantId()
                ));

        int availableStock = inventory.getStockQty() - inventory.getReservedQty();

        if (request.getQuantity() > availableStock) {
            throw new IllegalArgumentException(
                    "Requested quantity exceeds available stock. Available stock: " + availableStock
            );
        }

        BigDecimal unitPrice = cartItem.getProductVariant().getDiscountPrice() != null
                ? cartItem.getProductVariant().getDiscountPrice()
                : cartItem.getProductVariant().getPrice();

        if (unitPrice == null) {
            throw new IllegalArgumentException("Product variant price is not available");
        }

        cartItem.setQuantity(request.getQuantity());
        cartItem.setUnitPrice(unitPrice);
        cartItemRepo.save(cartItem);

        List<CartItem> cartItems = cartItemRepo.findByCart_CartId(cart.getCartId());
        return cartMapper.toCartResponse(cart, cartItems);
    }

    @Override
    public void removeCartItem(Long cartItemId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Cart cart = getOrCreateActiveCart(user);

        CartItem cartItem = cartItemRepo.findByCartItemIdAndCart_CartId(cartItemId, cart.getCartId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found with id: " + cartItemId)
                );

        cartItemRepo.delete(cartItem);
    }

    @Override
    public void clearCart(String userEmail) {
        User user = getUserByEmail(userEmail);
        Cart cart = getOrCreateActiveCart(user);

        List<CartItem> cartItems = cartItemRepo.findByCart_CartId(cart.getCartId());
        cartItemRepo.deleteAll(cartItems);
    }

    private User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private Cart getOrCreateActiveCart(User user) {
        return cartRepo.findByUser_UserIdAndStatus(user.getUserId(), CartStatus.ACTIVE)
                .orElseGet(() -> cartRepo.save(
                        Cart.builder()
                                .user(user)
                                .status(CartStatus.ACTIVE)
                                .build()
                ));
    }
}