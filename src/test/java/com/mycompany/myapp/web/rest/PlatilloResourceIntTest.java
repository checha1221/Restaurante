package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RestauranteApp;

import com.mycompany.myapp.domain.Platillo;
import com.mycompany.myapp.repository.PlatilloRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlatilloResource REST controller.
 *
 * @see PlatilloResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestauranteApp.class)
public class PlatilloResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_VENCIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VENCIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    @Autowired
    private PlatilloRepository platilloRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPlatilloMockMvc;

    private Platillo platillo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlatilloResource platilloResource = new PlatilloResource(platilloRepository);
        this.restPlatilloMockMvc = MockMvcBuilders.standaloneSetup(platilloResource)
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
    public static Platillo createEntity(EntityManager em) {
        Platillo platillo = new Platillo()
            .nombre(DEFAULT_NOMBRE)
            .detalle(DEFAULT_DETALLE)
            .vencimiento(DEFAULT_VENCIMIENTO)
            .activo(DEFAULT_ACTIVO);
        return platillo;
    }

    @Before
    public void initTest() {
        platillo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlatillo() throws Exception {
        int databaseSizeBeforeCreate = platilloRepository.findAll().size();

        // Create the Platillo
        restPlatilloMockMvc.perform(post("/api/platillos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(platillo)))
            .andExpect(status().isCreated());

        // Validate the Platillo in the database
        List<Platillo> platilloList = platilloRepository.findAll();
        assertThat(platilloList).hasSize(databaseSizeBeforeCreate + 1);
        Platillo testPlatillo = platilloList.get(platilloList.size() - 1);
        assertThat(testPlatillo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPlatillo.getDetalle()).isEqualTo(DEFAULT_DETALLE);
        assertThat(testPlatillo.getVencimiento()).isEqualTo(DEFAULT_VENCIMIENTO);
        assertThat(testPlatillo.isActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    @Transactional
    public void createPlatilloWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = platilloRepository.findAll().size();

        // Create the Platillo with an existing ID
        platillo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlatilloMockMvc.perform(post("/api/platillos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(platillo)))
            .andExpect(status().isBadRequest());

        // Validate the Platillo in the database
        List<Platillo> platilloList = platilloRepository.findAll();
        assertThat(platilloList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPlatillos() throws Exception {
        // Initialize the database
        platilloRepository.saveAndFlush(platillo);

        // Get all the platilloList
        restPlatilloMockMvc.perform(get("/api/platillos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(platillo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())))
            .andExpect(jsonPath("$.[*].vencimiento").value(hasItem(DEFAULT_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void getPlatillo() throws Exception {
        // Initialize the database
        platilloRepository.saveAndFlush(platillo);

        // Get the platillo
        restPlatilloMockMvc.perform(get("/api/platillos/{id}", platillo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(platillo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE.toString()))
            .andExpect(jsonPath("$.vencimiento").value(DEFAULT_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlatillo() throws Exception {
        // Get the platillo
        restPlatilloMockMvc.perform(get("/api/platillos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlatillo() throws Exception {
        // Initialize the database
        platilloRepository.saveAndFlush(platillo);
        int databaseSizeBeforeUpdate = platilloRepository.findAll().size();

        // Update the platillo
        Platillo updatedPlatillo = platilloRepository.findOne(platillo.getId());
        updatedPlatillo
            .nombre(UPDATED_NOMBRE)
            .detalle(UPDATED_DETALLE)
            .vencimiento(UPDATED_VENCIMIENTO)
            .activo(UPDATED_ACTIVO);

        restPlatilloMockMvc.perform(put("/api/platillos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlatillo)))
            .andExpect(status().isOk());

        // Validate the Platillo in the database
        List<Platillo> platilloList = platilloRepository.findAll();
        assertThat(platilloList).hasSize(databaseSizeBeforeUpdate);
        Platillo testPlatillo = platilloList.get(platilloList.size() - 1);
        assertThat(testPlatillo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPlatillo.getDetalle()).isEqualTo(UPDATED_DETALLE);
        assertThat(testPlatillo.getVencimiento()).isEqualTo(UPDATED_VENCIMIENTO);
        assertThat(testPlatillo.isActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    public void updateNonExistingPlatillo() throws Exception {
        int databaseSizeBeforeUpdate = platilloRepository.findAll().size();

        // Create the Platillo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPlatilloMockMvc.perform(put("/api/platillos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(platillo)))
            .andExpect(status().isCreated());

        // Validate the Platillo in the database
        List<Platillo> platilloList = platilloRepository.findAll();
        assertThat(platilloList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePlatillo() throws Exception {
        // Initialize the database
        platilloRepository.saveAndFlush(platillo);
        int databaseSizeBeforeDelete = platilloRepository.findAll().size();

        // Get the platillo
        restPlatilloMockMvc.perform(delete("/api/platillos/{id}", platillo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Platillo> platilloList = platilloRepository.findAll();
        assertThat(platilloList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Platillo.class);
        Platillo platillo1 = new Platillo();
        platillo1.setId(1L);
        Platillo platillo2 = new Platillo();
        platillo2.setId(platillo1.getId());
        assertThat(platillo1).isEqualTo(platillo2);
        platillo2.setId(2L);
        assertThat(platillo1).isNotEqualTo(platillo2);
        platillo1.setId(null);
        assertThat(platillo1).isNotEqualTo(platillo2);
    }
}
