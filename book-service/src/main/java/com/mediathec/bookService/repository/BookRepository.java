package com.mediathec.bookService.repository;

import com.mediathec.bookService.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

    // Méthode pour getBooksByCategory
    List<Book> findByCategory(String category);

    // Méthode pour searchBooks
    List<Book> findByTitleContainingIgnoreCase(String keyword);

    // Méthode pour getAvailableBooks
    List<Book> findByAvailableTrue();
}

