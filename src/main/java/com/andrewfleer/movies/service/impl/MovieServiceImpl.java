package com.andrewfleer.movies.service.impl;

import com.andrewfleer.movies.dto.GenreDTO;
import com.andrewfleer.movies.dto.MovieDTO;
import com.andrewfleer.movies.entity.movie.Movie;
import com.andrewfleer.movies.entity.rating.Rating;
import com.andrewfleer.movies.repository.movie.MovieRepository;
import com.andrewfleer.movies.repository.rating.RatingRepository;
import com.andrewfleer.movies.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {
    private static final int PAGE_SIZE = 50;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ObjectMapper mapper;

    /**
     * Returns all movies in a paged set. The page size is currently a static value.
     * Future enhancements could include letting the user set the page size in their request.
     * @param pageNo - Which page number to return. Default is 0
     * @return - Return a list of Movie DTOs
     */
    @Override
    public List<MovieDTO> listAllMovies(Integer pageNo) {
        Pageable paging = PageRequest.of(pageNo, PAGE_SIZE);
        Page<Movie> pagedResult =  movieRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return convertEntityToDTO(pagedResult.getContent());
        } else {
            return new ArrayList<MovieDTO>();
        }
    }

    /**
     * Returns a list of all movies for a given year in a paged set. The page size is currently a static value.
     * Future enhancements could include:
     *      - Letting the user set the page size in their request
     *      - Letting the user choose if they want to sort ascending or descending
     *      - Changing the year to be a calendar object instead of a String.
     *           - This could already be achievable, but I was struggling with SQLite.
     * @param year - The year the movie came out
     * @param pageNo - Which page number to return. Default is 0
     * @return - A list of Movie DTOs
     */
    @Override
    public List<MovieDTO> getMoviesByYear(Integer year, Integer pageNo) {
        Pageable paging = PageRequest.of(pageNo, PAGE_SIZE, Sort.by("releaseDate").descending());
        String yearParam = year + "-%";
        Page<Movie> pagedResult = movieRepository.findAllByYear(paging, yearParam);

        if (pagedResult.hasContent()) {
            return convertEntityToDTO(pagedResult.getContent());
        } else {
            return new ArrayList<MovieDTO>();
        }
    }

    /**
     * Returns a list of all movies for a given genre in a paged set. The page size is currently a static value.
     * Future enhancements could include:
     *      - Letting the user set the page size in their request
     *      - Letting the user select multiple genres
     * @param genre - A genre of movie
     * @param pageNo - Which page number to return. Default is 0
     * @return - A list of Movie DTOs
     */
    @Override
    public List<MovieDTO> getMoviesByGenre(String genre, Integer pageNo) {
        Pageable paging = PageRequest.of(pageNo, PAGE_SIZE);

        genre = "%\"" + genre + "\"%";
        Page<Movie> pagedResult = movieRepository.findAllByGenre(paging, genre);

        if (pagedResult.hasContent()) {
            return convertEntityToDTO(pagedResult.getContent());
        } else {
            return new ArrayList<MovieDTO>();
        }
    }

    /**
     * Converts the entities to a DTO. It's cleaner this was and helps preserve data integrity.
     * @param content - A list of movie entities
     * @return - A list of DTOs
     */
    private List<MovieDTO> convertEntityToDTO(List<Movie> content) {
        List<MovieDTO> returnList = new ArrayList<MovieDTO>();
        for (Movie entity : content) {
            MovieDTO dto = new MovieDTO();
            dto.setImdbId(entity.getImdbId());
            dto.setTitle(entity.getTitle());
            dto.setReleaseDate(entity.getReleaseDate().toString());
            dto.setBudget("$" + entity.getBudget().toString());

            try {
                // Convert the genres String to a JSON object.
                Set<GenreDTO> genres = mapper.readValue(entity.getGenres(), Set.class);
                dto.setGenres(genres);
            } catch (Exception e) {
                e.printStackTrace();
            }
            returnList.add(dto);
        }

        return returnList;
    }
}
