package org.vrcordoba.moviefansdb.repository;

import org.vrcordoba.moviefansdb.domain.Director;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Director entity.
 */
@SuppressWarnings("unused")
public interface DirectorRepository extends JpaRepository<Director,Long> {

}
