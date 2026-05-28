package com.mediathec.webApp.service;

import com.mediathec.webApp.model.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public void register(Member member) {
        System.out.println("Inscription: " + member.getEmail());
    }
}