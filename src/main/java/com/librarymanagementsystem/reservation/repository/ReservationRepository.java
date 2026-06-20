package com.librarymanagementsystem.reservation.repository;

import com.librarymanagementsystem.reservation.entity.Reservation;
import com.librarymanagementsystem.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMemberMemberId(Long memberId);

    List<Reservation> findByBookBookId(Long bookId);

    @Query("select r from Reservation r where r.status= :status")
    List<Reservation> findByStatus(@Param("status") ReservationStatus status);

    Optional<Reservation> findByReservationIdAndStatus(Long reservationId, ReservationStatus reservationStatus);

    boolean existsByMemberMemberIdAndBookBookIdAndStatus(Long memberId, Long bookId, ReservationStatus reservationStatus);

    List<Reservation> findByStatusAndExpiryDateLessThanEqual(ReservationStatus reservationStatus, LocalDate now);


    Optional<Reservation> findTopByBookBookIdAndStatusOrderByReservationDateAscReservationIdAsc(Long bookId, ReservationStatus reservationStatus);

    List<Reservation> findByBookBookIdAndStatusAndExpiryDateLessThanEqual(Long bookId, ReservationStatus reservationStatus,LocalDate now);

    List<Reservation> findByStatusOrderByReservationDateAscReservationIdAsc(ReservationStatus reservationStatus);
}
