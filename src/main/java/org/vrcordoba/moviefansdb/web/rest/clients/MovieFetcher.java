package org.vrcordoba.moviefansdb.web.rest.clients;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.web.client.RestTemplate;
import org.vrcordoba.moviefansdb.domain.Actor;
import org.vrcordoba.moviefansdb.domain.Director;
import org.vrcordoba.moviefansdb.domain.Movie;
import org.vrcordoba.moviefansdb.domain.Review;
import org.vrcordoba.moviefansdb.repository.ActorRepository;
import org.vrcordoba.moviefansdb.repository.DirectorRepository;
import org.vrcordoba.moviefansdb.security.SecurityUtils;
import org.vrcordoba.moviefansdb.web.rest.util.RestTemplateUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class MovieFetcher {

    private final ImdbIdFetcher movieImdbFetcher;
    private RestTemplate restTemplate;
    private ActorRepository actorRepository;
    private DirectorRepository directorRepository;

    public MovieFetcher(
        final String movieName,
        final ActorRepository actorRepository,
        final DirectorRepository directorRepository) {
        movieImdbFetcher = new ImdbIdFetcher(
            ImdbQueryType.MOVIE,
            RestTemplateUtil.prepareForQuery(movieName));
        restTemplate = new RestTemplate();
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
    }

    public Movie fetch() {
        Optional<String> movieImdbId = movieImdbFetcher.fetchId();
        Movie movie = null;
        if (movieImdbId.isPresent()) {
            ObjectNode data = restTemplate.getForObject(
                RestTemplateUtil.OMDB_SEARCH_URL + movieImdbId.get(),
                ObjectNode.class);
            movie = parseJson(data);
        }
        return movie;
    }

    private Movie parseJson(final ObjectNode data) {
        Movie movie = new Movie();

        movie.setGenre(data.get("Genre").asText());
        movie.setImdbId(data.get("imdbID").asText());
        movie.setPlot(data.get("Plot").asText());
        movie.setRating((float) data.get("imdbRating").asDouble());
        movie.setTitle(data.get("Title").asText());
        movie.setCreator(SecurityUtils.getCurrentUserLogin());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
            .withLocale(Locale.US);
        movie.setDate(LocalDate.parse(data.get("Released").asText(), formatter));

        movie.setCasts(fetchCast(data.get("Actors").asText(), movie));
        movie.setDirector(fetchDirector(data.get("Director").asText(), movie));
        movie.setReviews(fetchReviews());

        return movie;
    }

    private Set<Actor> fetchCast(final String actorNames, final Movie movie) {
        String[] actors = actorNames.split(", ");
        Set<Actor> cast = new HashSet<>();
        CrewFetcher<Actor, ActorRepository> actorFetcher = new CrewFetcher<>(movie);
        for (String actor : actors) {
            Actor fetchedActor = new Actor();
            if(actorFetcher.fetch(actor, fetchedActor, actorRepository)) {
                cast.add(fetchedActor);
            }
        }
        return cast;
    }

    private Director fetchDirector(final String directorName, final Movie movie) {
        Director fetchedDirector = new Director();
        if(new CrewFetcher<>(movie).fetch(directorName, fetchedDirector, directorRepository)) {
            return fetchedDirector;
        } else {
            return null;
        }
    }

    private Set<Review> fetchReviews() {
        return null;
    }
}
