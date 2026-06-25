package com.mediathec.webApp.dto;

import lombok.Data;

@Data
public class BorrowedMediaDto {
    private String category;
    private String title;
    private String loanDate;
    private String returnDate;
    private String status;
}
