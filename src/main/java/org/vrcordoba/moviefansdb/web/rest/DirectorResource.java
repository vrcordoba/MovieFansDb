package org.vrcordoba.moviefansdb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.vrcordoba.moviefansdb.domain.Director;

import org.vrcordoba.moviefansdb.repository.DirectorRepository;
import org.vrcordoba.moviefansdb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing Director.
 */
@RestController
@RequestMapping("/api")
public class DirectorResource {

    private final Logger log = LoggerFactory.getLogger(DirectorResource.class);
        
    @Inject
    private DirectorRepository directorRepository;

    /**
     * POST  /directors : Create a new director.
     *
     * @param director the director to create
     * @return the ResponseEntity with status 201 (Created) and with body the new director, or with status 400 (Bad Request) if the director has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/directors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Director> createDirector(@Valid @RequestBody Director director) throws URISyntaxException {
        log.debug("REST request to save Director : {}", director);
        if (director.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("director", "idexists", "A new director cannot already have an ID")).body(null);
        }
        Director result = directorRepository.save(director);
        return ResponseEntity.created(new URI("/api/directors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("director", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /directors : Updates an existing director.
     *
     * @param director the director to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated director,
     * or with status 400 (Bad Request) if the director is not valid,
     * or with status 500 (Internal Server Error) if the director couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/directors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Director> updateDirector(@Valid @RequestBody Director director) throws URISyntaxException {
        log.debug("REST request to update Director : {}", director);
        if (director.getId() == null) {
            return createDirector(director);
        }
        Director result = directorRepository.save(director);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("director", director.getId().toString()))
            .body(result);
    }

    /**
     * GET  /directors : get all the directors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of directors in body
     */
    @RequestMapping(value = "/directors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Director> getAllDirectors() {
        log.debug("REST request to get all Directors");
        List<Director> directors = directorRepository.findAll();
        return directors;
    }

    /**
     * GET  /directors/:id : get the "id" director.
     *
     * @param id the id of the director to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the director, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/directors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Director> getDirector(@PathVariable Long id) {
        log.debug("REST request to get Director : {}", id);
        Director director = directorRepository.findOne(id);
        return Optional.ofNullable(director)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /directors/:id : delete the "id" director.
     *
     * @param id the id of the director to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/directors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDirector(@PathVariable Long id) {
        log.debug("REST request to delete Director : {}", id);
        directorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("director", id.toString())).build();
    }

}
