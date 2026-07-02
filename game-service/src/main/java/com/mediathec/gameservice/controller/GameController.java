package com.mediathec.gameservice.controller;

import com.mediathec.gameservice.entity.Game;
import com.mediathec.gameservice.service.GameService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @PostMapping("/add")
    public ResponseEntity<Game> addGame(@Valid @RequestBody Game game) {
        try {
            Game savedGame = gameService.createGame(game);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGame);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/findByTitle")
    public Game findGameByTitle(@RequestParam String title) {
        return gameService.findByTitle(title);
    }

    @GetMapping("/findByCategory/{category}")
    public List<Game> findGamesByCategory(@PathVariable String category) {
        return gameService.getGamesByCategory(category);
    }

    @GetMapping("/getAll")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/findById/{id}")
    public Game findGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    @GetMapping("/available")
    public List<Game> getAvailableGames() {
        return gameService.getAvailableGames();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @Valid @RequestBody Game game) {
        Game updatedGame = gameService.updateGame(id, game);
        return updatedGame != null ? ResponseEntity.ok(updatedGame) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/updateAvailability/{id}")
    public ResponseEntity<Game> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        Game updatedGame = gameService.updateAvailability(id, available);
        return updatedGame != null ? ResponseEntity.ok(updatedGame) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<Game> searchGames(@RequestParam String keyword) {
        return gameService.searchGames(keyword);
    }


}