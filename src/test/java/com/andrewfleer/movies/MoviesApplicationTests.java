package com.andrewfleer.movies;

import com.andrewfleer.movies.dto.MovieDTO;
import com.andrewfleer.movies.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
class MoviesApplicationTests {

    @Autowired
    private MovieService movieService;

    @Test
    public void should_find_movies() {
        List<MovieDTO> movies = movieService.listAllMovies(0);

        int zero_movies = 0;
        assertThat(movies).hasSizeGreaterThan(zero_movies);
    }

    @Test
    public void should_find_movies_in_2003() {
        List<MovieDTO> movies = movieService.getMoviesByYear(2003, 0);

        int zero_movies = 0;
        assertThat(movies).hasSizeGreaterThan(zero_movies);
    }

    @Test
    public void should_find_no_movies_in_1000() {
        List<MovieDTO> movies = movieService.getMoviesByYear(1000, 0);

        int zero_movies = 0;
        assertThat(movies).hasSize(zero_movies);
    }

    @Test
    public void should_find_comedy_movies() {
        List<MovieDTO> movies = movieService.getMoviesByGenre("Comedy", 0);

        int zero_movies = 0;
        assertThat(movies).hasSizeGreaterThan(zero_movies);
    }

    @Test
    public void should_find_no_foobar_movies() {
        List<MovieDTO> movies = movieService.getMoviesByGenre("foobar", 0);

        int zero_movies = 0;
        assertThat(movies).hasSize(zero_movies);
    }
}
