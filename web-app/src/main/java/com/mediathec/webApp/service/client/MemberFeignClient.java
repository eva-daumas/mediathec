package com.mediathec.webApp.service.client;

import com.mediathec.webApp.entity.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "members-service", url = "http://localhost:8085")
public interface MemberFeignClient {

    @GetMapping("/api/members/email/{email}")
    Member getMemberByEmail(@PathVariable("email") String email);

    @PostMapping("/add")
    Member createMember(@RequestBody Member member);

    @GetMapping("/api/getAll")
    List<Member> getAllMembers();

    @PutMapping("/api/update/{id}")
    Member updateMember(@PathVariable("id") Long id, @RequestBody Member member);

    @GetMapping("/api/findById/{id}")
    Member getMemberById(@PathVariable("id") Long id);

    @DeleteMapping("/api/delete/{id}")
    void deleteMember(@PathVariable("id") Long id);

}