package com.andrewfleer.movies.repository;

import com.andrewfleer.movies.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {
    @Query("select m from Movie m where m.releaseDate like :yearParam")
    Page<Movie> findAllByYear(Pageable paging, @Param("yearParam") String yearParam);

    @Query("select m from Movie m where m.genres like :genre")
    Page<Movie> findAllByGenre(Pageable paging, @Param("genre") String genre);
}
