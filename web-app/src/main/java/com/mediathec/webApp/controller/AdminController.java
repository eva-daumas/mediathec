package com.mediathec.webApp.controller;

import com.mediathec.webApp.dto.BookDto;
import com.mediathec.webApp.dto.GameDto;
import com.mediathec.webApp.dto.LoanDto;
import com.mediathec.webApp.dto.MemberDto;
import com.mediathec.webApp.service.CustomBookService;
import com.mediathec.webApp.service.CustomGameService;
import com.mediathec.webApp.service.LoanService;
import com.mediathec.webApp.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    private final MemberService memberService;
    private final LoanService loanService;
    private final CustomBookService customBookService;
    private final CustomGameService customGameService;

    public AdminController(MemberService memberService, LoanService loanService, CustomBookService customBookService, CustomGameService customGameService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customBookService = customBookService;
        this.customGameService = customGameService;
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        MemberDto memberDto = memberService.getMemberByEmail(email);

        if (memberDto == null || !"ADMIN".equals(memberDto.getRole())) {
            return "redirect:/profile";
        }

        // 1. Récupérer les livres
        List<BookDto> books = customBookService.getAllBooks();

        // 2. Récupérer les jeux
        List<GameDto> games = customGameService.getAllGames();

        // 3. Combiner livres ET jeux dans une liste "medias"
        List<Object> medias = new ArrayList<>();
        if (books != null) medias.addAll(books);
        if (games != null) medias.addAll(games);

        // 4. Récupérer les autres données
        List<MemberDto> memberDtos = memberService.getAllMembers();
        List<LoanDto> loanDtos = loanService.getAllLoans();

        // 5. Calculer le total des médias (livres + jeux)
        int totalMedias = (books != null ? books.size() : 0) + (games != null ? games.size() : 0);

        // 6. Ajouter tout au modèle
        model.addAttribute("userLogin", memberDto.getUsername());
        model.addAttribute("members", memberDtos);
        model.addAttribute("medias", medias);        // Pour la compatibilité
        model.addAttribute("books", books);          // Pour la section livres
        model.addAttribute("games", games);          // Pour la section jeux
        model.addAttribute("allLoans", loanDtos);
        model.addAttribute("totalMembers", memberDtos != null ? memberDtos.size() : 0);
        model.addAttribute("totalMedias", totalMedias);  // ← CORRIGÉ
        model.addAttribute("totalLoans", loanDtos != null ? loanDtos.size() : 0);

        return "admin";
    }

    @GetMapping("/admin/loans/new")
    public String showAddLoanForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("loan", new LoanDto());
        model.addAttribute("userLogin", userDetails.getUsername());
        return "loan-form";
    }

    @PostMapping("/admin/loans")
    public String addLoan(@ModelAttribute LoanDto loanDto) {
        loanService.createLoan(loanDto);
        return "redirect:/admin#loans";
    }

    @PutMapping("/admin/loans/return/{id}")
    @ResponseBody
    public ResponseEntity<String> returnLoan(@PathVariable Long id) {
        try {
            loanService.returnLoan(id);
            return ResponseEntity.ok("Emprunt retourné avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/loans/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteLoan(@PathVariable Long id) {
        try {
            loanService.deleteLoan(id);
            return ResponseEntity.ok("Emprunt supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

    // ======================== ADMIN - MEDIAS ========================

    @GetMapping("/admin/medias/new")
    public String showAddMediaForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("media", new BookDto());
        model.addAttribute("userLogin", userDetails.getUsername());
        return "media-form";
    }

    @PostMapping("/admin/medias")
    public String addMedia(@ModelAttribute BookDto book) {
        customBookService.createBook(book);
        return "redirect:/admin#medias";
    }

    @GetMapping("/admin/medias/edit/{id}")
    public String showEditMediaForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        // ✅ Vérifier si le média est un livre ou un jeu
        BookDto book = customBookService.getBookById(id);
        if (book != null) {
            model.addAttribute("media", book);
        } else {
            // Si ce n'est pas un livre, essayer de récupérer un jeu
            GameDto game = customGameService.getGameById(id);
            if (game != null) {
                // Convertir GameDto en BookDto pour le formulaire
                BookDto gameAsBook = new BookDto();
                gameAsBook.setId(game.getId());
                gameAsBook.setTitle(game.getTitle());
                gameAsBook.setAuthor(game.getAuthor());
                gameAsBook.setCategory(game.getCategory());
                gameAsBook.setDescription(game.getDescription());
                gameAsBook.setCoverImage(game.getCoverImage());
                gameAsBook.setAvailable(game.isAvailable());
                model.addAttribute("media", gameAsBook);
            } else {
                model.addAttribute("media", new BookDto());  // Valeur par défaut
            }
        }

        model.addAttribute("userLogin", userDetails.getUsername());
        return "media-form-edit";
    }

    @PostMapping("/admin/medias/update/{id}")
    public String updateMedia(@PathVariable Long id, @ModelAttribute BookDto book) {
        customBookService.updateBook(id, book);
        return "redirect:/admin#medias";
    }

    @DeleteMapping("/admin/medias/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteMedia(@PathVariable Long id) {
        try {
            customBookService.deleteBook(id);
            return ResponseEntity.ok("Média supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }
}
