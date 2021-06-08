package com.andrewfleer.movies.entity.rating;

import com.andrewfleer.movies.entity.movie.Movie;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    private Integer ratingId;

    private Integer userId;

    private Integer movieId;

    @Column(columnDefinition = "REAL")
    private BigDecimal rating;

    @Column(columnDefinition = "INTEGER")
    private Long timestamp;

    @ManyToOne
    @JoinColumn(name="movieId")
    private Movie movie;

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
