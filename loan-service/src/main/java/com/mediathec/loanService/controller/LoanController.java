package com.mediathec.loanService.controller;

import com.mediathec.loanService.entity.Loan;
import com.mediathec.loanService.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/borrow")
    public ResponseEntity<Loan> borrowBook(@Valid @RequestBody Loan loan) {
        Loan savedLoan = loanService.borrowBook(loan);
        return savedLoan != null
                ? new ResponseEntity<>(savedLoan, HttpStatus.CREATED)
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<Loan> returnBook(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnBook(id);
        return returnedLoan != null
                ? ResponseEntity.ok(returnedLoan)
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        return loan != null ? ResponseEntity.ok(loan) : ResponseEntity.notFound().build();
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Loan>> getLoansByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(loanService.getLoansByMember(memberId));
    }

    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<List<Loan>> getActiveLoansByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(loanService.getActiveLoansByMember(memberId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        return ResponseEntity.ok(loanService.getActiveLoans());
    }

    @GetMapping("/returned")
    public ResponseEntity<List<Loan>> getReturnedLoans() {
        return ResponseEntity.ok(loanService.getReturnedLoans());
    }
}