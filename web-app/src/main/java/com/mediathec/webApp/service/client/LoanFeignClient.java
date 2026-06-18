package com.mediathec.webApp.service.client;

import com.mediathec.webApp.model.Loan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "loan-service", url = "http://localhost:8087")
public interface LoanFeignClient {

    @GetMapping("/api/getAll")
    List<Loan> getAllLoans();

    @PostMapping("/add")
    Loan createLoan(@RequestBody Loan loan);

    @PutMapping("/api/return/{id}")
    void returnLoan(@PathVariable("id") Long id);

    @DeleteMapping("/api/delete/{id}")
    void deleteLoan(@PathVariable("id") Long id);
}