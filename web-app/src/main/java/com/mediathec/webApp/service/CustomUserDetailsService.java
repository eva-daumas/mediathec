package com.mediathec.webApp.service;

import com.mediathec.webApp.feign.MemberFeignClient;
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
        // Appel à member-service via Feign
        String memberJson = memberFeignClient.getMemberByEmail(email);

        if (memberJson == null || memberJson.isEmpty()) {
            throw new UsernameNotFoundException("Member not found with email: " + email);
        }

        // Pour l'instant, on retourne un User simple
        // Tu pourras parser le JSON plus tard
        return User.builder()
                .username(email)
                .password("password")
                .roles("USER")
                .build();
    }
}