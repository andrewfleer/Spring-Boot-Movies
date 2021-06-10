package com.andrewfleer.movies.service.impl;

import com.andrewfleer.movies.dto.GenreDTO;
import com.andrewfleer.movies.dto.MovieDTO;
import com.andrewfleer.movies.dto.MovieDetailsDTO;
import com.andrewfleer.movies.dto.ProductionCompanyDTO;
import com.andrewfleer.movies.entity.movie.Movie;
import com.andrewfleer.movies.entity.rating.Rating;
import com.andrewfleer.movies.repository.movie.MovieRepository;
import com.andrewfleer.movies.repository.rating.RatingRepository;
import com.andrewfleer.movies.service.MovieService;
import com.andrewfleer.movies.util.CommonConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {

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
        Pageable paging = PageRequest.of(pageNo, CommonConstants.PAGE_SIZE);
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
        Pageable paging = PageRequest.of(pageNo, CommonConstants.PAGE_SIZE, Sort.by("releaseDate").descending());
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
        Pageable paging = PageRequest.of(pageNo, CommonConstants.PAGE_SIZE);

        genre = "%\"" + genre + "\"%";
        Page<Movie> pagedResult = movieRepository.findAllByGenre(paging, genre);

        if (pagedResult.hasContent()) {
            return convertEntityToDTO(pagedResult.getContent());
        } else {
            return new ArrayList<MovieDTO>();
        }
    }

    /**
     * Returns a DTO of the movie details and an average rating score.
     * @param movieId - The movie ID in the database
     * @param title - The movie title
     * @return - a Movie Details DTO
     */
    @Override
    public MovieDetailsDTO getMovieDetails(Integer movieId, String title) {
        Movie movie = null;
        if (movieId != null) {
            movie = movieRepository.findById(movieId).orElse(null);
        } else {
            // If the ID is null, assume a title exists
            // Convert to upper case to ensure improper capitalization doesn't affect the search.
            movie = movieRepository.findByTitle(title.toUpperCase()).orElse(null);
        }

        if (movie == null) {
            throw new EntityNotFoundException("Could not find a movie to match your criteria!");
        }

        // We found the movie, now let's get the ratings.
        Set<Rating> ratings = ratingRepository.findByMovieId(movie.getMovieId());
        BigDecimal averageRating = calculateAverageRating(ratings);

        MovieDetailsDTO movieDetails = convertEntityToDTO(movie, averageRating);

        return movieDetails;
    }

    /**
     * Converts the entities to a DTO. It's cleaner this way and helps preserve data integrity.
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

    /**
     * Converts the movie entity into a Movie Details DTO to preserve data integrity.
     * @param movie - The Movie Object from the database
     * @param averageRating - The average rating for the movie
     * @return - A DTO object
     */
    private MovieDetailsDTO convertEntityToDTO(Movie movie, BigDecimal averageRating) {
        MovieDetailsDTO movieDetails = new MovieDetailsDTO();
        MovieDTO movieDto = new MovieDTO();
        movieDto.setImdbId(movie.getImdbId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setOverview(movie.getOverview());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setBudget("$" + movie.getBudget().toString());
        movieDto.setRuntime(movie.getRuntime());
        movieDto.setLanguage(movie.getLanguage());

        try {
            // Convert the genres String to a JSON object.
            Set<GenreDTO> genres = mapper.readValue(movie.getGenres(), Set.class);
            movieDto.setGenres(genres);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Convert the production company String into a JSON object.
            Set<ProductionCompanyDTO> productionCompanies = mapper.readValue(movie.getProductionCompanies(), Set.class);
            movieDto.setProductionCompanies(productionCompanies);
        } catch (Exception e) {
            e.printStackTrace();
        }


        movieDetails.setMovie(movieDto);
        movieDetails.setAverageRating(averageRating);

        return movieDetails;
    }

    /**
     * Calculates the average ratings.
     * Sums all the scores together and then divides by the total number of scores.
     *
     * @param ratings - A set of ratings
     * @return - A Big Decimal representing the average score. Returns 0 if none exist.
     */
    private BigDecimal calculateAverageRating(Set<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal sumOfRatings = BigDecimal.ZERO;
            for (Rating rating : ratings) {
                sumOfRatings = sumOfRatings.add(rating.getRating());
            }

            BigDecimal averageRating = sumOfRatings.divide(new BigDecimal(ratings.size()),2,RoundingMode.HALF_UP);

            return averageRating;
        }
    }
}
