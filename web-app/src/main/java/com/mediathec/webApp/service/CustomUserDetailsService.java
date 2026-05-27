package com.mediathec.webApp.service;

import com.mediathec.webApp.model.Member;
import com.mediathec.webApp.service.client.MemberFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberFeignClient memberFeignClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberFeignClient.getMemberByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException("Member not found with email: " + email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole())
                .build();
    }
}