package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RestauranteApp;

import com.mycompany.myapp.domain.Tpr;
import com.mycompany.myapp.repository.TprRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TprResource REST controller.
 *
 * @see TprResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestauranteApp.class)
public class TprResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    @Autowired
    private TprRepository tprRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTprMockMvc;

    private Tpr tpr;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TprResource tprResource = new TprResource(tprRepository);
        this.restTprMockMvc = MockMvcBuilders.standaloneSetup(tprResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tpr createEntity(EntityManager em) {
        Tpr tpr = new Tpr()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .status(DEFAULT_STATUS)
            .activo(DEFAULT_ACTIVO);
        return tpr;
    }

    @Before
    public void initTest() {
        tpr = createEntity(em);
    }

    @Test
    @Transactional
    public void createTpr() throws Exception {
        int databaseSizeBeforeCreate = tprRepository.findAll().size();

        // Create the Tpr
        restTprMockMvc.perform(post("/api/tprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tpr)))
            .andExpect(status().isCreated());

        // Validate the Tpr in the database
        List<Tpr> tprList = tprRepository.findAll();
        assertThat(tprList).hasSize(databaseSizeBeforeCreate + 1);
        Tpr testTpr = tprList.get(tprList.size() - 1);
        assertThat(testTpr.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTpr.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testTpr.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTpr.isActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    @Transactional
    public void createTprWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tprRepository.findAll().size();

        // Create the Tpr with an existing ID
        tpr.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTprMockMvc.perform(post("/api/tprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tpr)))
            .andExpect(status().isBadRequest());

        // Validate the Tpr in the database
        List<Tpr> tprList = tprRepository.findAll();
        assertThat(tprList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTprs() throws Exception {
        // Initialize the database
        tprRepository.saveAndFlush(tpr);

        // Get all the tprList
        restTprMockMvc.perform(get("/api/tprs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tpr.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void getTpr() throws Exception {
        // Initialize the database
        tprRepository.saveAndFlush(tpr);

        // Get the tpr
        restTprMockMvc.perform(get("/api/tprs/{id}", tpr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tpr.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTpr() throws Exception {
        // Get the tpr
        restTprMockMvc.perform(get("/api/tprs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTpr() throws Exception {
        // Initialize the database
        tprRepository.saveAndFlush(tpr);
        int databaseSizeBeforeUpdate = tprRepository.findAll().size();

        // Update the tpr
        Tpr updatedTpr = tprRepository.findOne(tpr.getId());
        updatedTpr
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .status(UPDATED_STATUS)
            .activo(UPDATED_ACTIVO);

        restTprMockMvc.perform(put("/api/tprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTpr)))
            .andExpect(status().isOk());

        // Validate the Tpr in the database
        List<Tpr> tprList = tprRepository.findAll();
        assertThat(tprList).hasSize(databaseSizeBeforeUpdate);
        Tpr testTpr = tprList.get(tprList.size() - 1);
        assertThat(testTpr.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTpr.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testTpr.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTpr.isActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    public void updateNonExistingTpr() throws Exception {
        int databaseSizeBeforeUpdate = tprRepository.findAll().size();

        // Create the Tpr

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTprMockMvc.perform(put("/api/tprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tpr)))
            .andExpect(status().isCreated());

        // Validate the Tpr in the database
        List<Tpr> tprList = tprRepository.findAll();
        assertThat(tprList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTpr() throws Exception {
        // Initialize the database
        tprRepository.saveAndFlush(tpr);
        int databaseSizeBeforeDelete = tprRepository.findAll().size();

        // Get the tpr
        restTprMockMvc.perform(delete("/api/tprs/{id}", tpr.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tpr> tprList = tprRepository.findAll();
        assertThat(tprList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tpr.class);
        Tpr tpr1 = new Tpr();
        tpr1.setId(1L);
        Tpr tpr2 = new Tpr();
        tpr2.setId(tpr1.getId());
        assertThat(tpr1).isEqualTo(tpr2);
        tpr2.setId(2L);
        assertThat(tpr1).isNotEqualTo(tpr2);
        tpr1.setId(null);
        assertThat(tpr1).isNotEqualTo(tpr2);
    }
}
