package com.mediathec.webApp.service;

import com.mediathec.webApp.model.Loan;
import com.mediathec.webApp.service.client.LoanFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //  RETOURNER UN EMPRUNT
    public void returnLoan(Long id) {
        // Appeler loan-service pour retourner l'emprunt
        loanFeignClient.returnLoan(id);
    }

    //  SUPPRIMER UN EMPRUNT
    public void deleteLoan(Long id) {
        loanFeignClient.deleteLoan(id);
    }
}