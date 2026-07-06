package com.mediathec.webApp.service;

import com.mediathec.webApp.dto.MovieDto;
import com.mediathec.webApp.service.client.MovieFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomMovieService {

    @Autowired
    private MovieFeignClient movieFeignClient;

    public List<MovieDto> getAllMovies() {
        return movieFeignClient.getAllMovies();
    }

    public MovieDto getMovieById(Long id) {
        return movieFeignClient.getMovieById(id);
    }

    public List<MovieDto> getMoviesByCategory(String category) {
        return movieFeignClient.getMoviesByCategory(category);
    }

    public List<MovieDto> searchMovies(String keyword) {
        return movieFeignClient.searchMovies(keyword);
    }

    public List<MovieDto> getAvailableMovies() {
        return movieFeignClient.getAvailableMovies();
    }

    public MovieDto createMovie(MovieDto movie) {
        return movieFeignClient.createMovie(movie);
    }

    public MovieDto updateMovie(Long id, MovieDto movie) {
        return movieFeignClient.updateMovie(id, movie);
    }

    public MovieDto updateAvailability(Long id, boolean available) {
        return movieFeignClient.updateAvailability(id, available);
    }

    public void deleteMovie(Long id) {
        movieFeignClient.deleteMovie(id);
    }


    }
