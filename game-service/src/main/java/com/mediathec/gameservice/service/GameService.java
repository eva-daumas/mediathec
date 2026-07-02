package com.mediathec.gameservice.service;

import com.mediathec.gameservice.entity.Game;
import com.mediathec.gameservice.repository.GameRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(Game game) {
        game.setAvailable(true);
        return gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public List<Game> getGamesByCategory(String category) {
        return gameRepository.findByCategory(category);
    }

    public List<Game> searchGames(String keyword) {
        return gameRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Game> getAvailableGames() {
        return gameRepository.findByAvailableTrue();
    }

    public Game updateGame(Long id, Game gameDetails) {
        return gameRepository.findById(id).map(game -> {
            game.setTitle(gameDetails.getTitle());
            game.setAuthor(gameDetails.getAuthor());
            game.setCategory(gameDetails.getCategory());
            game.setDescription(gameDetails.getDescription());
            game.setCoverImage(gameDetails.getCoverImage());
            return gameRepository.save(game);
        }).orElse(null);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    public Game updateAvailability(Long id, boolean available) {
        return gameRepository.findById(id).map(game -> {
            game.setAvailable(available);
            return gameRepository.save(game);
        }).orElse(null);
    }

    public Game findByTitle(String title) {
        List<Game> games = gameRepository.findByTitleContainingIgnoreCase(title);
        return games.isEmpty() ? null : games.get(0);
    }
}