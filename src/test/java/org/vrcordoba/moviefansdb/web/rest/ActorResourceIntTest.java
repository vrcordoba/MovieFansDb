package org.vrcordoba.moviefansdb.web.rest;

import org.vrcordoba.moviefansdb.MovieFansDbApp;

import org.vrcordoba.moviefansdb.domain.Actor;
import org.vrcordoba.moviefansdb.repository.ActorRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ActorResource REST controller.
 *
 * @see ActorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieFansDbApp.class)
public class ActorResourceIntTest {

    private static final String DEFAULT_IMDB_ID = "AAAAA";
    private static final String UPDATED_IMDB_ID = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_BIOGRAPHY = "AAAAA";
    private static final String UPDATED_BIOGRAPHY = "BBBBB";

    @Inject
    private ActorRepository actorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restActorMockMvc;

    private Actor actor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActorResource actorResource = new ActorResource();
        ReflectionTestUtils.setField(actorResource, "actorRepository", actorRepository);
        this.restActorMockMvc = MockMvcBuilders.standaloneSetup(actorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actor createEntity(EntityManager em) {
        Actor actor = new Actor()
                .imdbId(DEFAULT_IMDB_ID)
                .name(DEFAULT_NAME)
                .biography(DEFAULT_BIOGRAPHY);
        return actor;
    }

    @Before
    public void initTest() {
        actor = createEntity(em);
    }

    @Test
    @Transactional
    public void createActor() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor

        restActorMockMvc.perform(post("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actor)))
                .andExpect(status().isCreated());

        // Validate the Actor in the database
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeCreate + 1);
        Actor testActor = actors.get(actors.size() - 1);
        assertThat(testActor.getImdbId()).isEqualTo(DEFAULT_IMDB_ID);
        assertThat(testActor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActor.getBiography()).isEqualTo(DEFAULT_BIOGRAPHY);
    }

    @Test
    @Transactional
    public void checkImdbIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setImdbId(null);

        // Create the Actor, which fails.

        restActorMockMvc.perform(post("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actor)))
                .andExpect(status().isBadRequest());

        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActors() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actors
        restActorMockMvc.perform(get("/api/actors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
                .andExpect(jsonPath("$.[*].imdbId").value(hasItem(DEFAULT_IMDB_ID.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].biography").value(hasItem(DEFAULT_BIOGRAPHY.toString())));
    }

    @Test
    @Transactional
    public void getActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", actor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actor.getId().intValue()))
            .andExpect(jsonPath("$.imdbId").value(DEFAULT_IMDB_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.biography").value(DEFAULT_BIOGRAPHY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActor() throws Exception {
        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor
        Actor updatedActor = actorRepository.findOne(actor.getId());
        updatedActor
                .imdbId(UPDATED_IMDB_ID)
                .name(UPDATED_NAME)
                .biography(UPDATED_BIOGRAPHY);

        restActorMockMvc.perform(put("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedActor)))
                .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actors.get(actors.size() - 1);
        assertThat(testActor.getImdbId()).isEqualTo(UPDATED_IMDB_ID);
        assertThat(testActor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActor.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
    }

    @Test
    @Transactional
    public void deleteActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);
        int databaseSizeBeforeDelete = actorRepository.findAll().size();

        // Get the actor
        restActorMockMvc.perform(delete("/api/actors/{id}", actor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
