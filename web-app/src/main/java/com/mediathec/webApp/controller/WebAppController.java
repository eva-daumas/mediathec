package com.mediathec.webApp.controller;

import com.mediathec.webApp.model.Member;
import com.mediathec.webApp.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebAppController {

    private final MemberService memberService;

    public WebAppController(MemberService memberService) {
        this.memberService = memberService;
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
        model.addAttribute("member", member);
        model.addAttribute("userLogin", member.getUsername());
        return "profile";
    }

    @GetMapping("/members")
    public String getMembersPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        // Récupère la liste des membres
        model.addAttribute("userLogin", userDetails.getUsername());
        return "member/member-list";
    }

    @GetMapping("/loans")
    public String getLoansPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("userLogin", userDetails.getUsername());
        return "loans";
    }
}