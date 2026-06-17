package com.librarymanagementsystem.loan.service;

import com.librarymanagementsystem.loan.dto.LoanCreateRequest;
import com.librarymanagementsystem.loan.dto.LoanResponse;

import java.util.List;

public interface LoanService {
    LoanResponse createLoan( LoanCreateRequest loanCreateRequest);

    List<LoanResponse> getLoan();

    LoanResponse getLoanById(Long loanId);

    List<LoanResponse> getLoanByMemberId(Long memberId);

    List<LoanResponse> getLoanByBookId(Long bookId);

    List<LoanResponse> getLoanActive();

    LoanResponse updateLoan(Long loanId);

    List<LoanResponse> checkOverdue();
}
