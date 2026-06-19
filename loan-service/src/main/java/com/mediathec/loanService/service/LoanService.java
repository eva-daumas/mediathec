package com.mediathec.loanService.service;

import com.mediathec.loanService.client.BookFeignClient;
import com.mediathec.loanService.client.MemberFeignClient;
import com.mediathec.loanService.entity.Loan;
import com.mediathec.loanService.repository.LoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class LoanService {

    private final LoanRepository loanRepository;


    @Autowired  // ← AJOUTE CES INJECTIONS
    private MemberFeignClient memberFeignClient;

    @Autowired
    private BookFeignClient bookFeignClient;

    // Constructeur
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan save(Loan loan) {
        loan.setLoanDate(LocalDateTime.now());
        loan.setStatus("BORROWED");
        return loanRepository.save(loan);
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public Loan update(Loan newLoanData) {
        return loanRepository.save(newLoanData);
    }

    public void delete(Long id) {
        loanRepository.deleteById(id);
    }

    public Loan findById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    public List<Loan> findByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    public List<Loan> findByBookId(Long bookId) {
        return loanRepository.findByBookId(bookId);
    }

    public Loan returnLoan(Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null && "BORROWED".equals(loan.getStatus())) {
            loan.setStatus("RETURNED");
            loan.setReturnDate(LocalDateTime.now());
            return loanRepository.save(loan);
        }
        return null;
    }
}