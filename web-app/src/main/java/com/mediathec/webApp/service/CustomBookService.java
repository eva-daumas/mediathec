package com.mediathec.webApp.service;

import com.mediathec.webApp.model.Book;
import com.mediathec.webApp.service.client.BookFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomBookService {

    @Autowired
    private BookFeignClient bookFeignClient;

    public List<Book> getAllBooks() {
        return bookFeignClient.getAllBooks();
    }

    public Book getBookById(Long id) {
        return bookFeignClient.getBookById(id);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookFeignClient.getBooksByCategory(category);
    }

    public List<Book> searchBooks(String keyword) {
        return bookFeignClient.searchBooks(keyword);
    }

    public List<Book> getAvailableBooks() {
        return bookFeignClient.getAvailableBooks();
    }
}