package com.mediathec.loanService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Loan date is required")
    private LocalDateTime loanDate;

    private LocalDateTime returnDate;

    private String status = "BORROWED";  // BORROWED, RETURNED, LATE

    @Column(name = "loan_date")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        loanDate = LocalDateTime.now();
        createdAt = LocalDateTime.now();
    }
}