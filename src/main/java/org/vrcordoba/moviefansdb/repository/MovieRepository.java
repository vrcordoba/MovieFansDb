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

    @Query("select m from Movie m where m.director.id = ?1")
    List<Movie> findByDirectorId(Long directorId);

    @Query("select m from Movie m where (locate(lower(?1), lower(m.title)) > 0) and m.director.id = ?2")
    List<Movie> findByTitleContainingIgnoreCaseAndDirectorId(String title, Long directorId);
}
