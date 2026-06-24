package com.mediathec.webApp.service;

import com.mediathec.webApp.entity.Member;
import com.mediathec.webApp.service.client.MemberFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberFeignClient memberFeignClient;

    public void register(Member member) {
       memberFeignClient.createMember(member);
    }

    public Member getMemberByEmail(String email) {
        return memberFeignClient.getMemberByEmail(email);

    }

    public List<Member> getAllMembers() {
        return memberFeignClient.getAllMembers();
    }

    public Member getMemberById(Long id) {
        return memberFeignClient.getMemberById(id);
    }

    public void deleteMember(Long id) {
        memberFeignClient.deleteMember(id);
    }

    public void updateMember(Long id, Member member) {
        memberFeignClient.updateMember(id, member);
    }
}