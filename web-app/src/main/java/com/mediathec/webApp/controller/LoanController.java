package com.mediathec.webApp.controller;

import com.mediathec.webApp.dto.BookDto;
import com.mediathec.webApp.dto.BorrowedMediaDto;
import com.mediathec.webApp.dto.LoanDto;
import com.mediathec.webApp.dto.MemberDto;
import com.mediathec.webApp.service.CustomBookService;
import com.mediathec.webApp.service.LoanService;
import com.mediathec.webApp.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoanController {

        private final MemberService memberService;
        private final LoanService loanService;
        private final CustomBookService customBookService;

        public LoanController(MemberService memberService, LoanService loanService, CustomBookService customBookService) {
            this.memberService = memberService;
            this.loanService = loanService;
            this.customBookService = customBookService;
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
            BookDto bookDto = customBookService.getBookById(loanDto.getBookId());
            BorrowedMediaDto borrowedMedia = new BorrowedMediaDto();
            borrowedMedia.setCategory(bookDto.getCategory());
            borrowedMedia.setTitle(bookDto.getTitle());
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
                //  Récupérer UNIQUEMENT les emprunts de CE membre
                List<LoanDto> loanDtos = loanService.getLoansByMemberId(memberDto.getId());
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
            LoanDto created = loanService.createLoan(loanDto);
            customBookService.updateAvailability(created.getBookId(), false);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/api/loans/return/{bookId}")
    @ResponseBody
    public ResponseEntity<?> returnMedia(@PathVariable Long bookId) {
        try {
            // Trouve l'emprunt actif pour ce livre
            List<LoanDto> loanDtos = loanService.getAllLoans();
            LoanDto activeLoanDto = loanDtos.stream()
                    .filter(l -> l.getBookId().equals(bookId) && "BORROWED".equals(l.getStatus()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Aucun emprunt actif pour ce livre"));

            loanService.returnLoan(activeLoanDto.getId());
            customBookService.updateAvailability(activeLoanDto.getBookId(), true);
            return ResponseEntity.ok("Retour effectué");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}







