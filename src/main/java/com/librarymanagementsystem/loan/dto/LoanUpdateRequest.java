package com.librarymanagementsystem.loan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanUpdateRequest {

    @NotBlank(message = "Member id is required")
    private Long memberId;

    @NotBlank(message = "Book id is required")
    private Long  bookId;
}
