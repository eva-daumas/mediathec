package com.mediathec.webApp.service.client;

import com.mediathec.webApp.model.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "members-service", url = "http://localhost:8085")
public interface MemberFeignClient {

    @GetMapping("/api/members/email/{email}")  // ← Cette URL
    Member getMemberByEmail(@PathVariable("email") String email);


    @PostMapping("/add")
    Member createMember(@RequestBody Member member);

    @GetMapping("/api/getAll")
    List<Member> getAllMembers();
}