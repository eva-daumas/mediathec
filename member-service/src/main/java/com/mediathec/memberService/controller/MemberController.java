package com.mediathec.memberService.controller;

import com.mediathec.memberService.entity.Member;
import com.mediathec.memberService.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/add")
    public ResponseEntity<Member> addMember(@Valid @RequestBody Member member) {
        try {
            Member savedMember = memberService.save(member);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/api/findByEmail")
    public Member findMemberByEmail(@RequestParam String email) {
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
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @Valid @RequestBody Member member) {
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