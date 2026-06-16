package com.librarymanagementsystem.loan.dto;


import com.librarymanagementsystem.loan.entity.LoanStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanCreateRequest {

    @NotBlank(message = "Member id is required")
    private Long memberId;

    @NotBlank(message = "Book id is required")
    private Long  bookId;
}
