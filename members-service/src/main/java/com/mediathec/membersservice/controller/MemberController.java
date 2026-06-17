package com.mediathec.membersservice.controller;

import com.mediathec.membersservice.entity.Member;
import com.mediathec.membersservice.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class MemberController {

    private final MemberService memberService;

    // Constructeur
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/add")
    public Member addMember(@RequestBody Member member) {

            return memberService.save(member);

    }

    @GetMapping("/api/members/email/{email}")
    public Member findMemberByEmail(@PathVariable("email") String email) {
        return memberService.findByEmail(email);
    }

    @GetMapping("/api/getAll")
    public List<Member> getAllMembers() {
        return memberService.findAll();
    }

    @GetMapping("/api/findById/{id}")
    public Member findMemberById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @PutMapping("/api/update/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        member.setId(id);
        Member updatedMember = memberService.update(member);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/api/delete/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
