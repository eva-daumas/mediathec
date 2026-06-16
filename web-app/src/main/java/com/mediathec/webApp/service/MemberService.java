package com.mediathec.webApp.service;

import com.mediathec.webApp.model.Member;
import com.mediathec.webApp.service.client.MemberFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}