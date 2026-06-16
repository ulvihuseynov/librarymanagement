package com.librarymanagementsystem.loan.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanCreateRequest {

    @NotNull(message = "Member id is required")
    private Long memberId;

    @NotNull(message = "Book id is required")
    private Long  bookId;
}
