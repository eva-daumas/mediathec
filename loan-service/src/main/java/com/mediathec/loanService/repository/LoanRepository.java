package com.mediathec.loanService.repository;

import com.mediathec.loanService.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByMemberId(Long memberId);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByStatus(String status);
}