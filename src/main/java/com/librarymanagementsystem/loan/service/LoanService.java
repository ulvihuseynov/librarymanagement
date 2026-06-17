package com.librarymanagementsystem.loan.service;

import com.librarymanagementsystem.loan.dto.LoanCreateRequest;
import com.librarymanagementsystem.loan.dto.LoanResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface LoanService {
    LoanResponse createLoan(@Valid LoanCreateRequest loanCreateRequest);

    List<LoanResponse> getLoan();

    LoanResponse getLoanById(Long borrowId);

    List<LoanResponse> getLoanByMemberId(Long memberId);

    List<LoanResponse> getLoanByBookId(Long bookId);

    List<LoanResponse> getLoanActive();

    LoanResponse updateLoan(Long borrowId);
}
