package com.sulaks.TechSpark.service;

import com.sulaks.TechSpark.dto.return_request.CreateReturnRequest;
import com.sulaks.TechSpark.dto.return_request.ReturnResponse;

import java.util.List;

public interface ReturnService {

    ReturnResponse createReturnRequest(CreateReturnRequest request, String userEmail);

    List<ReturnResponse> getMyReturns(String userEmail);

    ReturnResponse getMyReturnById(Long returnId, String userEmail);

    List<ReturnResponse> getAllReturns();

    ReturnResponse getReturnById(Long returnId);

    ReturnResponse approveReturn(Long returnId, String adminEmail);

    ReturnResponse rejectReturn(Long returnId, String adminEmail);

    ReturnResponse markPickedUp(Long returnId, String adminEmail);

    ReturnResponse markReceived(Long returnId, String adminEmail);

    ReturnResponse closeReturn(Long returnId, String adminEmail);
}