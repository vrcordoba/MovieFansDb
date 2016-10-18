package org.vrcordoba.moviefansdb.web.rest.clients;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import org.springframework.web.client.RestTemplate;
import org.vrcordoba.moviefansdb.domain.Movie;
import org.vrcordoba.moviefansdb.security.SecurityUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class MovieFetcher {

    private final ImdbIdFetcher movieImdbFetcher;
    private RestTemplate restTemplate;

    private static final String OMDB_SEARCH_URL =
        "http://www.omdbapi.com/?plot=full&r=json&i=";

    public MovieFetcher(final String movieName) {
        movieImdbFetcher = new ImdbIdFetcher(
            ImdbQueryType.MOVIE,
            prepareForQuery(movieName));
        restTemplate = new RestTemplate();
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

      //TODO: fill these fields
      /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d LLL yyyy")
          .withLocale(Locale.US);
      movie.setDate(LocalDate.parse(data.get("Released").asText(), formatter));*/
      movie.setDate(null);
      movie.setCasts(null);
      movie.setDirector(null);
      movie.setReviews(null);

      return movie;
    }
}
