package com.mediathec.loanService.controller;

import com.mediathec.loanService.dto.LoanDto;
import com.mediathec.loanService.entity.Loan;
import com.mediathec.loanService.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@AllArgsConstructor
public class LoanController {

    private final LoanService loanService;

    //  GET - Récupérer tous les emprunts
    @GetMapping
    public ResponseEntity<List<LoanDto>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        List<LoanDto> loanDtos = loans.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loanDtos);
    }

    //  GET - Récupérer un emprunt par ID
    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        return ResponseEntity.ok(convertToDto(loan));
    }

    //  GET - Récupérer les emprunts d'un membre
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoanDto>> getLoansByMemberId(@PathVariable Long memberId) {
        List<Loan> loans = loanService.getLoansByMemberId(memberId);
        List<LoanDto> loanDtos = loans.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loanDtos);
    }

    //  POST - Créer un emprunt (CORRECTION : enlever le "/loans" en trop)
    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody LoanDto loanDto) {
        Loan loan = new Loan();
        loan.setMemberId(loanDto.getMemberId());
        loan.setBookId(loanDto.getBookId());
        loan.setGameId(loanDto.getGameId());
        loan.setMovieId(loanDto.getMovieId());

        loan.setStatus("BORROWED");
        loan.setLoanDate(LocalDateTime.now());

        Loan savedLoan = loanService.createLoan(loan);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLoan);
    }

    //  PUT - Retourner un emprunt
    @PutMapping("/return/{id}")
    public ResponseEntity<LoanDto> returnLoan(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnLoan(id);
        if (returnedLoan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDto(returnedLoan));
    }

    //  DELETE - Supprimer un emprunt
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================
    // MÉTHODE PRIVÉE - Convertir Loan en LoanDto
    // ============================================
    private LoanDto convertToDto(Loan loan) {
        if (loan == null) return null;
        LoanDto dto = new LoanDto();
        dto.setId(loan.getId());
        dto.setMemberId(loan.getMemberId());
        dto.setBookId(loan.getBookId());
        dto.setGameId(loan.getGameId());
        dto.setMovieId(loan.getMovieId());

        dto.setLoanDate(loan.getLoanDate() != null ? loan.getLoanDate().toString() : null);
        dto.setReturnDate(loan.getReturnDate() != null ? loan.getReturnDate().toString() : null);
        dto.setStatus(loan.getStatus());
        return dto;
    }
}