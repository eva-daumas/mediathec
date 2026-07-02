package com.mediathec.webApp.service.client;



import com.mediathec.webApp.dto.GameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "game-service", url = "http://localhost:8088")
public interface GameFeignClient {

    @GetMapping("/api/games/getAll")
    List<GameDto> getAllGames();

    @GetMapping("/api/games/findById/{id}")
    GameDto getGameById(@PathVariable("id") Long id);

    @GetMapping("/api/games/findByCategory/{category}")
    List<GameDto> getGamesByCategory(@PathVariable("category") String category);

    @GetMapping("/api/games/search")
    List<GameDto> searchGames(@RequestParam("keyword") String keyword);

    @GetMapping("/api/games/available")
    List<GameDto> getAvailableGames();

    @PostMapping("/api/games/add")
    GameDto createGame(@RequestBody GameDto game);

    @PutMapping("/api/games/update/{id}")
    GameDto updateGame(@PathVariable("id") Long id, @RequestBody GameDto game);

    @PostMapping("/api/games/updateAvailability/{id}")
    GameDto updateAvailability(@PathVariable("id") Long id, @RequestParam("available") boolean available);

    @DeleteMapping("/api/games/delete/{id}")
    void deleteGame(@PathVariable("id") Long id);
}
