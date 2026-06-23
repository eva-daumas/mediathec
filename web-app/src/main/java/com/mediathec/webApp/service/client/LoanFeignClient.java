package com.mediathec.webApp.service.client;

import com.mediathec.webApp.model.Loan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "loan-service", url = "${loan.service.url:http://localhost:8087}")
public interface LoanFeignClient {

    @GetMapping("/loans")
    List<Loan> getAllLoans();

    @GetMapping("/loans/member/{memberId}")
    List<Loan> getLoansByMemberId(@PathVariable("memberId") Long memberId);


    @PostMapping("/loans")
    Loan createLoan(@RequestBody Loan loan);

    @PutMapping("/loans/return/{id}")
    void returnLoan(@PathVariable("id") Long id);

    @DeleteMapping("/loans/{id}")
    void deleteLoan(@PathVariable("id") Long id);
}