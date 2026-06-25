package com.mediathec.webApp.service;

import com.mediathec.webApp.dto.MemberDto;
import com.mediathec.webApp.service.client.MemberFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberFeignClient memberFeignClient;

    public void register(MemberDto memberDto) {
       memberFeignClient.createMember(memberDto);
    }

    public MemberDto getMemberByEmail(String email) {
        return memberFeignClient.getMemberByEmail(email);

    }

    public List<MemberDto> getAllMembers() {
        return memberFeignClient.getAllMembers();
    }

    public MemberDto getMemberById(Long id) {
        return memberFeignClient.getMemberById(id);
    }

    public void deleteMember(Long id) {
        memberFeignClient.deleteMember(id);
    }

    public void updateMember(Long id, MemberDto memberDto) {
        memberFeignClient.updateMember(id, memberDto);
    }
}