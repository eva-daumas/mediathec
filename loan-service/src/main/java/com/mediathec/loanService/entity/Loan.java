package com.mediathec.loanService.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;
    private Long gameId;  // ← DOIT ÊTRE PRÉSENT

    @Column(nullable = false)
    private Long memberId;

    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
    private String status = "BORROWED";

    public Loan() {}

    // ✅ CONSTRUCTEUR AVEC TOUS LES CHAMPS
    public Loan(Long memberId, Long bookId, Long gameId, String status) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.gameId = gameId;
        this.status = status;
        this.loanDate = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (loanDate == null) {
            loanDate = LocalDateTime.now();
        }
    }

    // ✅ TOUS LES GETTERS ET SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getGameId() { return gameId; }  // ← GETTER
    public void setGameId(Long gameId) { this.gameId = gameId; }  // ← SETTER

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public LocalDateTime getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDateTime loanDate) { this.loanDate = loanDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}