package com.mediathec.webApp.controller;

import com.mediathec.webApp.dto.MemberDto;
import com.mediathec.webApp.service.CustomBookService;
import com.mediathec.webApp.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberController {
    private final MemberService memberService;
    public MemberController(MemberService memberService) {

        this.memberService = memberService;

    }

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
}

