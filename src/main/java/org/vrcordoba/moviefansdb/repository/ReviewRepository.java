package org.vrcordoba.moviefansdb.repository;

import org.vrcordoba.moviefansdb.domain.Review;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Review entity.
 */
@SuppressWarnings("unused")
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select review from Review review where review.author.login = ?#{principal.username}")
    List<Review> findByAuthorIsCurrentUser();

}
