package com.mediathec.webApp.service.client;

import com.mediathec.webApp.dto.MovieDto;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "movie-service", url = "http://localhost:8089")
public interface MovieFeignClient {

    @GetMapping("/api/movies/getAll")
    List<MovieDto> getAllMovies();

    @GetMapping("/api/movies/findById/{id}")
    MovieDto getMovieById(@PathVariable("id") Long id);

    @GetMapping("/api/movies/findByCategory/{category}")
    List<MovieDto> getMoviesByCategory(@PathVariable("category") String category);

    @GetMapping("/api/movies/search")
    List<MovieDto> searchMovies(@RequestParam("keyword") String keyword);

    @GetMapping("/api/movies/available")
    List<MovieDto> getAvailableMovies();

    @PostMapping("/api/movies/add")
    MovieDto createMovie(@RequestBody MovieDto movie);

    @PutMapping("/api/movies/update/{id}")
    MovieDto updateMovie(@PathVariable("id") Long id, @RequestBody MovieDto movie);

    @PostMapping("/api/movies/updateAvailability/{id}")
    MovieDto updateAvailability(@PathVariable Long id, @RequestParam boolean available);

    @DeleteMapping("/api/movies/delete/{id}")
    void deleteMovie(@PathVariable("id") Long id);
}