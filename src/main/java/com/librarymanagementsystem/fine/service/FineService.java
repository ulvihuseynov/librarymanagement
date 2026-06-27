package com.librarymanagementsystem.fine.service;

import com.librarymanagementsystem.common.response.PaginationResponse;
import com.librarymanagementsystem.fine.dto.FineResponse;

import java.util.List;

public interface FineService {
    PaginationResponse<FineResponse> getFineList(Integer pageSize, Integer pageNumber, String sortBy, String sortDirection);

    FineResponse getFineById(Long fineId);

    List<FineResponse> getFineByMember(Long memberId);

    List<FineResponse> getFineByUnPaid();

    FineResponse payFine(Long fineId);

    List<FineResponse> getMyFines();
}
