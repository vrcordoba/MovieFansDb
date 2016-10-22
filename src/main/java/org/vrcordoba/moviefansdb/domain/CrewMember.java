package org.vrcordoba.moviefansdb.domain;

import java.util.Set;

public interface CrewMember {

    Long getId();
    void setId(Long id);
    String getName();
    CrewMember name(String name);
    void setName(String name);
    String getBiography();
    CrewMember biography(String biography);
    void setBiography(String biography);
    String getImdbId();
    CrewMember imdbId(String imdbId);
    void setImdbId(String imdbId);
    Set<Movie> getMovies();
    CrewMember movies(Set<Movie> movies);
    CrewMember addMovies(Movie movie);
    CrewMember removeMovies(Movie movie);
    void setMovies(Set<Movie> movies);
    String getCreator();
    CrewMember creator(String creator);
    void setCreator(String creator);
}
