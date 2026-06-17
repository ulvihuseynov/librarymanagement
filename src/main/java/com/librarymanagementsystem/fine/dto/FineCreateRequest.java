package com.librarymanagementsystem.fine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FineCreateRequest {


    @NotNull(message = "Loan id is required")
    private Long loanId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be at least 0")
    private Integer amount;


    @NotNull(message = "Days late is required")
    @Min(value = 0, message = "Days late must be at least 0")
    private Integer daysLate;


    @NotNull(message = "Calculated at is required")
    @Min(value = 0, message = "Calculated at must be at least 0")
    private Integer calculatedAt;

    @NotNull(message = "Paid at is required")
    @Min(value = 0, message = "Paid at must be at least 0")
    private Integer paidAt;
}
