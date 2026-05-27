package com.mediathec.webApp.service.client;

import com.mediathec.webApp.model.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "book-service", url = "http://localhost:8086")
public interface BookFeignClient {

    @GetMapping("/api/getAll")
    List<Book> getAllBooks();

    @GetMapping("/api/findById/{id}")
    Book getBookById(@PathVariable("id") Long id);

    @GetMapping("/api/findByCategory/{category}")
    List<Book> getBooksByCategory(@PathVariable("category") String category);

    @GetMapping("/api/search")
    List<Book> searchBooks(@RequestParam("keyword") String keyword);

    @GetMapping("/api/available")
    List<Book> getAvailableBooks();
}