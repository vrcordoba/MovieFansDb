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

import com.fasterxml.jackson.databind.node.ObjectNode;

public class MovieFetcher {

    private final ImdbIdFetcher movieImdbFetcher;
    private RestTemplate restTemplate;
    private ActorRepository actorRepository;
    private DirectorRepository directorRepository;

    private static final String OMDB_SEARCH_URL =
        "http://www.omdbapi.com/?plot=full&r=json&i=";

    public MovieFetcher(
        final String movieName,
        final ActorRepository actorRepository,
        final DirectorRepository directorRepository) {
        movieImdbFetcher = new ImdbIdFetcher(
            ImdbQueryType.MOVIE,
            prepareForQuery(movieName));
        restTemplate = new RestTemplate();
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
    }

    private String prepareForQuery(String inputString) {
        return inputString.replaceAll("\\p{Space}", "+");
    }

    public Optional<Movie> fetchMovie() {
        Optional<String> movieImdbId = movieImdbFetcher.fetchId();
        Optional<Movie> optionalMovie = Optional.empty();
        if (movieImdbId.isPresent()) {
            ObjectNode data = restTemplate.getForObject(
                OMDB_SEARCH_URL + movieImdbId.get(),
                ObjectNode.class);
            optionalMovie = Optional.of(parseJsonMovie(data));
        }
        return optionalMovie;
    }

    private Movie parseJsonMovie(final ObjectNode data) {
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
      movie.setDirector(fetchDirector(data.get("Director").asText()));
      movie.setReviews(fetchReviews());

      return movie;
    }

    private Set<Actor> fetchCast(final String actorNames, final Movie movie) {
        String[] actors = actorNames.split(", ");
        Set<Actor> cast = new HashSet<>();
        for (String actor : actors) {
            Actor fetchedActor = new ActorFetcher(actor, actorRepository).fetch();
            if (Objects.nonNull(fetchedActor)) {
                Set<Movie> currentMovies = fetchedActor.getMovies();
                currentMovies.add(movie);
                fetchedActor.setMovies(currentMovies);
                cast.add(fetchedActor);
            }
        }
        return cast;
    }

    private Director fetchDirector(final String directorName) {
        return null;
    }

    private Set<Review> fetchReviews() {
        return null;
    }
}
