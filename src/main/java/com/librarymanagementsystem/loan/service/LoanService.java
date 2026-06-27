package com.librarymanagementsystem.loan.service;

import com.librarymanagementsystem.common.response.PaginationResponse;
import com.librarymanagementsystem.loan.dto.LoanCreateRequest;
import com.librarymanagementsystem.loan.dto.LoanResponse;

import java.util.List;

public interface LoanService {
    LoanResponse createLoan( LoanCreateRequest loanCreateRequest);

    PaginationResponse<LoanResponse> getLoan(Integer pageSize,Integer pageNumber,String sortBy,String sortDirection);

    LoanResponse getLoanById(Long loanId);

    List<LoanResponse> getLoanByMemberId(Long memberId);

    List<LoanResponse> getLoanByBookId(Long bookId);

    List<LoanResponse> getLoanActive();

    LoanResponse updateLoan(Long loanId);

    List<LoanResponse> checkOverdue();

    List<LoanResponse> getMyLoans();
}
