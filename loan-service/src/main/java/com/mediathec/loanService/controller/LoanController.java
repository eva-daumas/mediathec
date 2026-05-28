package com.mediathec.loanService.controller;

import com.mediathec.loanService.entity.Loan;
import com.mediathec.loanService.service.LoanService;
import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class LoanController {

    private final LoanService loanService;

    // Constructeur
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/add")
    public ResponseEntity<Loan> addLoan(@Valid @RequestBody Loan loan) {
        try {
            Loan savedLoan = loanService.save(loan);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLoan);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/api/findByMemberId/{memberId}")
    public List<Loan> findLoansByMemberId(@PathVariable Long memberId) {
        return loanService.findByMemberId(memberId);
    }

    @GetMapping("/api/findByBookId/{bookId}")
    public List<Loan> findLoansByBookId(@PathVariable Long bookId) {
        return loanService.findByBookId(bookId);
    }

    @GetMapping("/api/getAll")
    public List<Loan> getAllLoans() {
        return loanService.findAll();
    }

    @GetMapping("/api/findById/{id}")
    public Loan findLoanById(@PathVariable Long id) {
        return loanService.findById(id);
    }

    @PutMapping("/api/return/{id}")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnLoan(id);
        if (returnedLoan != null) {
            return ResponseEntity.ok(returnedLoan);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/delete/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}