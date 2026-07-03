package com.mediathec.movieservice.controller;

import com.mediathec.movieservice.entity.Movie;
import com.mediathec.movieservice.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


    @RestController
    @AllArgsConstructor
    @RequestMapping("/api/movies")
    public class MovieController {

        private final MovieService movieService;

        @PostMapping("/add")
        public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
            try {
                Movie savedMovie = movieService.createMovie(movie);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
        }

        @GetMapping("/findByTitle")
        public Movie findMovieByTitle(@RequestParam String title) {
            return movieService.findByTitle(title);
        }

        @GetMapping("/findByCategory/{category}")
        public List<Movie> findMovieByCategory(@PathVariable String category) {
            return movieService.getMoviesByCategory(category);
        }

        @GetMapping("/getAll")
        public List<Movie> getAllMovies() {
            return movieService.getAllMovies();
        }

        @GetMapping("/findById/{id}")
        public Movie findMovieById(@PathVariable Long id) {
            return movieService.getMovieById(id);
        }

        @GetMapping("/available")
        public List<Movie> getAvailableMovies() {
            return movieService.getAvailableMovies();
        }

        @PutMapping("/update/{id}")
        public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movie) {
            Movie updatedMovie = movieService.updateMovie(id, movie);
            return updatedMovie != null ? ResponseEntity.ok(updatedMovie) : ResponseEntity.notFound().build();
        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        }

        @PostMapping("/api/updateAvailability/{id}")
        public ResponseEntity<Movie> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
            Movie updatedMovie = movieService.updateAvailability(id, available);
            return updatedMovie != null ? ResponseEntity.ok(updatedMovie) : ResponseEntity.notFound().build();
        }

        @GetMapping("/search")
        public List<Movie> searchMovies(@RequestParam String keyword) {
            return movieService.searchMovies(keyword);
        }

    }

