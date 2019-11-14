package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AligncheckApp;
import com.mycompany.myapp.domain.Rightalign;
import com.mycompany.myapp.repository.RightalignRepository;
import com.mycompany.myapp.service.RightalignService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.RightalignCriteria;
import com.mycompany.myapp.service.RightalignQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RightalignResource} REST controller.
 */
@SpringBootTest(classes = AligncheckApp.class)
public class RightalignResourceIT {

    private static final String DEFAULT_SDF = "AAAAAAAAAA";
    private static final String UPDATED_SDF = "BBBBBBBBBB";

    @Autowired
    private RightalignRepository rightalignRepository;

    @Autowired
    private RightalignService rightalignService;

    @Autowired
    private RightalignQueryService rightalignQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRightalignMockMvc;

    private Rightalign rightalign;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RightalignResource rightalignResource = new RightalignResource(rightalignService, rightalignQueryService,null);
        this.restRightalignMockMvc = MockMvcBuilders.standaloneSetup(rightalignResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rightalign createEntity(EntityManager em) {
        Rightalign rightalign = new Rightalign()
            .sdf(DEFAULT_SDF);
        return rightalign;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rightalign createUpdatedEntity(EntityManager em) {
        Rightalign rightalign = new Rightalign()
            .sdf(UPDATED_SDF);
        return rightalign;
    }

    @BeforeEach
    public void initTest() {
        rightalign = createEntity(em);
    }

    @Test
    @Transactional
    public void createRightalign() throws Exception {
        int databaseSizeBeforeCreate = rightalignRepository.findAll().size();

        // Create the Rightalign
        restRightalignMockMvc.perform(post("/api/rightaligns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rightalign)))
            .andExpect(status().isCreated());

        // Validate the Rightalign in the database
        List<Rightalign> rightalignList = rightalignRepository.findAll();
        assertThat(rightalignList).hasSize(databaseSizeBeforeCreate + 1);
        Rightalign testRightalign = rightalignList.get(rightalignList.size() - 1);
        assertThat(testRightalign.getSdf()).isEqualTo(DEFAULT_SDF);
    }

    @Test
    @Transactional
    public void createRightalignWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rightalignRepository.findAll().size();

        // Create the Rightalign with an existing ID
        rightalign.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRightalignMockMvc.perform(post("/api/rightaligns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rightalign)))
            .andExpect(status().isBadRequest());

        // Validate the Rightalign in the database
        List<Rightalign> rightalignList = rightalignRepository.findAll();
        assertThat(rightalignList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRightaligns() throws Exception {
        // Initialize the database
        rightalignRepository.saveAndFlush(rightalign);

        // Get all the rightalignList
        restRightalignMockMvc.perform(get("/api/rightaligns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rightalign.getId().intValue())))
            .andExpect(jsonPath("$.[*].sdf").value(hasItem(DEFAULT_SDF.toString())));
    }
    
    @Test
    @Transactional
    public void getRightalign() throws Exception {
        // Initialize the database
        rightalignRepository.saveAndFlush(rightalign);

        // Get the rightalign
        restRightalignMockMvc.perform(get("/api/rightaligns/{id}", rightalign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rightalign.getId().intValue()))
            .andExpect(jsonPath("$.sdf").value(DEFAULT_SDF.toString()));
    }

    @Test
    @Transactional
    public void getAllRightalignsBySdfIsEqualToSomething() throws Exception {
        // Initialize the database
        rightalignRepository.saveAndFlush(rightalign);

        // Get all the rightalignList where sdf equals to DEFAULT_SDF
        defaultRightalignShouldBeFound("sdf.equals=" + DEFAULT_SDF);

        // Get all the rightalignList where sdf equals to UPDATED_SDF
        defaultRightalignShouldNotBeFound("sdf.equals=" + UPDATED_SDF);
    }

    @Test
    @Transactional
    public void getAllRightalignsBySdfIsInShouldWork() throws Exception {
        // Initialize the database
        rightalignRepository.saveAndFlush(rightalign);

        // Get all the rightalignList where sdf in DEFAULT_SDF or UPDATED_SDF
        defaultRightalignShouldBeFound("sdf.in=" + DEFAULT_SDF + "," + UPDATED_SDF);

        // Get all the rightalignList where sdf equals to UPDATED_SDF
        defaultRightalignShouldNotBeFound("sdf.in=" + UPDATED_SDF);
    }

    @Test
    @Transactional
    public void getAllRightalignsBySdfIsNullOrNotNull() throws Exception {
        // Initialize the database
        rightalignRepository.saveAndFlush(rightalign);

        // Get all the rightalignList where sdf is not null
        defaultRightalignShouldBeFound("sdf.specified=true");

        // Get all the rightalignList where sdf is null
        defaultRightalignShouldNotBeFound("sdf.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRightalignShouldBeFound(String filter) throws Exception {
        restRightalignMockMvc.perform(get("/api/rightaligns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rightalign.getId().intValue())))
            .andExpect(jsonPath("$.[*].sdf").value(hasItem(DEFAULT_SDF)));

        // Check, that the count call also returns 1
        restRightalignMockMvc.perform(get("/api/rightaligns/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRightalignShouldNotBeFound(String filter) throws Exception {
        restRightalignMockMvc.perform(get("/api/rightaligns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRightalignMockMvc.perform(get("/api/rightaligns/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRightalign() throws Exception {
        // Get the rightalign
        restRightalignMockMvc.perform(get("/api/rightaligns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRightalign() throws Exception {
        // Initialize the database
        rightalignService.save(rightalign);

        int databaseSizeBeforeUpdate = rightalignRepository.findAll().size();

        // Update the rightalign
        Rightalign updatedRightalign = rightalignRepository.findById(rightalign.getId()).get();
        // Disconnect from session so that the updates on updatedRightalign are not directly saved in db
        em.detach(updatedRightalign);
        updatedRightalign
            .sdf(UPDATED_SDF);

        restRightalignMockMvc.perform(put("/api/rightaligns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRightalign)))
            .andExpect(status().isOk());

        // Validate the Rightalign in the database
        List<Rightalign> rightalignList = rightalignRepository.findAll();
        assertThat(rightalignList).hasSize(databaseSizeBeforeUpdate);
        Rightalign testRightalign = rightalignList.get(rightalignList.size() - 1);
        assertThat(testRightalign.getSdf()).isEqualTo(UPDATED_SDF);
    }

    @Test
    @Transactional
    public void updateNonExistingRightalign() throws Exception {
        int databaseSizeBeforeUpdate = rightalignRepository.findAll().size();

        // Create the Rightalign

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRightalignMockMvc.perform(put("/api/rightaligns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rightalign)))
            .andExpect(status().isBadRequest());

        // Validate the Rightalign in the database
        List<Rightalign> rightalignList = rightalignRepository.findAll();
        assertThat(rightalignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRightalign() throws Exception {
        // Initialize the database
        rightalignService.save(rightalign);

        int databaseSizeBeforeDelete = rightalignRepository.findAll().size();

        // Delete the rightalign
        restRightalignMockMvc.perform(delete("/api/rightaligns/{id}", rightalign.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rightalign> rightalignList = rightalignRepository.findAll();
        assertThat(rightalignList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rightalign.class);
        Rightalign rightalign1 = new Rightalign();
        rightalign1.setId(1L);
        Rightalign rightalign2 = new Rightalign();
        rightalign2.setId(rightalign1.getId());
        assertThat(rightalign1).isEqualTo(rightalign2);
        rightalign2.setId(2L);
        assertThat(rightalign1).isNotEqualTo(rightalign2);
        rightalign1.setId(null);
        assertThat(rightalign1).isNotEqualTo(rightalign2);
    }
}
