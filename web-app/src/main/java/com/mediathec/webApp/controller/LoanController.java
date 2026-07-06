package com.mediathec.webApp.controller;

import com.mediathec.webApp.dto.BookDto;
import com.mediathec.webApp.dto.BorrowedMediaDto;
import com.mediathec.webApp.dto.GameDto;
import com.mediathec.webApp.dto.LoanDto;
import com.mediathec.webApp.dto.MemberDto;
import com.mediathec.webApp.dto.MovieDto;
import com.mediathec.webApp.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoanController {

    private final MemberService memberService;
    private final LoanService loanService;
    private final CustomBookService customBookService;
    private final CustomGameService customGameService;
    private final CustomMovieService customMovieService;

    public LoanController(MemberService memberService, LoanService loanService,
                          CustomBookService customBookService,
                          CustomGameService customGameService, CustomMovieService customMovieService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customBookService = customBookService;
        this.customGameService = customGameService;
        this.customMovieService = customMovieService;
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String email = userDetails.getUsername();
        MemberDto memberDto = memberService.getMemberByEmail(email);

        if (memberDto != null && "ADMIN".equals(memberDto.getRole())) {
            return "redirect:/admin";
        }

        // Récupérer les emprunts de l'utilisateur
        List<LoanDto> userLoanDtos = loanService.getLoansByMemberId(memberDto.getId());
        List<Long> borrowedMediaIds = userLoanDtos.stream()
                .filter(loanDto -> "BORROWED".equals(loanDto.getStatus()))
                .map(LoanDto::getBookId)
                .collect(Collectors.toList());
        model.addAttribute("borrowedMediaIds", borrowedMediaIds);

        model.addAttribute("member", memberDto);
        model.addAttribute("userLogin", memberDto.getUsername());
        return "profile";
    }

    @GetMapping("/loans")
    public String getLoansPage(Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }
        String email = userDetails.getUsername();
        model.addAttribute("userLogin", email);
        MemberDto memberDto = memberService.getMemberByEmail(email);
        List<LoanDto> userLoanDtos = loanService.getLoansByMemberId(memberDto.getId());
        List<BorrowedMediaDto> borrowedMedias = new ArrayList<>();

        userLoanDtos.forEach(loanDto -> {
            BorrowedMediaDto borrowedMedia = new BorrowedMediaDto();

            // Vérifier si c'est un livre (bookId non null)
            if (loanDto.getBookId() != null) {
                try {
                    BookDto bookDto = customBookService.getBookById(loanDto.getBookId());
                    if (bookDto != null) {
                        borrowedMedia.setCategory(bookDto.getCategory());
                        borrowedMedia.setTitle(bookDto.getTitle());
                    } else {
                        borrowedMedia.setCategory("Livre");
                        borrowedMedia.setTitle("Livre non trouvé (ID: " + loanDto.getBookId() + ")");
                    }
                } catch (Exception e) {
                    borrowedMedia.setCategory("Livre");
                    borrowedMedia.setTitle("Erreur de chargement");
                }
            }
            // Vérifier si c'est un jeu (gameId non null)
            else if (loanDto.getGameId() != null) {
                try {
                    GameDto gameDto = customGameService.getGameById(loanDto.getGameId());
                    if (gameDto != null) {
                        borrowedMedia.setCategory(gameDto.getCategory());
                        borrowedMedia.setTitle(gameDto.getTitle());
                    } else {
                        borrowedMedia.setCategory("Jeu");
                        borrowedMedia.setTitle("Jeu non trouvé (ID: " + loanDto.getGameId() + ")");
                    }
                } catch (Exception e) {
                    borrowedMedia.setCategory("Jeu");
                    borrowedMedia.setTitle("Erreur de chargement");
                }
            }
            //  AJOUT POUR LES FILMS
            else if (loanDto.getMovieId() != null) {
                try {
                    MovieDto movieDto = customMovieService.getMovieById(loanDto.getMovieId());
                    if (movieDto != null) {
                        borrowedMedia.setCategory(movieDto.getCategory());
                        borrowedMedia.setTitle(movieDto.getTitle());
                    } else {
                        borrowedMedia.setCategory("Film");
                        borrowedMedia.setTitle("Film non trouvé (ID: " + loanDto.getMovieId() + ")");
                    }
                } catch (Exception e) {
                    borrowedMedia.setCategory("Film");
                    borrowedMedia.setTitle("Erreur de chargement");
                }
            } else {
                borrowedMedia.setCategory("Inconnu");
                borrowedMedia.setTitle("Média inconnu");
            }

            borrowedMedia.setLoanDate(loanDto.getLoanDate());
            borrowedMedia.setReturnDate(loanDto.getReturnDate());
            borrowedMedia.setStatus(loanDto.getStatus());
            borrowedMedias.add(borrowedMedia);
        });

        model.addAttribute("borrowedMedias", borrowedMedias);
        model.addAttribute("member", memberDto);
        model.addAttribute("userLogin", memberDto.getUsername());
        return "loans";
    }

    @GetMapping("/loans/new")
    public String getNewLoanPage(Model model) {
        model.addAttribute("loan", new LoanDto());
        return "loan-form";
    }

    @PostMapping("/loans")
    public String saveLoan(@ModelAttribute LoanDto loanDto) {
        loanService.createLoan(loanDto);
        return "redirect:/loans";
    }

    @GetMapping("/returns")
    public String getReturnsPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        model.addAttribute("userLogin", email);

        try {
            MemberDto memberDto = memberService.getMemberByEmail(email);
            if (memberDto != null) {
                // Récupérer UNIQUEMENT les emprunts de CE membre
                List<LoanDto> loanDtos = loanService.getLoansByMemberId(memberDto.getId());

                for (LoanDto loan : loanDtos) {
                    // Récupérer le username du membre
                    MemberDto member = memberService.getMemberById(loan.getMemberId());
                    if (member != null) {
                        loan.setUsername(member.getUsername());
                    }

                    // Récupérer le titre du livre OU du jeu
                    if (loan.getBookId() != null) {
                        try {
                            BookDto book = customBookService.getBookById(loan.getBookId());
                            if (book != null) {
                                loan.setTitle(book.getTitle());
                            } else {
                                loan.setTitle("Livre non trouvé");
                            }
                        } catch (Exception e) {
                            loan.setTitle("Erreur chargement livre");
                        }
                    } else if (loan.getGameId() != null) {
                        try {
                            GameDto game = customGameService.getGameById(loan.getGameId());
                            if (game != null) {
                                loan.setTitle(game.getTitle());
                            } else {
                                loan.setTitle("Jeu non trouvé");
                            }
                        } catch (Exception e) {
                            loan.setTitle("Erreur chargement jeu");
                        }
                    }
                    //  AJOUT POUR LES FILMS
                    else if (loan.getMovieId() != null) {
                        try {
                            MovieDto movie = customMovieService.getMovieById(loan.getMovieId());
                            if (movie != null) {
                                loan.setTitle(movie.getTitle());
                            } else {
                                loan.setTitle("Film non trouvé");
                            }
                        } catch (Exception e) {
                            loan.setTitle("Erreur chargement film");
                        }
                    } else {
                        loan.setTitle("Média inconnu");
                    }
                }

                model.addAttribute("loans", loanDtos);
                System.out.println("🔍 Retours trouvés pour " + email + " : " + loanDtos.size());
            } else {
                model.addAttribute("loans", new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            model.addAttribute("loans", new ArrayList<>());
        }

        return "returns";
    }

    @PostMapping("/api/loans")
    @ResponseBody
    public ResponseEntity<?> borrowMedia(@RequestBody LoanDto loanDto) {
        try {
            // Vérifier si c'est un livre ou un jeu
            if (loanDto.getBookId() != null && loanDto.getBookId() > 0) {
                // C'est un livre
                System.out.println("Emprunt d'un livre ID: " + loanDto.getBookId());
                LoanDto created = loanService.createLoan(loanDto);
                customBookService.updateAvailability(created.getBookId(), false);
                return ResponseEntity.ok(created);

            } else if (loanDto.getGameId() != null && loanDto.getGameId() > 0) {
                // C'est un jeu
                System.out.println("🎮 Emprunt d'un jeu ID: " + loanDto.getGameId());
                LoanDto created = loanService.createLoan(loanDto);
                customGameService.updateAvailability(loanDto.getGameId(), false);
                return ResponseEntity.ok(created);

            } else if (loanDto.getMovieId() != null && loanDto.getMovieId() > 0) { // ← AJOUTE CES 4 LIGNES
                // C'est un film
                System.out.println("🎬 Emprunt d'un film ID: " + loanDto.getMovieId());
                LoanDto created = loanService.createLoan(loanDto);
                customMovieService.updateAvailability(loanDto.getMovieId(), false);
                return ResponseEntity.ok(created);

            } else {
                return ResponseEntity.badRequest().body(" Aucun média spécifié (bookId, gameId ou movieId requis)");
            }

        } catch (Exception e) {
            System.err.println(" Erreur emprunt: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(" Erreur: " + e.getMessage());
        }
    }

    @PutMapping("/api/loans/return/{mediaId}")
    @ResponseBody
    public ResponseEntity<?> returnMedia(@PathVariable Long mediaId) {
        try {
            List<LoanDto> loanDtos = loanService.getAllLoans();
            LoanDto activeLoanDto = loanDtos.stream()
                    .filter(l -> (l.getBookId() != null && l.getBookId().equals(mediaId)) ||
                            (l.getGameId() != null && l.getGameId().equals(mediaId)) ||
                            (l.getMovieId() != null && l.getMovieId().equals(mediaId))) // AJOUT film
                    .filter(l -> "BORROWED".equals(l.getStatus()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Aucun emprunt actif pour ce média"));

            loanService.returnLoan(activeLoanDto.getId());

            //  Mettre à jour la disponibilité
            if (activeLoanDto.getGameId() != null) {
                customGameService.updateAvailability(activeLoanDto.getGameId(), true);
                System.out.println("🎮 Retour du jeu ID: " + activeLoanDto.getGameId());
            } else if (activeLoanDto.getMovieId() != null) { //  AJOUT film
                customMovieService.updateAvailability(activeLoanDto.getMovieId(), true);
                System.out.println(" Retour du film ID: " + activeLoanDto.getMovieId());
            } else if (activeLoanDto.getBookId() != null) {
                customBookService.updateAvailability(activeLoanDto.getBookId(), true);
                System.out.println(" Retour du livre ID: " + activeLoanDto.getBookId());
            }

            return ResponseEntity.ok("Retour effectué");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(" Erreur: " + e.getMessage());
        }
    }
}