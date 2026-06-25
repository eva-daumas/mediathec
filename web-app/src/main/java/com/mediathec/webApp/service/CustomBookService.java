package com.mediathec.webApp.service;

import com.mediathec.webApp.dto.BookDto;
import com.mediathec.webApp.service.client.BookFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomBookService {

    @Autowired
    private BookFeignClient bookFeignClient;

    public List<BookDto> getAllBooks() {
        return bookFeignClient.getAllBooks();
    }

    public BookDto getBookById(Long id) {
        return bookFeignClient.getBookById(id);
    }

    public List<BookDto> getBooksByCategory(String category) {
        return bookFeignClient.getBooksByCategory(category);
    }

    public List<BookDto> searchBooks(String keyword) {
        return bookFeignClient.searchBooks(keyword);
    }

    public List<BookDto> getAvailableBooks() {
        return bookFeignClient.getAvailableBooks();
    }

    public BookDto createBook(BookDto book) {
        return bookFeignClient.createBook(book);
    }

    public BookDto updateBook(Long id, BookDto book) {
        return bookFeignClient.updateBook(id, book);
    }

    public BookDto updateAvailability(Long id, boolean available) { return bookFeignClient.updateAvailability(id, available); }

    public void deleteBook(Long id) {
        bookFeignClient.deleteBook(id);
    }
}