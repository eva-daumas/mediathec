package com.mediathec.loanService.service;

import com.mediathec.loanService.entity.Loan;
import com.mediathec.loanService.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    public Loan borrowBook(Loan loan) {
        // Vérifier si le livre est déjà emprunté
        if (loanRepository.existsByBookIdAndStatus(loan.getBookId(), "BORROWED")) {
            return null;  // Livre déjà emprunté
        }
        loan.setStatus("BORROWED");
        loan.setLoanDate(LocalDateTime.now());
        return loanRepository.save(loan);
    }

    public Loan returnBook(Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null && "BORROWED".equals(loan.getStatus())) {
            loan.setStatus("RETURNED");
            loan.setReturnDate(LocalDateTime.now());
            return loanRepository.save(loan);
        }
        return null;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    public List<Loan> getLoansByMember(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    public List<Loan> getActiveLoansByMember(Long memberId) {
        return loanRepository.findByMemberIdAndStatus(memberId, "BORROWED");
    }

    public List<Loan> getActiveLoans() {
        return loanRepository.findByStatus("BORROWED");
    }

    public List<Loan> getReturnedLoans() {
        return loanRepository.findByStatus("RETURNED");
    }
}