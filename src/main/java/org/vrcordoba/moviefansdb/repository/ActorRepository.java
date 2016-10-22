package org.vrcordoba.moviefansdb.repository;

import org.vrcordoba.moviefansdb.domain.Actor;
import org.vrcordoba.moviefansdb.domain.CrewMember;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Actor entity.
 */
public interface ActorRepository extends JpaRepository<Actor,Long>, CrewMemberRepository {

    List<CrewMember> findByImdbId(String imdbId);

    List<Actor> findByNameContainingAndCreator(String name, String Creator);

    List<Actor> findByNameContaining(String name);

    List<Actor> findByCreator(String Creator);
}
