package com.mediathec.webApp.service;

import com.mediathec.webApp.entity.Loan;
import com.mediathec.webApp.service.client.LoanFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanFeignClient loanFeignClient;

    public List<Loan> getAllLoans() {
        return loanFeignClient.getAllLoans();
    }

    public Loan createLoan(Loan loan) {
        return loanFeignClient.createLoan(loan);
    }

    public void returnLoan(Long id) {
        loanFeignClient.returnLoan(id);
    }

    public void deleteLoan(Long id) {
        loanFeignClient.deleteLoan(id);
    }

    // ============================================================
    //             VERSION AVEC LOGS POUR DÉBOGUER
    // ============================================================
    public List<Loan> getLoansByMemberId(Long memberId) {
        try {
            System.out.println("========================================");
            System.out.println("LoanService.getLoansByMemberId()");
            System.out.println("memberId reçu : " + memberId);

            List<Loan> loans = loanFeignClient.getLoansByMemberId(memberId);

            System.out.println("🔍 Emprunts reçus du FeignClient : " + (loans != null ? loans.size() : 0));
            if (loans != null && !loans.isEmpty()) {
                for (Loan loan : loans) {
                    System.out.println("   - ID: " + loan.getId() + ", memberId: " + loan.getMemberId() + ", status: " + loan.getStatus());
                }
            }
            System.out.println("========================================");

            return loans;
        } catch (Exception e) {
            System.err.println(" Erreur FeignClient: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}