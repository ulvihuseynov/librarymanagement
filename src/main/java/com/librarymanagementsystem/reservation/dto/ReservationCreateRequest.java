package com.librarymanagementsystem.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCreateRequest {

    @NotNull(message = "Book id is required")
    private Long bookId;

    @NotNull(message = "Member id is required")
    private Long memberId;
}
