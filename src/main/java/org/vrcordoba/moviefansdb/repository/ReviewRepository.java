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

    List<Review> findByMovie_Id(Long movieId);

    List<Review> findByAuthorIgnoreCaseAndDateGreaterThan(String author, LocalDate date);

    List<Review> findByAuthorIgnoreCaseAndMovie_Id(String author, Long movieId);

    List<Review> findByAuthorIgnoreCaseAndDateGreaterThanAndMovie_Id(
        String author,
        LocalDate date,
        Long movieId);

    List<Review> findByDateGreaterThanAndMovie_Id(LocalDate date, Long movieId);
}
