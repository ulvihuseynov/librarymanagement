package com.librarymanagementsystem.loan.dto;


import com.librarymanagementsystem.book.dto.BookResponse;
import com.librarymanagementsystem.loan.entity.LoanStatus;
import com.librarymanagementsystem.member.dto.MemberResponse;
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
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
    private BookResponse book;
    private MemberResponse member;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
