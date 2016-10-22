package org.vrcordoba.moviefansdb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.vrcordoba.moviefansdb.domain.Movie;
import org.vrcordoba.moviefansdb.repository.ActorRepository;
import org.vrcordoba.moviefansdb.repository.DirectorRepository;
import org.vrcordoba.moviefansdb.repository.MovieRepository;
import org.vrcordoba.moviefansdb.web.rest.clients.MovieFetcher;
import org.vrcordoba.moviefansdb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Movie.
 */
@RestController
@RequestMapping("/api")
public class MovieResource {

    private final Logger log = LoggerFactory.getLogger(MovieResource.class);
        
    @Inject
    private MovieRepository movieRepository;

    @Inject
    private ActorRepository actorRepository;

    @Inject
    private DirectorRepository directorRepository;

    /**
     * POST  /movies : Create a new movie.
     *
     * @param movie the movie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movie, or with status 400 (Bad Request) if the movie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/movies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to save Movie : {}", movie);
        if (movie.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "idexists", "A new movie cannot already have an ID")).body(null);
        }
        Movie result = movieRepository.save(movie);
        return ResponseEntity.created(new URI("/api/movies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("movie", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /movies/fetcher : Create/Update a movie taking information from Internet.
     *
     * @param movieName the name of the movie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movie, or with status 404 (Not found) if the movie is not found
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/movies/fetcher",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movie> fetchMovie(@Valid @RequestBody String movieName) throws URISyntaxException {
        log.debug("REST request to fetch Movie : {}", movieName);
        MovieFetcher movieFetcher = new MovieFetcher(
            movieName,
            actorRepository,
            directorRepository);
        Optional<Movie> movie = movieFetcher.fetchMovie();
        if (movie.isPresent()) {
            Movie fetchedMovie = movie.get();
            List<Movie> recoveredMovies = movieRepository.findByImdbId(fetchedMovie.getImdbId());
            if (!recoveredMovies.isEmpty()) {
                fetchedMovie.setId(recoveredMovies.get(0).getId());
                return updateMovie(fetchedMovie);
            }
            return createMovie(fetchedMovie);
        }
        return ResponseEntity.badRequest().headers(
            HeaderUtil.createFailureAlert(
                "movie",
                "idnotfound",
                String.format("No movie could be fetched with %s name", movieName)))
            .body(null);
    }

    /**
     * PUT  /movies : Updates an existing movie.
     *
     * @param movie the movie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movie,
     * or with status 400 (Bad Request) if the movie is not valid,
     * or with status 500 (Internal Server Error) if the movie couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/movies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movie> updateMovie(@Valid @RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to update Movie : {}", movie);
        if (movie.getId() == null) {
            return createMovie(movie);
        }
        Movie result = movieRepository.save(movie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("movie", movie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movies : get all the movies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of movies in body
     */
    @RequestMapping(value = "/movies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Movie> getAllMovies() {
        log.debug("REST request to get all Movies");
        List<Movie> movies = movieRepository.findAllWithEagerRelationships();
        return movies;
    }

    /**
     * GET  /movies/:id : get the "id" movie.
     *
     * @param id the id of the movie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movie, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/movies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        log.debug("REST request to get Movie : {}", id);
        Movie movie = movieRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(movie)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /movies/:id : delete the "id" movie.
     *
     * @param id the id of the movie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/movies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.debug("REST request to delete Movie : {}", id);
        movieRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("movie", id.toString())).build();
    }

}
