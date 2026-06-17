package com.librarymanagementsystem.loan.dto;


import com.librarymanagementsystem.loan.entity.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {


    private Long loanId;
    private Long bookId;
    private Long memberId;
    private String bookTitle;
    private String memberFullName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
