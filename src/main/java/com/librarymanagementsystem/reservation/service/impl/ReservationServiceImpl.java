package com.librarymanagementsystem.reservation.service.impl;

import com.librarymanagementsystem.book.entity.Book;
import com.librarymanagementsystem.book.repository.BookRepository;
import com.librarymanagementsystem.common.exception.BadRequestException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.fine.entity.FineStatus;
import com.librarymanagementsystem.fine.repository.FineRepository;
import com.librarymanagementsystem.loan.entity.LoanStatus;
import com.librarymanagementsystem.loan.repository.LoanRepository;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.entity.MemberStatus;
import com.librarymanagementsystem.member.repository.MemberRepository;
import com.librarymanagementsystem.reservation.dto.ReservationCreateRequest;
import com.librarymanagementsystem.reservation.dto.ReservationResponse;
import com.librarymanagementsystem.reservation.entity.Reservation;
import com.librarymanagementsystem.reservation.entity.ReservationStatus;
import com.librarymanagementsystem.reservation.mapper.ReservationMapper;
import com.librarymanagementsystem.reservation.repository.ReservationRepository;
import com.librarymanagementsystem.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponse createReservation(ReservationCreateRequest reservationCreateRequest) {

        Reservation reservation = reservationMapper.toEntity(reservationCreateRequest);

        Member member = memberRepository.findById(reservationCreateRequest.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + reservationCreateRequest.getMemberId()));

        Book book = bookRepository.findById(reservationCreateRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + reservationCreateRequest.getBookId()));

        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new BadRequestException("Member is not active and cannot reserve books");
        }
        if (book.getAvailableCopies() > 0) {
            throw new BadRequestException("Book is available. You can borrow it directly.");
        }

        boolean existsReservation = reservationRepository.existsByMemberMemberIdAndBookBookIdAndStatus(member.getMemberId(), book.getBookId(), ReservationStatus.PENDING);

        if (existsReservation) {
            throw new BadRequestException("Member already has an active reservation for this book.");
        }

        boolean existsMemberBook = loanRepository.existsByMemberMemberIdAndBookBookIdAndStatusIn(member.getMemberId(), book.getBookId(), List.of(LoanStatus.BORROWED, LoanStatus.OVERDUE));

        if (existsMemberBook) {
            throw new BadRequestException( "Member already has an active loan for this book");
        }

        boolean memberUnPaid = fineRepository.existsByLoanMemberMemberIdAndStatus(member.getMemberId(), FineStatus.UNPAID);
        if (memberUnPaid) {
            throw new BadRequestException("Member has unpaid fines.");
        }

        reservation.setBook(book);
        reservation.setMember(member);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setExpiryDate(reservation.getReservationDate().plusDays(3));
        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationResponse> getReservationList() {

        List<Reservation> reservationList = reservationRepository.findAll();
        return reservationList.stream().map(reservationMapper::toResponse).toList();
    }

    @Override
    public ReservationResponse getReservationById(Long reservationId) {
        Reservation reservation = getReservation(reservationId);
        return reservationMapper.toResponse(reservation);
    }


    @Override
    public List<ReservationResponse> getReservationMemberId(Long memberId) {
   memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + memberId));

        List<Reservation> reservationList = reservationRepository.findByMemberMemberId(memberId);
        return reservationList.stream().map(reservationMapper::toResponse).toList();
    }

    @Override
    public List<ReservationResponse> getReservationBookId(Long bookId) {


        bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + bookId));
        List<Reservation> reservationList = reservationRepository.findByBookBookId(bookId);
        return reservationList.stream().map(reservationMapper::toResponse).toList();
    }

    @Override
    public List<ReservationResponse> getReservationPending() {

        List<Reservation> reservationList = reservationRepository.findByStatus(ReservationStatus.PENDING);
        return reservationList.stream().map(reservationMapper::toResponse).toList();

    }

    @Transactional
    @Override
    public ReservationResponse reservationCancel(Long reservationId) {

        Reservation reservationsPending = reservationRepository.findByReservationIdAndStatus(reservationId, ReservationStatus.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID " + reservationId));

        reservationsPending.setStatus(ReservationStatus.CANCELLED);

        return reservationMapper.toResponse(reservationsPending);
    }

    @Transactional
    @Override
    public List<ReservationResponse> reservationCheckExpired() {

        List<Reservation> reservationList = reservationRepository.findByStatusAndExpiryDateLessThanEqual(ReservationStatus.PENDING, LocalDate.now());
        reservationList.forEach(reservation -> {
            reservation.setStatus(ReservationStatus.EXPIRED);
        });
        return reservationList.stream().map(reservationMapper::toResponse).toList();
    }


    private Reservation getReservation(Long reservationId) {

        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID "+reservationId));
    }
}
