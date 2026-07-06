package com.mediathec.loanService.client;

import com.mediathec.loanService.model.Movie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "movie-service", url = "http://localhost:8089")
public interface MovieFeignClient {

    @GetMapping("/api/movies/findById/{id}")
    Movie getMovieById(@PathVariable("id") Long id);

    @PutMapping("/api/movies/update/{id}")
    Movie updateMovie(@PathVariable("id") Long id, @RequestBody Movie movie);
}