package com.mediathec.gameservice.repository;

import com.mediathec.gameservice.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    // ===== MÉTHODES PERSONNALISÉES =====
    List<Game> findByCategory(String category);

    List<Game> findByTitleContainingIgnoreCase(String keyword);

    List<Game> findByAvailableTrue();

}