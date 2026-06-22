package com.librarymanagementsystem.reservation.controller;

import com.librarymanagementsystem.common.response.ApiResponse;
import com.librarymanagementsystem.reservation.dto.ReservationCreateRequest;
import com.librarymanagementsystem.reservation.dto.ReservationResponse;
import com.librarymanagementsystem.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;


    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(@Valid @RequestBody ReservationCreateRequest reservationCreateRequest) {

        ReservationResponse reservationResponse = reservationService.createReservation(reservationCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("The reservation successfully created", reservationResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationList() {

        List<ReservationResponse> reservationResponse = reservationService.getReservationList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The reservations were successfully delivered ", reservationResponse));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(@PathVariable Long reservationId) {

        ReservationResponse reservationResponse = reservationService.getReservationById(reservationId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The reservation was successfully delivered", reservationResponse));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationMemberId(@PathVariable Long memberId) {

        List<ReservationResponse> reservationResponse = reservationService.getReservationMemberId(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The reservation was successfully delivered", reservationResponse));
    }


    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationBookId(@PathVariable Long bookId) {

        List<ReservationResponse> reservationResponse = reservationService.getReservationBookId(bookId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The reservation was successfully delivered", reservationResponse));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationPending() {

        List<ReservationResponse> reservationResponse = reservationService.getReservationPending();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The reservation was successfully delivered", reservationResponse));
    }

    @GetMapping("/pending/queue/{bookId}")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationPendingQueue(@PathVariable Long bookId) {

        List<ReservationResponse> reservationResponse = reservationService.getReservationPendingQueue(bookId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The reservation was successfully delivered", reservationResponse));
    }

    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<ApiResponse<ReservationResponse>> reservationCancel(@PathVariable Long reservationId) {

        ReservationResponse reservationResponse = reservationService.reservationCancel(reservationId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The reservation was successfully cancelled", reservationResponse));
    }

    @PutMapping("/check-expired")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> reservationCheckExpired() {

        List<ReservationResponse> reservationResponse = reservationService.reservationCheckExpired();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Expired reservations were successfully updated", reservationResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getMyReservations(){

        List<ReservationResponse> reservationResponse= reservationService.getMyReservations();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The reservations were successfully retrieved",reservationResponse));
    }
}


