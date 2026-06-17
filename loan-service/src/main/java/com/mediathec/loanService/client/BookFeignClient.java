package com.mediathec.loanService.client;

import com.mediathec.loanService.model.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", url = "http://localhost:8086")
public interface BookFeignClient {

    @GetMapping("/api/findById/{id}")
    Book getBookById(@PathVariable("id") Long id);
}