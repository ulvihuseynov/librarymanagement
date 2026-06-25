package com.librarymanagementsystem.loan.controller;

import com.librarymanagementsystem.common.response.ApiResponse;
import com.librarymanagementsystem.loan.dto.LoanCreateRequest;
import com.librarymanagementsystem.loan.dto.LoanResponse;
import com.librarymanagementsystem.loan.service.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Loan")
@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<ApiResponse<LoanResponse>> createLoan(@Valid @RequestBody LoanCreateRequest loanCreateRequest) {


        LoanResponse loanResponse = loanService.createLoan(loanCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Loan successfully created", loanResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getLoan() {


        List<LoanResponse> loanResponse = loanService.getLoan();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The loans were successfully delivered ", loanResponse));
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<ApiResponse<LoanResponse>> getLoanById(@PathVariable Long loanId) {


        LoanResponse loanResponse = loanService.getLoanById(loanId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The loan was successfully delivered ", loanResponse));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getLoanByMemberId(@PathVariable Long memberId) {


        List<LoanResponse> loanResponse = loanService.getLoanByMemberId(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The loans were successfully delivered ", loanResponse));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getLoanByBookId(@PathVariable Long bookId) {


        List<LoanResponse> loanResponse = loanService.getLoanByBookId(bookId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The loans were successfully delivered ", loanResponse));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getLoanActive() {


        List<LoanResponse> loanResponse = loanService.getLoanActive();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The loans were successfully delivered ", loanResponse));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<ApiResponse<LoanResponse>> updateLoan(@PathVariable Long loanId) {


        LoanResponse loanResponse = loanService.updateLoan(loanId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Loan successfully returned", loanResponse));
    }

    @PutMapping("/check-overdue")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> checkOverdue() {


        List<LoanResponse> loanResponse = loanService.checkOverdue();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Loan successfully returned", loanResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getMyLoans() {


        List<LoanResponse> loanResponse = loanService.getMyLoans();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Loan successfully returned", loanResponse));
    }
}
