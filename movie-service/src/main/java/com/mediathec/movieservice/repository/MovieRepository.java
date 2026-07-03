package com.mediathec.movieservice.repository;

import com.mediathec.movieservice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // ===== MÉTHODES PERSONNALISÉES =====
    Optional<Movie> findByTitle(String title);

    List<Movie> findByCategory(String category);

    List<Movie> findByTitleContainingIgnoreCase(String keyword);

    List<Movie> findByAvailableTrue();
}
