package org.vrcordoba.moviefansdb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.vrcordoba.moviefansdb.domain.Actor;
import org.vrcordoba.moviefansdb.domain.CrewMember;
import org.vrcordoba.moviefansdb.repository.ActorRepository;
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
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing Actor.
 */
@RestController
@RequestMapping("/api")
public class ActorResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);
        
    @Inject
    private ActorRepository actorRepository;

    /**
     * POST  /actors : Create a new actor.
     *
     * @param actor the actor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actor, or with status 400 (Bad Request) if the actor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/actors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) throws URISyntaxException {
        log.debug("REST request to save Actor : {}", actor);
        if (actor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("actor", "idexists", "A new actor cannot already have an ID")).body(null);
        }
        Actor result = actorRepository.save(actor);
        return ResponseEntity.created(new URI("/api/actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("actor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actors : Updates an existing actor.
     *
     * @param actor the actor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actor,
     * or with status 400 (Bad Request) if the actor is not valid,
     * or with status 500 (Internal Server Error) if the actor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/actors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actor> updateActor(@Valid @RequestBody Actor actor) throws URISyntaxException {
        log.debug("REST request to update Actor : {}", actor);
        if (actor.getId() == null) {
            return createActor(actor);
        }
        Actor result = actorRepository.save(actor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("actor", actor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actors : get all the actors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of actors in body
     */
    @RequestMapping(value = "/actors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Actor> getAllActors(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String creator) {
        List<Actor> actors = null;
        if(Objects.nonNull(name) && Objects.nonNull(creator)) {
            log.debug("REST request to get all Actors by name and creator");
            actors = actorRepository.findByNameContainingAndCreator(name, creator);
        } else if(Objects.nonNull(name)) {
            log.debug("REST request to get all Actors by name");
            actors = actorRepository.findByNameContaining(name);
        } else if(Objects.nonNull(creator)) {
            log.debug("REST request to get all Actors by creator");
            actors = actorRepository.findByCreator(creator);
        } else {
            log.debug("REST request to get all Actors");
            actors = actorRepository.findAll();
        }
        return actors;
    }

    /**
     * GET  /actors/:id : get the "id" actor.
     *
     * @param id the id of the actor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actor, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/actors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actor> getActor(@PathVariable Long id) {
        log.debug("REST request to get Actor : {}", id);
        Actor actor = actorRepository.findOne(id);
        return Optional.ofNullable(actor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /actors/:id : delete the "id" actor.
     *
     * @param id the id of the actor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/actors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.debug("REST request to delete Actor : {}", id);
        actorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("actor", id.toString())).build();
    }

}
