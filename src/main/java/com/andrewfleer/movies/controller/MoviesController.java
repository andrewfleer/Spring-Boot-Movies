package com.andrewfleer.movies.controller;

import com.andrewfleer.movies.dto.MovieDTO;
import com.andrewfleer.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MoviesController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/listAllMovies")
    public List<MovieDTO> listAllMovies(@RequestParam(defaultValue = "0") Integer page){
        return movieService.listAllMovies(page);
    }

    @GetMapping("/moviesByYear")
    public List<MovieDTO> moviesByYear(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(required = true) Integer year) throws Exception {
        return movieService.getMoviesByYear(year, page);
    }

    @GetMapping("/moviesByGenre")
    public List<MovieDTO> moviesByGenre(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(required = true) String genre) throws Exception {
        return movieService.getMoviesByGenre(genre, page);
    }
}
