package com.mediathec.loanService.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "loan_date")
    private LocalDateTime loanDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    private String status;  // BORROWED, RETURNED

    @PrePersist
    protected void onCreate() {
        loanDate = LocalDateTime.now();
        status = "BORROWED";
    }
}