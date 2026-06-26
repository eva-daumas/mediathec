/*
package com.mediathec.webApp.controller;

import com.mediathec.webApp.dto.BookDto;
import com.mediathec.webApp.dto.BorrowedMediaDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//todo: découper la classe WebAppController en plusieurs petites classe, AdminController | MemberController | LoanController | BookController

@Controller
public class WebAppController {

    private final MemberService memberService;
    private final LoanService loanService;
    private final CustomBookService customBookService;

    public WebAppController(MemberService memberService, LoanService loanService, CustomBookService customBookService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customBookService = customBookService;
    }

    // ======================== PAGES PUBLIQUES ========================

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

    @GetMapping("/login")
    public String getLoginPage() {
        return "signin";
    }

    @GetMapping("/signup")
    public String getSignUpPage(Model model) {
        model.addAttribute("member", new MemberDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String createMember(@ModelAttribute MemberDto memberDto) {
        memberService.register(memberDto);
        return "redirect:/login";
    }

    // ======================== PROFIL ========================

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

    // ======================== ADMIN ========================

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

    // ======================== GESTION DES MEMBRES ========================

    @GetMapping("/members")
    public String getMembersPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("userLogin", userDetails.getUsername());
        return "member/member-list";
    }

    @GetMapping("/members/new")
    public String getNewMemberPage(Model model) {
        model.addAttribute("member", new MemberDto());
        return "member/member-form";
    }

    @PostMapping("/members")
    public String saveMember(@ModelAttribute MemberDto memberDto) {
        memberService.register(memberDto);
        return "redirect:/members";
    }

    @GetMapping("/members/{id}/edit")
    public String editMemberForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", memberService.getMemberById(id));
        model.addAttribute("userLogin", userDetails.getUsername());
        return "member/member-edit";
    }

    @PostMapping("/members/{id}/edit")
    public String updateMember(@PathVariable Long id, @ModelAttribute MemberDto memberDto) {
        memberService.updateMember(id, memberDto);
        return "redirect:/members";
    }

    @DeleteMapping("/admin/members/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok("Membre supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

    // ======================== GESTION DES EMPRUNTS ========================

    @GetMapping("/loans")
    public String getLoansPage(Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        model.addAttribute("userLogin", email);

//        try {
//
//            MemberDto memberDto = memberService.getMemberByEmail(email);
//
//            if (memberDto == null) {
//                model.addAttribute("loans", new ArrayList<>());
//                return "loans";
//            }
//
//            System.out.println("====================================");
//            System.out.println("Utilisateur connecté : " + email);
//            System.out.println("MemberDto ID : " + memberDto.getId());
//            System.out.println("====================================");
//
//            List<LoanDto> loanDtos =
//                    loanService.getAllLoans();
//
//            System.out.println("Nombre d'emprunts trouvés : " + loanDtos.size());
//
//            for (LoanDto loanDto : loanDtos) {
//                System.out.println(
//                        "LoanDto ID=" + loanDto.getId()
//                                + " BookID=" + loanDto.getBookId()
//                                + " MemberID=" + loanDto.getMemberId()
//                                + " Status=" + loanDto.getStatus()
//                );
//            }
//
//            model.addAttribute("loans", loanDtos);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//            model.addAttribute("loans", new ArrayList<>());
//        }
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
                // 🔥 Récupérer UNIQUEMENT les emprunts de CE membre
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

    // ======================== ADMIN - EMPRUNTS ========================

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

    // ======================== API - EMPRUNTS ========================

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
}*/
