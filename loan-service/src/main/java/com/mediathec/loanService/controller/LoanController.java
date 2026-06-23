package com.mediathec.loanService.controller;

import com.mediathec.loanService.entity.Loan;
import com.mediathec.loanService.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // ============================================
    // GET - Récupérer tous les emprunts
    // ============================================
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // ============================================
    // GET - Récupérer un emprunt par ID
    // ============================================
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        return ResponseEntity.ok(loan);
    }

    // ============================================
    // GET - Récupérer les emprunts d'un membres
    // ============================================
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Loan>> getLoansByMemberId(@PathVariable Long memberId) {
        List<Loan> loans = loanService.getLoansByMemberId(memberId);
        return ResponseEntity.ok(loans);
    }

    // ============================================
    // POST - Créer un emprunt
    // ============================================
    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        Loan createdLoan = loanService.createLoan(loan);
        return ResponseEntity.ok(createdLoan);
    }

    // ============================================
    // PUT - Retourner un emprunt
    // ============================================
    @PutMapping("/return/{id}")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnLoan(id);
        if (returnedLoan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(returnedLoan);
    }

    // ============================================
    // DELETE - Supprimer un emprunt
    // ============================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}