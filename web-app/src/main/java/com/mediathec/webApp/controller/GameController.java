package com.mediathec.webApp.controller;


import com.mediathec.webApp.dto.GameDto;
import com.mediathec.webApp.dto.LoanDto;
import com.mediathec.webApp.dto.MemberDto;
import com.mediathec.webApp.service.CustomGameService;
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
public class GameController {
    private final MemberService memberService;
    private final LoanService loanService;
    private final CustomGameService customGameService;

    public GameController(MemberService memberService, LoanService loanService, CustomGameService customGameService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customGameService = customGameService;
    }

    @GetMapping({"/games"})
    public String getGamesPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Récupérer tous les jeux
        List<GameDto> games = customGameService.getAllGames();
        model.addAttribute("games", games);   // ← TOUJOURS présents

        List<Long> borrowedGameIds = new ArrayList<>();

        if (userDetails != null) {   // ← SEULEMENT SI CONNECTÉ
            String email = userDetails.getUsername();
            model.addAttribute("userLogin", email);

            try {
                MemberDto memberDto = memberService.getMemberByEmail(email);
                if (memberDto != null) {
                    model.addAttribute("currentMemberId", memberDto.getId());
                    model.addAttribute("memberRole", memberDto.getRole());
                    List<LoanDto> userLoanDtos = loanService.getLoansByMemberId(memberDto.getId());
                    borrowedGameIds = userLoanDtos.stream()
                            .filter(loanDto -> "BORROWED".equals(loanDto.getStatus()))
                            .map(LoanDto::getGameId)
                            .collect(Collectors.toList());
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }

        model.addAttribute("borrowedGameIds", borrowedGameIds);
        return "games";
    }
}