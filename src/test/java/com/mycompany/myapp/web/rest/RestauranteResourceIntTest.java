package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RestauranteApp;

import com.mycompany.myapp.domain.Restaurante;
import com.mycompany.myapp.repository.RestauranteRepository;
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
 * Test class for the RestauranteResource REST controller.
 *
 * @see RestauranteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestauranteApp.class)
public class RestauranteResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TELEFONO = 1;
    private static final Integer UPDATED_TELEFONO = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRestauranteMockMvc;

    private Restaurante restaurante;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RestauranteResource restauranteResource = new RestauranteResource(restauranteRepository);
        this.restRestauranteMockMvc = MockMvcBuilders.standaloneSetup(restauranteResource)
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
    public static Restaurante createEntity(EntityManager em) {
        Restaurante restaurante = new Restaurante()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO)
            .status(DEFAULT_STATUS)
            .activo(DEFAULT_ACTIVO);
        return restaurante;
    }

    @Before
    public void initTest() {
        restaurante = createEntity(em);
    }

    @Test
    @Transactional
    public void createRestaurante() throws Exception {
        int databaseSizeBeforeCreate = restauranteRepository.findAll().size();

        // Create the Restaurante
        restRestauranteMockMvc.perform(post("/api/restaurantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurante)))
            .andExpect(status().isCreated());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurante testRestaurante = restauranteList.get(restauranteList.size() - 1);
        assertThat(testRestaurante.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testRestaurante.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testRestaurante.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testRestaurante.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRestaurante.isActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    @Transactional
    public void createRestauranteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = restauranteRepository.findAll().size();

        // Create the Restaurante with an existing ID
        restaurante.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestauranteMockMvc.perform(post("/api/restaurantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurante)))
            .andExpect(status().isBadRequest());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRestaurantes() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        // Get all the restauranteList
        restRestauranteMockMvc.perform(get("/api/restaurantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void getRestaurante() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        // Get the restaurante
        restRestauranteMockMvc.perform(get("/api/restaurantes/{id}", restaurante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(restaurante.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRestaurante() throws Exception {
        // Get the restaurante
        restRestauranteMockMvc.perform(get("/api/restaurantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestaurante() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);
        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();

        // Update the restaurante
        Restaurante updatedRestaurante = restauranteRepository.findOne(restaurante.getId());
        updatedRestaurante
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .status(UPDATED_STATUS)
            .activo(UPDATED_ACTIVO);

        restRestauranteMockMvc.perform(put("/api/restaurantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRestaurante)))
            .andExpect(status().isOk());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
        Restaurante testRestaurante = restauranteList.get(restauranteList.size() - 1);
        assertThat(testRestaurante.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testRestaurante.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testRestaurante.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testRestaurante.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRestaurante.isActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    public void updateNonExistingRestaurante() throws Exception {
        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();

        // Create the Restaurante

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRestauranteMockMvc.perform(put("/api/restaurantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurante)))
            .andExpect(status().isCreated());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRestaurante() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);
        int databaseSizeBeforeDelete = restauranteRepository.findAll().size();

        // Get the restaurante
        restRestauranteMockMvc.perform(delete("/api/restaurantes/{id}", restaurante.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restaurante.class);
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(restaurante1.getId());
        assertThat(restaurante1).isEqualTo(restaurante2);
        restaurante2.setId(2L);
        assertThat(restaurante1).isNotEqualTo(restaurante2);
        restaurante1.setId(null);
        assertThat(restaurante1).isNotEqualTo(restaurante2);
    }
}
