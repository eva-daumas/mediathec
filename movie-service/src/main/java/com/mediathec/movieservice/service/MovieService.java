package com.mediathec.movieservice.service;

import com.mediathec.movieservice.entity.Movie;
import com.mediathec.movieservice.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;


    @Service
    public class MovieService {

        private final MovieRepository movieRepository;

        public MovieService(MovieRepository movieRepository) {
            this.movieRepository = movieRepository;
        }

        public Movie createMovie(Movie movie) {
            movie.setAvailable(true);
            return movieRepository.save(movie);
        }

        public List<Movie> getAllMovies() {
            return movieRepository.findAll();
        }

        public Movie getMovieById(Long id) {
            return movieRepository.findById(id).orElse(null);
        }

        public List<Movie> getMoviesByCategory(String category) {
            return movieRepository.findByCategory(category);
        }

        public List<Movie> searchMovies(String keyword) {
            return movieRepository.findByTitleContainingIgnoreCase(keyword);
        }

        public List<Movie> getAvailableMovies() {
            return movieRepository.findByAvailableTrue();
        }

        public Movie updateMovie(Long id, Movie movieDetails) {
            return movieRepository.findById(id).map(movie -> {
                movie.setTitle(movieDetails.getTitle());
                movie.setAuthor(movieDetails.getAuthor());
                movie.setCategory(movieDetails.getCategory());
                movie.setDescription(movieDetails.getDescription());
                movie.setCoverImage(movieDetails.getCoverImage());
                return movieRepository.save(movie);
            }).orElse(null);
        }

        public void deleteMovie(Long id) {
            movieRepository.deleteById(id);
        }

        public Movie updateAvailability(Long id, boolean available) {
            return movieRepository.findById(id).map(movie -> {
                movie.setAvailable(available);
                return movieRepository.save(movie);
            }).orElse(null);
        }

        public Movie findByTitle(String title) {
            List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);
            return movies.isEmpty() ? null : movies.get(0);
        }
}
