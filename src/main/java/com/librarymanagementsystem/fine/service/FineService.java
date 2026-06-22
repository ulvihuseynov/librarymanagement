package com.librarymanagementsystem.fine.service;

import com.librarymanagementsystem.fine.dto.FineResponse;

import java.util.List;

public interface FineService {
    List<FineResponse> getFineList();

    FineResponse getFineById(Long fineId);

    List<FineResponse> getFineByMember(Long memberId);

    List<FineResponse> getFineByUnPaid();

    FineResponse payFine(Long fineId);

    List<FineResponse> getMyFines();
}
