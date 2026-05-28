package com.mediathec.bookService.service;

import com.mediathec.bookService.entity.Book;
import com.mediathec.bookService.repository.BookRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class BookService {

    private final BookRepository bookRepository;

    // Constructeur
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(Book book) {
        book.setAvailable(true);
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableTrue();
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setCategory(bookDetails.getCategory());
            book.setDescription(bookDetails.getDescription());
            book.setCoverImage(bookDetails.getCoverImage());
            return bookRepository.save(book);
        }
        return null;
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateAvailability(Long id, boolean available) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setAvailable(available);
            return bookRepository.save(book);
        }
        return null;
    }
    public Book findByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return books.isEmpty() ? null : books.get(0);
    }
}
