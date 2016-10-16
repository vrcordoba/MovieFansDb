package org.vrcordoba.moviefansdb.web.rest.util;

import org.springframework.web.client.RestTemplate;
import org.vrcordoba.moviefansdb.domain.Actor;
import org.vrcordoba.moviefansdb.domain.Director;
import org.vrcordoba.moviefansdb.domain.Movie;
import org.vrcordoba.moviefansdb.domain.User;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class MovieFetcher {

    private final String movieName;

    public MovieFetcher(final String movieName) {
        this.movieName = prepareForQuery(movieName);
    }

    private String prepareForQuery(String inputString) {
        return inputString.replaceAll("\\p{Space}", "+");
    }

    public Movie fetchMovie() {
      return null;
    }

    private Actor fetchActors() {
        return null;
    }

    private Director fetchDirector() {
        return null;
    }

    private User fetchCreator() {
        return null;
    }
}
