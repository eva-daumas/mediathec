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
}