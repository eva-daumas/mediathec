package com.mediathec.memberService.service;

import com.mediathec.memberService.entity.Member;
import com.mediathec.memberService.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
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