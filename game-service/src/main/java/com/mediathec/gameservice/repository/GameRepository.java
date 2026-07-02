package com.mediathec.gameservice.repository;

import com.mediathec.gameservice.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;  
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    // ===== MÉTHODES PERSONNALISÉES =====
    Optional<Game> findByTitle(String title);

    List<Game> findByCategory(String category);

    List<Game> findByTitleContainingIgnoreCase(String keyword);

    List<Game> findByAvailableTrue();



}