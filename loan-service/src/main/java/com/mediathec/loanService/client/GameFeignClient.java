package com.mediathec.loanService.client;

import com.mediathec.loanService.model.Game;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "game-service", url = "http://localhost:8088")
public interface GameFeignClient {

    @GetMapping("/api/findById/{id}")
    Game getGameById(@PathVariable("id") Long id);

    @PutMapping("/api/update/{id}")
    Game updateGame(@PathVariable("id") Long id, @RequestBody Game game);
}
