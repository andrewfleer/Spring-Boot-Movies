package com.andrewfleer.movies.service;

import com.andrewfleer.movies.dto.MovieDTO;

import java.util.List;

public interface MovieService {

    List<MovieDTO> listAllMovies(Integer pageNo);

    List<MovieDTO> getMoviesByYear(Integer year, Integer pageNo);

    List<MovieDTO> getMoviesByGenre(String genre, Integer page);
}
