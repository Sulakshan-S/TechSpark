package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.refund.CreateRefundRequest;
import com.sulaks.TechSpark.dto.refund.RefundResponse;

import java.util.List;

public interface RefundService {

    RefundResponse createRefund(CreateRefundRequest request, String adminEmail);

    List<RefundResponse> getAllRefunds();

    RefundResponse getRefundById(Long refundId);

    List<RefundResponse> getMyRefunds(String userEmail);

    RefundResponse getMyRefundById(Long refundId, String userEmail);

    RefundResponse getRefundByReturnId(Long returnId);
}