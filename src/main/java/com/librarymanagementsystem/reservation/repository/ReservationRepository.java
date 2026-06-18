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
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("select count(r) > 0 from Reservation r where r.member.memberId= :memberId and r.status=:status")
    boolean existsByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status")ReservationStatus status);

    List<Reservation> findByMemberMemberId(Long memberId);

    List<Reservation> findByBookBookId(Long bookId);

    @Query("select r from Reservation r where r.status= :status")
    List<Reservation> findByStatus(@Param("status") ReservationStatus status);

    Optional<Reservation> findByReservationIdAndStatus(Long reservationId, ReservationStatus reservationStatus);

    List<Reservation> findByStatusAndExpiryDateBefore(ReservationStatus reservationStatus, LocalDate today);
}
