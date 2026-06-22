package com.mediathec.bookService.controller;

import com.mediathec.bookService.entity.Book;
import com.mediathec.bookService.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        try {
            Book savedBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/api/findByTitle")
    public Book findBookByTitle(@RequestParam String title) {
        // Cette méthode n'existe pas dans BookService
        // Il faut l'ajouter ou utiliser searchBooks
        List<Book> books = bookService.searchBooks(title);
        return bookService.findByTitle(title);
    }

    @GetMapping("/api/findByCategory/{category}")
    public List<Book> findBooksByCategory(@PathVariable String category) {
        return bookService.getBooksByCategory(category);
    }

    @GetMapping("/api/getAll")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/api/findById/{id}")
    public Book findBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/api/available")
    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    @PutMapping("/api/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return updatedBook != null ? ResponseEntity.ok(updatedBook) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/updateAvailability/{id}")
    public ResponseEntity<Book> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        Book updatedBook = bookService.updateAvailability(id, available);
        return updatedBook != null ? ResponseEntity.ok(updatedBook) : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookService.searchBooks(keyword);
    }


}