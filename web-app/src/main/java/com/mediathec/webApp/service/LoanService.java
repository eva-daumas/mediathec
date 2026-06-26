package com.mediathec.webApp.service;

import com.mediathec.webApp.dto.LoanDto;
import com.mediathec.webApp.service.client.LoanFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
//todo: constructeur pat autowired
    @Autowired
    private LoanFeignClient loanFeignClient;

    public List<LoanDto> getAllLoans() {
        return loanFeignClient.getAllLoans();
    }

    public LoanDto createLoan(LoanDto loanDto) {
        return loanFeignClient.createLoan(loanDto);
    }

    public void returnLoan(Long id) {
        loanFeignClient.returnLoan(id);
    }

    public void deleteLoan(Long id) {
        loanFeignClient.deleteLoan(id);
    }

    // ============================================================
    //             VERSION AVEC LOGS POUR DÉBOGUER
    // ============================================================
    public List<LoanDto> getLoansByMemberId(Long memberId) {
        try {
            System.out.println("========================================");
            System.out.println("LoanController.getLoansByMemberId()");
            System.out.println("memberId reçu : " + memberId);

            List<LoanDto> loanDtos = loanFeignClient.getLoansByMemberId(memberId);

            System.out.println("🔍 Emprunts reçus du FeignClient : " + (loanDtos != null ? loanDtos.size() : 0));
            if (loanDtos != null && !loanDtos.isEmpty()) {
                for (LoanDto loanDto : loanDtos) {
                    System.out.println("   - ID: " + loanDto.getId() + ", memberId: " + loanDto.getMemberId() + ", status: " + loanDto.getStatus());
                }
            }
            System.out.println("========================================");

            return loanDtos;
        } catch (Exception e) {
            System.err.println(" Erreur FeignClient: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}