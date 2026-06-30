package com.mediathec.loanService.controller;

import com.mediathec.loanService.dto.LoanDto;  // ← Import du DTO
import com.mediathec.loanService.entity.Loan;
import com.mediathec.loanService.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loans")
@AllArgsConstructor
public class LoanController {

    private LoanService loanService;

    // ============================================
    // GET - Récupérer tous les emprunts
    // ============================================
    @GetMapping
    public ResponseEntity<List<LoanDto>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        List<LoanDto> loanDtos = loans.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loanDtos);
    }

    // ============================================
    // GET - Récupérer un emprunt par son ID
    // ============================================
    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        return ResponseEntity.ok(convertToDto(loan));
    }

    // ============================================
    // GET - Récupérer tous les emprunts d'un membre
    // ============================================
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoanDto>> getLoansByMemberId(@PathVariable Long memberId) {
        List<Loan> loans = loanService.getLoansByMemberId(memberId);
        List<LoanDto> loanDtos = loans.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loanDtos);
    }

    // ============================================
    // POST - Créer un nouvel emprunt
    // ============================================
    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@RequestBody LoanDto loanDto) {
        // Convertir le DTO en entité Loan
        Loan loan = new Loan();
        loan.setMemberId(loanDto.getMemberId());
        loan.setBookId(loanDto.getBookId());
        loan.setStatus("BORROWED");
        loan.setLoanDate(LocalDateTime.now());

        // Sauvegarder l'emprunt
        Loan createdLoan = loanService.createLoan(loan);

        // Retourner l'emprunt créé sous forme de DTO
        return ResponseEntity.ok(convertToDto(createdLoan));
    }

    // ============================================
    // PUT - Retourner un emprunt (changement de statut)
    // ============================================
    @PutMapping("/return/{id}")
    public ResponseEntity<LoanDto> returnLoan(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnLoan(id);
        if (returnedLoan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(returnedLoan));
    }

    // ============================================
    // DELETE - Supprimer un emprunt
    // ============================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================
    // MÉTHODE PRIVÉE - Convertir une entité Loan en LoanDto
    // ============================================
    private LoanDto convertToDto(Loan loan) {
        if (loan == null) return null;
        LoanDto dto = new LoanDto();
        dto.setId(loan.getId());
        dto.setMemberId(loan.getMemberId());
        dto.setBookId(loan.getBookId());
        dto.setLoanDate(loan.getLoanDate() != null ? loan.getLoanDate().toString() : null);
        dto.setReturnDate(loan.getReturnDate() != null ? loan.getReturnDate().toString() : null);
        dto.setStatus(loan.getStatus());
        return dto;
    }
}