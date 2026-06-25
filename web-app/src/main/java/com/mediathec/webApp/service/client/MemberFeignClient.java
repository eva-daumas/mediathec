package com.mediathec.webApp.service.client;

import com.mediathec.webApp.dto.MemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "members-service", url = "http://localhost:8085")
public interface MemberFeignClient {

    @GetMapping("/api/members/email/{email}")
    MemberDto getMemberByEmail(@PathVariable("email") String email);

    @PostMapping("/add")
    MemberDto createMember(@RequestBody MemberDto memberDto);

    @GetMapping("/api/getAll")
    List<MemberDto> getAllMembers();

    @PutMapping("/api/update/{id}")
    MemberDto updateMember(@PathVariable("id") Long id, @RequestBody MemberDto memberDto);

    @GetMapping("/api/findById/{id}")
    MemberDto getMemberById(@PathVariable("id") Long id);

    @DeleteMapping("/api/delete/{id}")
    void deleteMember(@PathVariable("id") Long id);

}