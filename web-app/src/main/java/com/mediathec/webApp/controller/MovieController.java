package com.mediathec.webApp.controller;

import com.mediathec.webApp.dto.LoanDto;
import com.mediathec.webApp.dto.MemberDto;
import com.mediathec.webApp.dto.MovieDto;
import com.mediathec.webApp.service.CustomMovieService;
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
public class MovieController {
    private final MemberService memberService;
    private final LoanService loanService;
    private final CustomMovieService customMovieService;

    public MovieController(MemberService memberService, LoanService loanService, CustomMovieService customMovieService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customMovieService = customMovieService;
    }

    @GetMapping({"/movies"})
    public String getMoviesPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Récupérer tous les films
        List<MovieDto> movies = customMovieService.getAllMovies();
        model.addAttribute("movies", movies);

        List<Long> borrowedMovieIds = new ArrayList<>();

        if (userDetails != null) {
            String email = userDetails.getUsername();
            model.addAttribute("userLogin", email);

            try {
                MemberDto memberDto = memberService.getMemberByEmail(email);
                if (memberDto != null) {
                    model.addAttribute("currentMemberId", memberDto.getId());
                    model.addAttribute("memberRole", memberDto.getRole());
                    List<LoanDto> userLoanDtos = loanService.getLoansByMemberId(memberDto.getId());
                    borrowedMovieIds = userLoanDtos.stream()
                            .filter(loanDto -> "BORROWED".equals(loanDto.getStatus()))
                            .map(LoanDto::getMovieId)
                            .collect(Collectors.toList());
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }

        model.addAttribute("borrowedMovieIds", borrowedMovieIds);
        return "home";  
    }
}