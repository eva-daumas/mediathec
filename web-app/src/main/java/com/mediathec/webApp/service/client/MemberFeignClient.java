package com.mediathec.webApp.service.client;

import com.mediathec.webApp.model.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "members-service", url = "http://localhost:8085")
public interface MemberFeignClient {

    @GetMapping("/api/members/email/{email}")
    Member getMemberByEmail(@PathVariable("email") String email);

    @PostMapping("/api/members")
    Member createMember(@RequestBody Member member);
}