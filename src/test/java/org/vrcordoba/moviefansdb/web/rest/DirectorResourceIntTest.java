package org.vrcordoba.moviefansdb.web.rest;

import org.vrcordoba.moviefansdb.MovieFansDbApp;

import org.vrcordoba.moviefansdb.domain.Director;
import org.vrcordoba.moviefansdb.repository.DirectorRepository;

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
 * Test class for the DirectorResource REST controller.
 *
 * @see DirectorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieFansDbApp.class)
public class DirectorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_BIOGRAPHY = "AAAAA";
    private static final String UPDATED_BIOGRAPHY = "BBBBB";

    private static final String DEFAULT_IMDB_ID = "AAAAA";
    private static final String UPDATED_IMDB_ID = "BBBBB";

    @Inject
    private DirectorRepository directorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDirectorMockMvc;

    private Director director;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DirectorResource directorResource = new DirectorResource();
        ReflectionTestUtils.setField(directorResource, "directorRepository", directorRepository);
        this.restDirectorMockMvc = MockMvcBuilders.standaloneSetup(directorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Director createEntity(EntityManager em) {
        Director director = new Director()
                .name(DEFAULT_NAME)
                .biography(DEFAULT_BIOGRAPHY)
                .imdbId(DEFAULT_IMDB_ID);
        return director;
    }

    @Before
    public void initTest() {
        director = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirector() throws Exception {
        int databaseSizeBeforeCreate = directorRepository.findAll().size();

        // Create the Director

        restDirectorMockMvc.perform(post("/api/directors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(director)))
                .andExpect(status().isCreated());

        // Validate the Director in the database
        List<Director> directors = directorRepository.findAll();
        assertThat(directors).hasSize(databaseSizeBeforeCreate + 1);
        Director testDirector = directors.get(directors.size() - 1);
        assertThat(testDirector.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDirector.getBiography()).isEqualTo(DEFAULT_BIOGRAPHY);
        assertThat(testDirector.getImdbId()).isEqualTo(DEFAULT_IMDB_ID);
    }

    @Test
    @Transactional
    public void checkImdbIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = directorRepository.findAll().size();
        // set the field null
        director.setImdbId(null);

        // Create the Director, which fails.

        restDirectorMockMvc.perform(post("/api/directors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(director)))
                .andExpect(status().isBadRequest());

        List<Director> directors = directorRepository.findAll();
        assertThat(directors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDirectors() throws Exception {
        // Initialize the database
        directorRepository.saveAndFlush(director);

        // Get all the directors
        restDirectorMockMvc.perform(get("/api/directors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(director.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].biography").value(hasItem(DEFAULT_BIOGRAPHY.toString())))
                .andExpect(jsonPath("$.[*].imdbId").value(hasItem(DEFAULT_IMDB_ID.toString())));
    }

    @Test
    @Transactional
    public void getDirector() throws Exception {
        // Initialize the database
        directorRepository.saveAndFlush(director);

        // Get the director
        restDirectorMockMvc.perform(get("/api/directors/{id}", director.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(director.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.biography").value(DEFAULT_BIOGRAPHY.toString()))
            .andExpect(jsonPath("$.imdbId").value(DEFAULT_IMDB_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDirector() throws Exception {
        // Get the director
        restDirectorMockMvc.perform(get("/api/directors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirector() throws Exception {
        // Initialize the database
        directorRepository.saveAndFlush(director);
        int databaseSizeBeforeUpdate = directorRepository.findAll().size();

        // Update the director
        Director updatedDirector = directorRepository.findOne(director.getId());
        updatedDirector
                .name(UPDATED_NAME)
                .biography(UPDATED_BIOGRAPHY)
                .imdbId(UPDATED_IMDB_ID);

        restDirectorMockMvc.perform(put("/api/directors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDirector)))
                .andExpect(status().isOk());

        // Validate the Director in the database
        List<Director> directors = directorRepository.findAll();
        assertThat(directors).hasSize(databaseSizeBeforeUpdate);
        Director testDirector = directors.get(directors.size() - 1);
        assertThat(testDirector.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDirector.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testDirector.getImdbId()).isEqualTo(UPDATED_IMDB_ID);
    }

    @Test
    @Transactional
    public void deleteDirector() throws Exception {
        // Initialize the database
        directorRepository.saveAndFlush(director);
        int databaseSizeBeforeDelete = directorRepository.findAll().size();

        // Get the director
        restDirectorMockMvc.perform(delete("/api/directors/{id}", director.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Director> directors = directorRepository.findAll();
        assertThat(directors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
