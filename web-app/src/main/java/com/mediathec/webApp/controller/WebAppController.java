package com.mediathec.webApp.controller;

import com.mediathec.webApp.model.Book;
import com.mediathec.webApp.model.Loan;
import com.mediathec.webApp.model.Member;
import com.mediathec.webApp.service.CustomBookService;
import com.mediathec.webApp.service.LoanService;
import com.mediathec.webApp.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class WebAppController {

    private final MemberService memberService;
    private final LoanService loanService;
    private final CustomBookService customBookService;

    @Autowired
    public WebAppController(MemberService memberService, LoanService loanService, CustomBookService customBookService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customBookService = customBookService;
    }

    // ======================== PAGES PUBLIQUES ========================

    @GetMapping({"/", "/home"})
    public String getHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Récupérer tous les médias
        List<Book> medias = customBookService.getAllBooks();
        model.addAttribute("medias", medias);   // ← TOUJOURS présents

        List<Long> borrowedMediaIds = new ArrayList<>();

        if (userDetails != null) {   // ← SEULEMENT SI CONNECTÉ
            String email = userDetails.getUsername();
            model.addAttribute("userLogin", email);

            try {
                Member member = memberService.getMemberByEmail(email);
                if (member != null) {
                    model.addAttribute("currentMemberId", member.getId());
                    List<Loan> userLoans = loanService.getLoansByMemberId(member.getId());
                    borrowedMediaIds = userLoans.stream()
                            .filter(loan -> "BORROWED".equals(loan.getStatus()))
                            .map(Loan::getBookId)
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
        model.addAttribute("member", new Member());
        return "signup";
    }

    @PostMapping("/signup")
    public String createMember(@ModelAttribute Member member) {
        memberService.register(member);
        return "redirect:/login";
    }

    // ======================== PROFIL ========================

    @GetMapping("/profile")
    public String getProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String email = userDetails.getUsername();
        Member member = memberService.getMemberByEmail(email);

        if (member != null && "ADMIN".equals(member.getRole())) {
            return "redirect:/admin";
        }

        // Récupérer les emprunts de l'utilisateur
        List<Loan> userLoans = loanService.getLoansByMemberId(member.getId());
        List<Long> borrowedMediaIds = userLoans.stream()
                .filter(loan -> "BORROWED".equals(loan.getStatus()))
                .map(Loan::getBookId)
                .collect(Collectors.toList());
        model.addAttribute("borrowedMediaIds", borrowedMediaIds);

        model.addAttribute("member", member);
        model.addAttribute("userLogin", member.getUsername());
        return "profile";
    }

    // ======================== ADMIN ========================

    @GetMapping("/admin")
    public String getAdminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        Member member = memberService.getMemberByEmail(email);

        if (member == null || !"ADMIN".equals(member.getRole())) {
            return "redirect:/profile";
        }

        List<Member> members = memberService.getAllMembers();
        List<Book> medias = customBookService.getAllBooks();
        List<Loan> loans = loanService.getAllLoans();

        model.addAttribute("userLogin", member.getUsername());
        model.addAttribute("members", members);
        model.addAttribute("medias", medias);
        model.addAttribute("allLoans", loans);
        model.addAttribute("totalMembers", members != null ? members.size() : 0);
        model.addAttribute("totalMedias", medias != null ? medias.size() : 0);
        model.addAttribute("totalLoans", loans != null ? loans.size() : 0);

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
        model.addAttribute("member", new Member());
        return "member/member-form";
    }

    @PostMapping("/members")
    public String saveMember(@ModelAttribute Member member) {
        memberService.register(member);
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
    public String updateMember(@PathVariable Long id, @ModelAttribute Member member) {
        memberService.updateMember(id, member);
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
    public String getLoansPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        model.addAttribute("userLogin", email);

        try {
            Member member = memberService.getMemberByEmail(email);
            if (member != null) {
                // 🔥 DEBUG : Afficher l'ID du membre
                System.out.println("🔍 Membre connecté : " + email + " (ID: " + member.getId() + ")");

                List<Loan> loans = loanService.getLoansByMemberId(member.getId());
                model.addAttribute("loans", loans);
                System.out.println("🔍 Emprunts trouvés : " + loans.size());
            } else {
                model.addAttribute("loans", new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            model.addAttribute("loans", new ArrayList<>());
        }

        return "loans";
    }

    @GetMapping("/loans/new")
    public String getNewLoanPage(Model model) {
        model.addAttribute("loan", new Loan());
        return "loan-form";
    }

    @PostMapping("/loans")
    public String saveLoan(@ModelAttribute Loan loan) {
        loanService.createLoan(loan);
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
            Member member = memberService.getMemberByEmail(email);
            if (member != null) {
                // 🔥 Récupérer UNIQUEMENT les emprunts de CE membre
                List<Loan> loans = loanService.getLoansByMemberId(member.getId());
                model.addAttribute("loans", loans);
                System.out.println("🔍 Retours trouvés pour " + email + " : " + loans.size());
            } else {
                model.addAttribute("loans", new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
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
        model.addAttribute("loan", new Loan());
        model.addAttribute("userLogin", userDetails.getUsername());
        return "loan-form";
    }

    @PostMapping("/admin/loans")
    public String addLoan(@ModelAttribute Loan loan) {
        loanService.createLoan(loan);
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
        model.addAttribute("media", new Book());
        model.addAttribute("userLogin", userDetails.getUsername());
        return "media-form";
    }

    @PostMapping("/admin/medias")
    public String addMedia(@ModelAttribute Book book) {
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
    public String updateMedia(@PathVariable Long id, @ModelAttribute Book book) {
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
    public ResponseEntity<?> borrowMedia(@RequestBody Loan loan) {
        try {
            Loan created = loanService.createLoan(loan);
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
            List<Loan> loans = loanService.getAllLoans();
            Loan activeLoan = loans.stream()
                    .filter(l -> l.getBookId().equals(bookId) && "BORROWED".equals(l.getStatus()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Aucun emprunt actif pour ce livre"));

            loanService.returnLoan(activeLoan.getId());
            return ResponseEntity.ok("Retour effectué");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}