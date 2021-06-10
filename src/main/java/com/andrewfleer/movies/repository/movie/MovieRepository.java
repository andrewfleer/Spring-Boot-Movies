package com.andrewfleer.movies.repository.movie;

import com.andrewfleer.movies.entity.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {
    @Query("SELECT m FROM Movie m WHERE m.releaseDate LIKE :year")
    Page<Movie> findAllByYear(Pageable paging, @Param("year") String year);

    @Query("SELECT m FROM Movie m WHERE m.genres LIKE :genre")
    Page<Movie> findAllByGenre(Pageable paging, @Param("genre") String genre);

    @Query("SELECT m FROM Movie m WHERE UPPER(m.title) = :title")
    Optional<Movie> findByTitle(@Param("title") String title);
}
