package com.mediathec.membersservice.service;

import com.mediathec.membersservice.entity.Member;
import com.mediathec.membersservice.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(Member member) {
        // ENCODE LE MOT DE PASSE
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        if (member.getCreatedAt() == null) {
            member.setCreatedAt(LocalDateTime.now());
        }

        return memberRepository.save(member);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member update(Member newMemberData) {
        return memberRepository.save(newMemberData);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }


}