package com.librarymanagementsystem.fine.dto;

import com.librarymanagementsystem.fine.entity.FineStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FineResponse {


    private Long fineId;
    private Long  loanId;
    private Long bookId;
    private Long memberId;
    private String bookTitle;
    private String memberFullName;
    private Integer amount;
    private Integer daysLate;
    private FineStatus status;
    private LocalDate calculatedAt;
    private Integer paidAt;
}
