package com.librarymanagementsystem.loan.service.impl;

import com.librarymanagementsystem.book.entity.Book;
import com.librarymanagementsystem.book.repository.BookRepository;
import com.librarymanagementsystem.common.exception.BadRequestException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.loan.LoanMapper;
import com.librarymanagementsystem.loan.dto.LoanCreateRequest;
import com.librarymanagementsystem.loan.dto.LoanResponse;
import com.librarymanagementsystem.loan.dto.LoanUpdateRequest;
import com.librarymanagementsystem.loan.entity.Loan;
import com.librarymanagementsystem.loan.entity.LoanStatus;
import com.librarymanagementsystem.loan.repository.LoanRepository;
import com.librarymanagementsystem.loan.service.LoanService;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.entity.MemberStatus;
import com.librarymanagementsystem.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanMapper loanMapper;

    @Transactional
    @Override
    public LoanResponse createLoan(LoanCreateRequest loanCreateRequest) {

        Loan loan = loanMapper.toEntity(loanCreateRequest);

        Book book = bookRepository.findById(loanCreateRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + loanCreateRequest.getBookId()));

        Member member = memberRepository.findById(loanCreateRequest.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + loanCreateRequest.getMemberId()));


        boolean isNotAvailable=loanRepository.existsByBookBookIdAndMemberMemberIdAndStatus(
                loanCreateRequest.getBookId(),loanCreateRequest.getMemberId(),LoanStatus.BORROWED,LoanStatus.OVERDUE);

       boolean memberActiveBookCount=loanRepository.existsByMemberMemberIdAndStatus(loanCreateRequest.getMemberId(),
               LoanStatus.BORROWED,LoanStatus.OVERDUE);


        if (isNotAvailable){
            throw new BadRequestException("Member already borrowed this book and has not returned it yet");
        }

        if (member.getStatus()!= MemberStatus.ACTIVE){
            throw new BadRequestException("Member is not active and cannot borrow books");
        }

        if (book.getAvailableCopies()==0){
            throw new BadRequestException("Book is not available for borrowing");
        }


        book.setAvailableCopies(book.getAvailableCopies()-1);

        bookRepository.save(book);

        loan.setBook(book);

        loan.setBorrowDate(LocalDate.now());
        loan.setDueDate(loan.getBorrowDate().plusDays(14));

        loan.setMember(member);

        if (memberActiveBookCount){
            throw new BadRequestException("Member cannot borrow more than 3 books at the same time");
        }
        loan.setStatus(LoanStatus.BORROWED);

        return loanMapper.toResponse(loanRepository.save(loan));
    }

    @Override
    public List<LoanResponse> getLoan() {

        List<Loan> loanList = loanRepository.findAll();
        return loanList.stream().map( loanMapper::toResponse).toList();
    }

    @Override
    public LoanResponse getLoanById(Long borrowId) {

        Loan loan = getLoan(borrowId);
        return loanMapper.toResponse(loan);
    }

    @Override
    public List<LoanResponse> getLoanByMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + memberId));

        List<Loan> loanList= loanRepository.findByMemberMemberId(memberId);
        return loanList.stream().map( loanMapper::toResponse).toList();

    }

    @Override
    public List<LoanResponse> getLoanByBookId(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + bookId));

        List<Loan> loanList= loanRepository.findByBookBookId(bookId);

        return loanList.stream().map( loanMapper::toResponse).toList();

    }

    @Override
    public List<LoanResponse> getLoanActive() {

        LoanStatus borrowed = LoanStatus.BORROWED;
        LoanStatus overdue = LoanStatus.OVERDUE;
        List<Loan> loanList= loanRepository.findByStatusActive(borrowed,overdue);
        return loanList.stream().map( loanMapper::toResponse).toList();
    }

    @Override
    public LoanResponse updateLoan(LoanUpdateRequest loanUpdateRequest,Long borrowId) {

        Loan loanFromDb = getLoan(borrowId);

        Book book = loanFromDb.getBook();


        if (loanFromDb.getStatus()==LoanStatus.RETURNED){
            throw new BadRequestException("This book has already been returned");
        }

        loanFromDb.setStatus(LoanStatus.RETURNED);
        loanFromDb.setReturnDate(LocalDate.now());

        book.setAvailableCopies(book.getAvailableCopies()+1);

        bookRepository.save(book);
        return loanMapper.toResponse(loanRepository.save(loanFromDb));
    }

    private Loan getLoan (Long id){

        return loanRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Loan not found with ID "+id));
    }
}
