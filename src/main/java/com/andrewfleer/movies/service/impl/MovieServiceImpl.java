package com.andrewfleer.movies.service.impl;

import com.andrewfleer.movies.dto.Genre;
import com.andrewfleer.movies.dto.MovieDTO;
import com.andrewfleer.movies.entity.Movie;
import com.andrewfleer.movies.repository.MovieRepository;
import com.andrewfleer.movies.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {
    private static final int PAGE_SIZE = 50;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper mapper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

    @Override
    public List<MovieDTO> getMoviesByYear(Integer year, Integer pageNo) throws Exception {
        Pageable paging = PageRequest.of(pageNo, PAGE_SIZE, Sort.by("releaseDate").descending());
        String yearParam = year + "-%";
        Page<Movie> pagedResult = movieRepository.findAllByYear(paging, yearParam);

        if (pagedResult.hasContent()) {
            return convertEntityToDTO(pagedResult.getContent());
        } else {
            return new ArrayList<MovieDTO>();
        }
    }

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

    private List<MovieDTO> convertEntityToDTO(List<Movie> content) {
        List<MovieDTO> returnList = new ArrayList<MovieDTO>();
        for (Movie entity : content) {
            MovieDTO dto = new MovieDTO();
            dto.setImdbId(entity.getImdbId());
            dto.setTitle(entity.getTitle());
            dto.setReleaseDate(entity.getReleaseDate().toString());
            dto.setBudget("$" + entity.getBudget().toString());

            try {
                Set<Genre> genres = mapper.readValue(entity.getGenres(), Set.class);
                dto.setGenres(genres);
            } catch (Exception e) {
                e.printStackTrace();
            }
            returnList.add(dto);
        }

        return returnList;
    }
}
