package com.andrewfleer.movies.controller;

import com.andrewfleer.movies.dto.MovieDTO;
import com.andrewfleer.movies.dto.MovieDetailsDTO;
import com.andrewfleer.movies.entity.rating.Rating;
import com.andrewfleer.movies.service.MovieService;
import com.andrewfleer.movies.util.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MoviesController {

    @Autowired
    private MovieService movieService;

    /**
     * Gets a paginated list of all the movies.
     * @param page - Which page number to return, default is 0
     * @return - A list of Movie DTOs
     */
    @GetMapping(CommonConstants.LIST_ALL_MOVIES)
    public List<MovieDTO> listAllMovies(@RequestParam(defaultValue = "0") Integer page){
        return movieService.listAllMovies(page);
    }

    /**
     * Gets a paginated list of all the movies for a given year.
     * Will throw an error if a year is not provided.
     * @param page - Which page number to return, default is 0
     * @param year - Which year to search for
     * @return - A list of Movie DTOs
     * @throws Exception
     */
    @GetMapping(CommonConstants.MOVIES_BY_YEAR)
    public List<MovieDTO> moviesByYear(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(required = true) Integer year) throws Exception {
        return movieService.getMoviesByYear(year, page);
    }

    /**
     * Gets a paginated list of all the movies for a given genre.
     * Will throw an error if a genre is not provided.
     * @param page - Which page number to return, default is 0
     * @param genre - Which genre to search for
     * @return - A list of Movie DTOs
     * @throws Exception
     */
    @GetMapping(CommonConstants.MOVIES_BY_GENRE)
    public List<MovieDTO> moviesByGenre(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(required = true) String genre) throws Exception {
        return movieService.getMoviesByGenre(genre, page);
    }

    /**
     * Gets the details for a specific movie.
     * If an ID is sent, we will search by that.
     * If a title is sent, we will return the result for the first movie we find that matches that title.
     * @param movieId - The ID of the movie in the database
     * @param title - The Title of the movie
     * @return - A Movie Details DTO
     * @throws Exception
     */
    @GetMapping(CommonConstants.MOVIE_DETAILS)
    public MovieDetailsDTO getMovieDetails(@RequestParam(required = false) Integer movieId,
                                           @RequestParam(required = false) String title) throws Exception{
        if (movieId == null && title == null)
            throw new Exception("Error! Both movie ID and movie title are null!");

        return movieService.getMovieDetails(movieId, title);

    }
}
