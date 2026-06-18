package com.librarymanagementsystem.fine.repository;

import com.librarymanagementsystem.fine.entity.Fine;
import com.librarymanagementsystem.fine.entity.FineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine,Long> {
    Optional<Fine> findByLoanLoanId(Long loanId);

    List<Fine> findByLoanMemberMemberId(Long memberId);


    List<Fine> findByStatus(FineStatus unPaid);

    boolean existsByLoanMemberMemberIdAndStatus(Long memberId, FineStatus fineStatus);
}
