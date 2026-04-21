package com.sulaks.TechSpark.service_impl;

import com.sulaks.TechSpark.dto.review.ReviewRequest;
import com.sulaks.TechSpark.dto.review.ReviewResponse;
import com.sulaks.TechSpark.enums.OrderStatus;
import com.sulaks.TechSpark.exception.ResourceAlreadyExistsException;
import com.sulaks.TechSpark.exception.ResourceNotFoundException;
import com.sulaks.TechSpark.mapper.ReviewMapper;
import com.sulaks.TechSpark.models.Order;
import com.sulaks.TechSpark.models.OrderItem;
import com.sulaks.TechSpark.models.Product;
import com.sulaks.TechSpark.models.Review;
import com.sulaks.TechSpark.models.User;
import com.sulaks.TechSpark.repository.OrderItemRepo;
import com.sulaks.TechSpark.repository.OrderRepo;
import com.sulaks.TechSpark.repository.ProductRepo;
import com.sulaks.TechSpark.repository.ReviewRepo;
import com.sulaks.TechSpark.repository.UserRepo;
import com.sulaks.TechSpark.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponse createReview(ReviewRequest request, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + userEmail)
                );

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + request.getProductId())
                );

        boolean alreadyReviewed = reviewRepo.existsByUser_UserIdAndProduct_ProductId(
                user.getUserId(),
                product.getProductId()
        );

        if (alreadyReviewed) {
            throw new ResourceAlreadyExistsException("You have already reviewed this product");
        }

        if (!hasPurchasedDeliveredProduct(user.getUserId(), product.getProductId())) {
            throw new IllegalArgumentException("You can review only delivered products that you have purchased");
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .isApproved(false)
                .build();

        Review savedReview = reviewRepo.save(review);
        return reviewMapper.toResponse(savedReview);
    }

    @Override
    public List<ReviewResponse> getApprovedReviewsByProduct(Long productId) {
        if (!productRepo.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        return reviewRepo.findByProduct_ProductIdAndIsApprovedTrueOrderByReviewIdDesc(productId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    public List<ReviewResponse> getMyReviews(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + userEmail)
                );

        return reviewRepo.findByUser_UserIdOrderByReviewIdDesc(user.getUserId())
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepo.findAll()
                .stream()
                .sorted((a, b) -> b.getReviewId().compareTo(a.getReviewId()))
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    public List<ReviewResponse> getPendingReviews() {
        return reviewRepo.findByIsApprovedFalseOrderByReviewIdDesc()
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    public ReviewResponse approveReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found with id: " + reviewId)
                );

        review.setApproved(true);
        Review updatedReview = reviewRepo.save(review);

        return reviewMapper.toResponse(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found with id: " + reviewId)
                );

        reviewRepo.delete(review);
    }

    private boolean hasPurchasedDeliveredProduct(Long userId, Long productId) {
        List<Order> deliveredOrders = orderRepo.findByUser_UserIdOrderByOrderIdDesc(userId)
                .stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .toList();

        for (Order order : deliveredOrders) {
            List<OrderItem> orderItems = orderItemRepo.findByOrder_OrderId(order.getOrderId());

            for (OrderItem orderItem : orderItems) {
                if (orderItem.getProduct_variant() != null
                        && orderItem.getProduct_variant().getProduct() != null
                        && orderItem.getProduct_variant().getProduct().getProductId().equals(productId)) {
                    return true;
                }
            }
        }

        return false;
    }
}