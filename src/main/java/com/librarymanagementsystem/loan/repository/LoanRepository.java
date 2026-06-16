package com.librarymanagementsystem.loan.repository;

import com.librarymanagementsystem.loan.entity.Loan;
import com.librarymanagementsystem.loan.entity.LoanStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    List<Loan> findByMemberMemberId(Long memberId);

    List<Loan> findByBookBookId(Long bookId);

    @Query("select l from Loan l where l.status= :borrowed or l.status = :overdue")
    List<Loan> findByStatusActive(LoanStatus borrowed,LoanStatus overdue);

    @Query("select count(l)> 0 from Loan l where l.book.bookId = :bookId and" +
            " l.member.memberId =:memberId  and " +
            " (l.status = :overdue or l.status=:borrowed)")
    boolean existsByBookBookIdAndMemberMemberIdAndStatus(@Param("bookId") Long bookId,
                                                         @Param("memberId") Long memberId,
                                                         @Param("overdue") LoanStatus overdue, @Param("borrowed") LoanStatus borrowed);

    @Query("select count(l) >= 3 from Loan l where l.member.memberId =:memberId  " +
            "and " +
            "(l.status = :overdue or l.status=:borrowed)")
    boolean existsByMemberMemberIdAndStatus( @Param("memberId") Long memberId, @Param("borrowed") LoanStatus borrowed,@Param("overdue") LoanStatus overdue);
}
