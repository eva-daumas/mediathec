package com.mediathec.webApp.controller;

import com.mediathec.webApp.dto.BookDto;
import com.mediathec.webApp.dto.LoanDto;
import com.mediathec.webApp.dto.MemberDto;
import com.mediathec.webApp.service.CustomBookService;
import com.mediathec.webApp.service.LoanService;
import com.mediathec.webApp.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController {
    private final MemberService memberService;
    private final LoanService loanService;
    private final CustomBookService customBookService;

    public BookController(MemberService memberService, LoanService loanService, CustomBookService customBookService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customBookService = customBookService;
    }

    @GetMapping({"/home"})
    public String getHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Récupérer tous les médias
        List<BookDto> medias = customBookService.getAllBooks();
        model.addAttribute("medias", medias);   // ← TOUJOURS présents

        List<Long> borrowedMediaIds = new ArrayList<>();

        if (userDetails != null) {   // ← SEULEMENT SI CONNECTÉ
            String email = userDetails.getUsername();
            model.addAttribute("userLogin", email);

            try {
                MemberDto memberDto = memberService.getMemberByEmail(email);
                if (memberDto != null) {
                    model.addAttribute("currentMemberId", memberDto.getId());
                    List<LoanDto> userLoanDtos = loanService.getLoansByMemberId(memberDto.getId());
                    borrowedMediaIds = userLoanDtos.stream()
                            .filter(loanDto -> "BORROWED".equals(loanDto.getStatus()))
                            .map(LoanDto::getBookId)
                            .collect(Collectors.toList());
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }

        model.addAttribute("borrowedMediaIds", borrowedMediaIds);
        return "home";
    }
}

