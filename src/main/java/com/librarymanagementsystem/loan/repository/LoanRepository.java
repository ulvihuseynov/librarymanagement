package com.librarymanagementsystem.loan.repository;

import com.librarymanagementsystem.loan.entity.Loan;
import com.librarymanagementsystem.loan.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    List<Loan> findByMemberMemberId(Long memberId);

    List<Loan> findByBookBookId(Long bookId);

    @Query("select l.borrowed,l.overdue from Loan l where l.status= :borrowed or l.status = :overdue")
    List<Loan> findByStatusActive(LoanStatus borrowed,LoanStatus overdue);

//    List<Loan> findByStatusBorrowedAndOverdue();
}
