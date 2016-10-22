package org.vrcordoba.moviefansdb.repository;

import java.util.List;

import org.vrcordoba.moviefansdb.domain.CrewMember;

public interface CrewMemberRepository {

    List<CrewMember> findByImdbId(String imdbId);
}
