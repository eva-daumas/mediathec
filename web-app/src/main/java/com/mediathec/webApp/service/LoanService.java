package com.mediathec.webApp.service;

import com.mediathec.webApp.model.Loan;
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


    public List<Loan> getLoansByMemberId(Long memberId) {
        try {
            return loanFeignClient.getLoansByMemberId(memberId);
        } catch (Exception e) {
            System.err.println("❌ Erreur FeignClient: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}