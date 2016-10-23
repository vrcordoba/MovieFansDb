package org.vrcordoba.moviefansdb.repository;

import org.vrcordoba.moviefansdb.domain.Review;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Review entity.
 */
public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByAuthorIgnoreCase(String author);

    List<Review> findByDateGreaterThan(LocalDate date);

    @Query("select r from Review r where r.movie.id = ?1")
    List<Review> findByMovieId(Long movieId);

    List<Review> findByAuthorIgnoreCaseAndDateGreaterThan(String author, LocalDate date);

    @Query("select r from Review r where lower(r.author) = lower(?1) and r.movie.id = ?2")
    List<Review> findByAuthorIgnoreCaseAndMovieId(String author, Long movieId);

    @Query("select r from Review r where lower(r.author) = lower(?1) and r.date > ?2 and r.movie.id = ?3")
    List<Review> findByAuthorIgnoreCaseAndDateGreaterThanAndMovieId(
        String author,
        LocalDate date,
        Long movieId);

    @Query("select r from Review r where r.date > ?1 and r.movie.id = ?2")
    List<Review> findByDateGreaterThanAndMovieId(LocalDate date, Long movieId);
}
