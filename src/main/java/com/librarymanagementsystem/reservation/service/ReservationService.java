package com.librarymanagementsystem.reservation.service;

import com.librarymanagementsystem.reservation.dto.ReservationCreateRequest;
import com.librarymanagementsystem.reservation.dto.ReservationResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(@Valid ReservationCreateRequest reservationCreateRequest);

    List<ReservationResponse> getReservationList();

    ReservationResponse getReservationById(Long reservationId);

    List<ReservationResponse> getReservationMemberId(Long memberId);

    List<ReservationResponse> getReservationBookId(Long bookId);

    List<ReservationResponse> getReservationPending();

    ReservationResponse reservationCancel(Long reservationId);

    List<ReservationResponse> reservationCheckExpired();

    List<ReservationResponse> getReservationPendingQueue(Long bookId);
}
