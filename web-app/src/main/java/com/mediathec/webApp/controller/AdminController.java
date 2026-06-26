package com.mediathec.webApp.controller;

import com.mediathec.webApp.dto.BookDto;
import com.mediathec.webApp.dto.LoanDto;
import com.mediathec.webApp.dto.MemberDto;
import com.mediathec.webApp.service.CustomBookService;
import com.mediathec.webApp.service.LoanService;
import com.mediathec.webApp.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {
    private final MemberService memberService;
    private final LoanService loanService;
    private final CustomBookService customBookService;

    public AdminController(MemberService memberService, LoanService loanService, CustomBookService customBookService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customBookService = customBookService;
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

        List<MemberDto> memberDtos = memberService.getAllMembers();
        List<BookDto> medias = customBookService.getAllBooks();
        List<LoanDto> loanDtos = loanService.getAllLoans();

        model.addAttribute("userLogin", memberDto.getUsername());
        model.addAttribute("members", memberDtos);
        model.addAttribute("medias", medias);
        model.addAttribute("allLoans", loanDtos);
        model.addAttribute("totalMembers", memberDtos != null ? memberDtos.size() : 0);
        model.addAttribute("totalMedias", medias != null ? medias.size() : 0);
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
        model.addAttribute("media", customBookService.getBookById(id));
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
