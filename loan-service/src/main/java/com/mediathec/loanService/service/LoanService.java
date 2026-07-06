package com.mediathec.loanService.service;

import com.mediathec.loanService.dto.LoanDto;
import com.mediathec.loanService.entity.Loan;
import com.mediathec.loanService.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));
    }

    // Récupérer les emprunts d'un membre (UN SEUL)
    public List<Loan> getLoansByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    public Loan createLoan(Loan loan) {
        loan.setLoanDate(LocalDateTime.now());
        loan.setStatus("BORROWED");
        return loanRepository.save(loan);
    }

    public Loan createLoan(LoanDto loanDto) {
        Loan loan = new Loan();
        loan.setMemberId(loanDto.getMemberId());
        loan.setBookId(loanDto.getBookId());
        loan.setGameId(loanDto.getGameId());
        loan.setMovieId(loanDto.getMovieId());
        loan.setStatus("BORROWED");
        loan.setLoanDate(LocalDateTime.now());
        return loanRepository.save(loan);
    }

    public Loan returnLoan(Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null) {
            loan.setStatus("RETURNED");
            loan.setReturnDate(LocalDateTime.now());
            return loanRepository.save(loan);
        }
        return null;
    }

    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }
}