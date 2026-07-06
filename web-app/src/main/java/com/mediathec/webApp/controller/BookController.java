package com.mediathec.webApp.controller;

import com.mediathec.webApp.dto.*;
import com.mediathec.webApp.service.*;
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
    private final CustomGameService customGameService;
    private final CustomMovieService customMovieService;

    public BookController(MemberService memberService, LoanService loanService, CustomBookService customBookService, CustomGameService customGameService, CustomMovieService customMovieService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customBookService = customBookService;
        this.customGameService = customGameService;
        this.customMovieService = customMovieService;
    }

    @GetMapping({"/home"})
    public String getHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Récupérer tous les médias
        List<BookDto> medias = customBookService.getAllBooks();
        model.addAttribute("medias", medias);

        List<GameDto> games = customGameService.getAllGames();
        model.addAttribute("games", games);

        List<MovieDto> movies = customMovieService.getAllMovies();
        model.addAttribute("movies", movies);


        List<Long> borrowedMediaIds = new ArrayList<>();

        if (userDetails != null) {   // ← SEULEMENT SI CONNECTÉ
            String email = userDetails.getUsername();
            model.addAttribute("userLogin", email);

            try {
                MemberDto memberDto = memberService.getMemberByEmail(email);
                if (memberDto != null) {
                    model.addAttribute("currentMemberId", memberDto.getId());
                    model.addAttribute("memberRole", memberDto.getRole());
                    List<LoanDto> userLoanDtos = loanService.getLoansByMemberId(memberDto.getId());
                    borrowedMediaIds = userLoanDtos.stream()
                            .filter(loanDto -> "BORROWED".equals(loanDto.getStatus()))
                            .flatMap(loanDto -> {
                                List<Long> ids = new ArrayList<>();
                                if (loanDto.getBookId() != null) ids.add(loanDto.getBookId());
                                if (loanDto.getGameId() != null) ids.add(loanDto.getGameId());
                                if (loanDto.getMovieId() != null) ids.add(loanDto.getMovieId());
                                return ids.stream();
                            })
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