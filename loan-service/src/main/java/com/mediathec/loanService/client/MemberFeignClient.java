package com.mediathec.loanService.client;

import com.mediathec.loanService.model.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "members-service", url = "http://localhost:8085")
public interface MemberFeignClient {

    @GetMapping("/api/members/email/{email}")
    Member getMemberByEmail(@PathVariable("email") String email);

    @GetMapping("/api/findById/{id}")
    Member getMemberById(@PathVariable("id") Long id);
}