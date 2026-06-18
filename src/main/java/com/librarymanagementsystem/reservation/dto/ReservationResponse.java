package com.librarymanagementsystem.reservation.dto;


import com.librarymanagementsystem.reservation.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {

    private Long reservationId;
    private Long memberId;
    private Long bookId;
    private String memberFullName;
    private String bookTitle;
    private LocalDate reservationDate;
    private LocalDate expiryDate;
    private ReservationStatus status;
}
