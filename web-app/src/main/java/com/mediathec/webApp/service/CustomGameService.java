package com.mediathec.webApp.service;


import com.mediathec.webApp.dto.GameDto;
import com.mediathec.webApp.service.client.GameFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomGameService {

    @Autowired
    private GameFeignClient gameFeignClient;

    public List<GameDto> getAllGames() {
        return gameFeignClient.getAllGames();
    }

    public GameDto getGameById(Long id) {
        return gameFeignClient.getGameById(id);
    }

    public List<GameDto> getGamesByCategory(String category) {
        return gameFeignClient.getGamesByCategory(category);
    }

    public List<GameDto> searchGames(String keyword) {
        return gameFeignClient.searchGames(keyword);
    }

    public List<GameDto> getAvailableGames() {
        return gameFeignClient.getAvailableGames();
    }

    public GameDto createGame(GameDto game) {
        return gameFeignClient.createGame(game);
    }

    public GameDto updateGame(Long id, GameDto game) {
        return gameFeignClient.updateGame(id, game);
    }

    public GameDto updateAvailability(Long id, boolean available) {
        return gameFeignClient.updateAvailability(id, available);
    }

    public void deleteGame(Long id) {
        gameFeignClient.deleteGame(id);
    }
}