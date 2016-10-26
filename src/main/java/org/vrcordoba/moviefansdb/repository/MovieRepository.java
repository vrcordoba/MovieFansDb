package org.vrcordoba.moviefansdb.repository;

import org.vrcordoba.moviefansdb.domain.Movie;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Movie entity.
 */
public interface MovieRepository extends JpaRepository<Movie,Long> {

    @Query("select distinct movie from Movie movie left join fetch movie.casts")
    List<Movie> findAllWithEagerRelationships();

    @Query("select movie from Movie movie left join fetch movie.casts where movie.id =:id")
    Movie findOneWithEagerRelationships(@Param("id") Long id);

    List<Movie> findByImdbId(String imdbId);

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByCasts_Id(Long actorId);

    List<Movie> findByDirector_Id(Long directorId);

    List<Movie> findByTitleContainingIgnoreCaseAndDirector_Id(String title, Long directorId);

    List<Movie> findByTitleContainingIgnoreCaseAndCasts_Id(String title, Long actorId);

    List<Movie> findByCasts_IdAndDirector_Id(Long actorId, Long directorId);

    List<Movie> findByTitleContainingIgnoreCaseAndCasts_IdAndDirector_Id(
        String title,
        Long actorId,
        Long directorId);
}
