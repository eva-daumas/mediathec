// Dans com.mediathec.webApp.service.client.LoanFeignClient.java
package com.mediathec.webApp.service.client;

import com.mediathec.webApp.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "loan-service", url = "http://localhost:8087")  // ← Vérifier le port
public interface LoanFeignClient {

    @GetMapping("/api/loans")
    List<LoanDto> getAllLoans();

    @GetMapping("/api/loans/member/{memberId}")
    List<LoanDto> getLoansByMemberId(@PathVariable("memberId") Long memberId);

    @PostMapping("/api/loans")
    LoanDto createLoan(@RequestBody LoanDto loanDto);

    @PutMapping("/api/loans/return/{id}")
    void returnLoan(@PathVariable("id") Long id);

    @DeleteMapping("/api/loans/{id}")
    void deleteLoan(@PathVariable("id") Long id);
}