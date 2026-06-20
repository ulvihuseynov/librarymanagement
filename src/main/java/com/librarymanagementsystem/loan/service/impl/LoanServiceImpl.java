package com.librarymanagementsystem.loan.service.impl;

import com.librarymanagementsystem.book.entity.Book;
import com.librarymanagementsystem.book.repository.BookRepository;
import com.librarymanagementsystem.common.exception.BadRequestException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.fine.entity.Fine;
import com.librarymanagementsystem.fine.entity.FineStatus;
import com.librarymanagementsystem.fine.repository.FineRepository;
import com.librarymanagementsystem.loan.dto.LoanCreateRequest;
import com.librarymanagementsystem.loan.dto.LoanResponse;
import com.librarymanagementsystem.loan.entity.Loan;
import com.librarymanagementsystem.loan.entity.LoanStatus;
import com.librarymanagementsystem.loan.mapper.LoanMapper;
import com.librarymanagementsystem.loan.repository.LoanRepository;
import com.librarymanagementsystem.loan.service.LoanService;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.entity.MemberStatus;
import com.librarymanagementsystem.member.repository.MemberRepository;
import com.librarymanagementsystem.reservation.entity.Reservation;
import com.librarymanagementsystem.reservation.entity.ReservationStatus;
import com.librarymanagementsystem.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final FineRepository fineRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final LoanMapper loanMapper;

    @Transactional
    @Override
    public LoanResponse createLoan(LoanCreateRequest loanCreateRequest) {

        Loan loan = loanMapper.toEntity(loanCreateRequest);

        Book book = bookRepository.findById(loanCreateRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + loanCreateRequest.getBookId()));

        Member member = memberRepository.findById(loanCreateRequest.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + loanCreateRequest.getMemberId()));

        unPaidFineIsMemberValidation(member.getMemberId());
        memberIsActiveStatus(member.getStatus());
        duplicateMemberBook(loanCreateRequest.getBookId(), loanCreateRequest.getMemberId());
        isNotAvailableValidation(book.getAvailableCopies());
        memberActiveBookCountLimitValidation(loanCreateRequest.getMemberId());

        List<Reservation> expiryReservation = reservationRepository
                .findByBookBookIdAndStatusAndExpiryDateLessThanEqual(book.getBookId(), ReservationStatus.PENDING,LocalDate.now());

        expiryReservation.forEach(
                expiryReservationStatus->expiryReservationStatus.setStatus(ReservationStatus.EXPIRED)
        );
        Optional<Reservation> activeOldestReservation = reservationRepository.
                findTopByBookBookIdAndStatusOrderByReservationDateAscReservationIdAsc(book.getBookId(), ReservationStatus.PENDING);

        if (activeOldestReservation.isPresent()) {

            if (!Objects.equals(activeOldestReservation.get().getMember().getMemberId(), member.getMemberId())) {
                throw new BadRequestException("Another member is first in line to reserve the book.");
            }
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        bookRepository.save(book);

        loan.setBook(book);
        loan.setBorrowDate(LocalDate.now());
        loan.setDueDate(loan.getBorrowDate().plusDays(1));
        loan.setMember(member);
        loan.setStatus(LoanStatus.BORROWED);

        activeOldestReservation.ifPresent(reservation -> reservation.setStatus(ReservationStatus.FULFILLED));

        return loanMapper.toResponse(loanRepository.save(loan));
    }




    @Override
    public List<LoanResponse> getLoan() {

        List<Loan> loanList = loanRepository.findAll();
        return loanList.stream().map(loanMapper::toResponse).toList();
    }

    @Override
    public LoanResponse getLoanById(Long borrowId) {

        Loan loan = getLoan(borrowId);
        return loanMapper.toResponse(loan);
    }

    @Override
    public List<LoanResponse> getLoanByMemberId(Long memberId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + memberId));

        List<Loan> loanList = loanRepository.findByMemberMemberId(memberId);
        return loanList.stream().map(loanMapper::toResponse).toList();

    }

    @Override
    public List<LoanResponse> getLoanByBookId(Long bookId) {

        bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + bookId));

        List<Loan> loanList = loanRepository.findByBookBookId(bookId);

        return loanList.stream().map(loanMapper::toResponse).toList();

    }

    @Override
    public List<LoanResponse> getLoanActive() {

        LoanStatus borrowed = LoanStatus.BORROWED;
        LoanStatus overdue = LoanStatus.OVERDUE;
        List<Loan> loanList = loanRepository.findByStatusActive(borrowed, overdue);
        return loanList.stream().map(loanMapper::toResponse).toList();
    }

    @Transactional
    @Override
    public LoanResponse updateLoan(Long loanId) {

        Loan loanFromDb = getLoan(loanId);

        Book book = loanFromDb.getBook();


        if (loanFromDb.getStatus() == LoanStatus.RETURNED) {
            throw new BadRequestException("This book has already been returned");
        }

        loanFromDb.setStatus(LoanStatus.RETURNED);

        loanFromDb.setReturnDate(LocalDate.now());

        book.setAvailableCopies(book.getAvailableCopies() + 1);


        boolean isBefore = loanFromDb.getDueDate().isBefore(loanFromDb.getReturnDate());

        if (isBefore) {
            Fine fine = fineRepository.findByLoanLoanId(loanFromDb.getLoanId()).
                    orElseGet(Fine::new);
            Integer daysLate = (int) ChronoUnit.DAYS.between(loanFromDb.getDueDate(), loanFromDb.getReturnDate());

            fine.setLoan(loanFromDb);
            fine.setDaysLate(daysLate);
            fine.setCalculatedAt(LocalDate.now());
            fine.setAmount(daysLate);
            if (fine.getStatus() == null) {
                fine.setStatus(FineStatus.UNPAID);
                fine.setPaidAt(null);
            }
            fineRepository.save(fine);
        }
        bookRepository.save(book);
        return loanMapper.toResponse(loanRepository.save(loanFromDb));
    }

    @Transactional
    @Override
    public List<LoanResponse> checkOverdue() {


        LocalDate today = LocalDate.now();

        List<Loan> overDueLoans = loanRepository.findByStatusInAndReturnDateIsNullAndDueDateBefore(List.of(LoanStatus.BORROWED, LoanStatus.OVERDUE), today);

        overDueLoans.forEach(loan -> {

            Integer daysLate = (int) ChronoUnit.DAYS.between(loan.getDueDate(), today);

            loan.setStatus(LoanStatus.OVERDUE);

            Fine fine = fineRepository.findByLoanLoanId(loan.getLoanId())
                    .orElseGet(Fine::new);

            fine.setDaysLate(daysLate);
            fine.setAmount(daysLate);
            fine.setCalculatedAt(today);
            if (fine.getStatus() == null) {
                fine.setStatus(FineStatus.UNPAID);
                fine.setPaidAt(null);

            }

            fine.setLoan(loan);

            fineRepository.save(fine);

            loanRepository.save(loan);
        });


        return overDueLoans.stream().map(loanMapper::toResponse).toList();
    }

    private void unPaidFineIsMemberValidation(Long memberId) {

        boolean unPaidFineIsMember=fineRepository.existsByLoanMemberMemberIdAndStatus(memberId,FineStatus.UNPAID);

        if (unPaidFineIsMember){
            throw new BadRequestException("Member has unpaid fines.");
        }

    }

    private void memberIsActiveStatus(MemberStatus status) {

        if (status != MemberStatus.ACTIVE) {
            throw new BadRequestException("Member is not active and cannot borrow books");
        }
    }

    private void duplicateMemberBook(Long bookId, Long memberId) {

        boolean isNotAvailable = loanRepository.existsByBookBookIdAndMemberMemberIdAndStatus(bookId, memberId, LoanStatus.BORROWED, LoanStatus.OVERDUE);

        if (isNotAvailable) {
            throw new BadRequestException("Member already borrowed this book and has not returned it yet");
        }
    }

    private void isNotAvailableValidation(Integer availableCopies) {

        if (availableCopies <= 0) {
            throw new BadRequestException("Book is not available for borrowing");
        }
    }

    private void memberActiveBookCountLimitValidation(Long memberId) {

        boolean memberActiveBookCount = loanRepository.existsByMemberMemberIdAndStatus(memberId,
                LoanStatus.BORROWED, LoanStatus.OVERDUE);

        if (memberActiveBookCount) {
            throw new BadRequestException("Member cannot borrow more than 3 books at the same time");
        }
    }

    private Loan getLoan(Long id) {

        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID " + id));
    }
}
