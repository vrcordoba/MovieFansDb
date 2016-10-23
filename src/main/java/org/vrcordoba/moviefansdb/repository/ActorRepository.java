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

    List<Actor> findByNameContainingAndCreatorAllIgnoreCase(String name, String creator);

    List<Actor> findByNameContainingIgnoreCase(String name);

    List<Actor> findByCreator(String creator);
}
