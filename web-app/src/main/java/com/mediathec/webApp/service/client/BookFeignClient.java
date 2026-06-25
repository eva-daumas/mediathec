package com.mediathec.webApp.service.client;

import com.mediathec.webApp.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "book-service", url = "http://localhost:8086")
public interface BookFeignClient {

    @GetMapping("/api/getAll")
    List<BookDto> getAllBooks();

    @GetMapping("/api/findById/{id}")
    BookDto getBookById(@PathVariable("id") Long id);

    @GetMapping("/api/findByCategory/{category}")
    List<BookDto> getBooksByCategory(@PathVariable("category") String category);

    @GetMapping("/api/search")
    List<BookDto> searchBooks(@RequestParam("keyword") String keyword);

    @GetMapping("/api/available")
    List<BookDto> getAvailableBooks();

    @PostMapping("/add")
    BookDto createBook(@RequestBody BookDto book);

    @PutMapping("/api/update/{id}")
    BookDto updateBook(@PathVariable("id") Long id, @RequestBody BookDto book);

    @PostMapping("api/updateAvailability/{id}")
    BookDto updateAvailability(@PathVariable Long id, @RequestParam boolean available);

    @DeleteMapping("/api/delete/{id}")
    void deleteBook(@PathVariable("id") Long id);
}
