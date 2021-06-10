package com.andrewfleer.movies.repository.rating;

import com.andrewfleer.movies.entity.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query("SELECT r FROM Rating r WHERE r.movieId = :movieId")
    Set<Rating> findByMovieId(@Param("movieId") int movieId);
}
