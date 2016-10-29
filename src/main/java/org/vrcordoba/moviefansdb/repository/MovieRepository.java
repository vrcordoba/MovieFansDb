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

    @Query("select distinct m from Movie m left join fetch m.casts "
        + "where (locate(lower(?1), lower(m.title)) > 0)")
    List<Movie> findByTitleContainingIgnoreCase(String title);

    @Query("select m from Movie m left join fetch m.casts c where c.id = ?1")
    List<Movie> findByCasts_Id(Long actorId);

    @Query("select distinct m from Movie m left join fetch m.casts where m.director.id =?1")
    List<Movie> findByDirector_Id(Long directorId);

    @Query("select distinct m from Movie m left join fetch m.casts "
        + "where (locate(lower(?1), lower(m.title)) > 0) and m.director.id =?2")
    List<Movie> findByTitleContainingIgnoreCaseAndDirector_Id(String title, Long directorId);

    @Query("select m from Movie m left join fetch m.casts c "
        + "where (locate(lower(?1), lower(m.title)) > 0) and c.id = ?2")
    List<Movie> findByTitleContainingIgnoreCaseAndCasts_Id(String title, Long actorId);

    @Query("select m from Movie m left join fetch m.casts c where c.id = ?1 and m.director.id = ?2")
    List<Movie> findByCasts_IdAndDirector_Id(Long actorId, Long directorId);

    @Query("select m from Movie m left join fetch m.casts c "
        + "where (locate(lower(?1), lower(m.title)) > 0) and c.id = ?2 and m.director.id = ?3")
    List<Movie> findByTitleContainingIgnoreCaseAndCasts_IdAndDirector_Id(
        String title,
        Long actorId,
        Long directorId);
}
