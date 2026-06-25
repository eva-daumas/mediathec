package com.mediathec.webApp.service.client;

import com.mediathec.webApp.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "loan-service", url = "${loan.service.url:http://localhost:8087}")
public interface LoanFeignClient {

    @GetMapping("/loans")
    List<LoanDto> getAllLoans();

    @GetMapping("/loans/member/{memberId}")
    List<LoanDto> getLoansByMemberId(@PathVariable("memberId") Long memberId);


    @PostMapping("/loans")
    LoanDto createLoan(@RequestBody LoanDto loanDto);

    @PutMapping("/loans/return/{id}")
    void returnLoan(@PathVariable("id") Long id);

    @DeleteMapping("/loans/{id}")
    void deleteLoan(@PathVariable("id") Long id);
}