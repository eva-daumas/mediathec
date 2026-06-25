package com.mediathec.webApp.dto;

public class LoanDto {
    private Long id;
    private Long memberId;
    private Long bookId;
    private String loanDate;
    private String returnDate;
    private String status;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public String getLoanDate() { return loanDate; }
    public void setLoanDate(String loanDate) { this.loanDate = loanDate; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}