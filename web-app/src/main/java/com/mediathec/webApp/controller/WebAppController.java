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

import java.util.List;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class WebAppController {

    private final MemberService memberService;
    private final LoanService loanService;
    private CustomBookService customBookService;

    //ConstructeurS
    @Autowired
    public WebAppController(MemberService memberService, LoanService loanService, CustomBookService customBookService) {
        this.memberService = memberService;
        this.loanService = loanService;
        this.customBookService = customBookService;
    }

    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/")
    public String getBookPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            model.addAttribute("userLogin", username);
        }
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

    @GetMapping("/profile")
    public String getProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String email = userDetails.getUsername();
        Member member = memberService.getMemberByEmail(email);

        //  SI L'UTILISATEUR EST ADMIN → REDIRIGER VERS /admin
        if (member != null && "ADMIN".equals(member.getRole())) {
            return "redirect:/admin";
        }
        model.addAttribute("member", member);
        model.addAttribute("userLogin", member.getUsername());
        return "profile";
    }

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

        model.addAttribute("userLogin", member.getUsername());

        // Récupérer les membres
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);

        //  Récupérer les médias
        List<Book> medias = customBookService.getAllBooks();
        model.addAttribute("medias", medias);

        model.addAttribute("totalMembers", members != null ? members.size() : 0);
        model.addAttribute("totalMedias", medias != null ? medias.size() : 0);
        model.addAttribute("totalLoans", 0);
        model.addAttribute("totalReturns", 0);

        return "admin";
    }

    @GetMapping("/members")
    public String getMembersPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
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
        memberService.register(member);  // ← Utilise register
        return "redirect:/members";       // ← Redirige vers la liste
    }

    @GetMapping("/loans")
    public String getLoansPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        List<Loan> loans = loanService.getAllLoans();
        model.addAttribute("loans", loans);
        model.addAttribute("userLogin", userDetails.getUsername());
        return "loans";
    }

    @GetMapping("/loans/new")
    public String getNewLoanPage(Model model) {  // ← Supprime @NotNull
        model.addAttribute("loan", new Loan());
        return "loan-form";
    }

    @PostMapping("/loans")
    public String saveLoan(@ModelAttribute Loan loan) {
        // À implémenter
        return "redirect:/loans";
    }

    @GetMapping("/returns")
    public String getReturnsPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("userLogin", userDetails.getUsername());
        return "returns";  // ← Crée ce fichier plus tard
    }

    @GetMapping("/members/{id}/edit")
    public String editMemberForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        Member member = memberService.getMemberById(id);
        model.addAttribute("member", member);
        model.addAttribute("userLogin", userDetails.getUsername());
        return "member/member-edit";
    }

    @PostMapping("/members/{id}/edit")
    public String updateMember(@PathVariable Long id, @ModelAttribute Member member, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        // À implémenter : appeler members-service pour mettre à jour
        // memberService.updateMember(id, member);
        return "redirect:/profile";
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

    @PutMapping("/admin/members/update/{id}")
    @ResponseBody
    public ResponseEntity<String> updateMember(@PathVariable Long id, @RequestBody Member member) {
        try {
            memberService.updateMember(id, member);
            return ResponseEntity.ok("Membre modifié avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

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
    public String addMedia(@ModelAttribute Book book, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        customBookService.createBook(book);
        return "redirect:/admin#medias";
    }
}