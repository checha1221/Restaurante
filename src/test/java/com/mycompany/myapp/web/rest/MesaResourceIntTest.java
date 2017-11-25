package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RestauranteApp;

import com.mycompany.myapp.domain.Mesa;
import com.mycompany.myapp.repository.MesaRepository;
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
 * Test class for the MesaResource REST controller.
 *
 * @see MesaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestauranteApp.class)
public class MesaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String DEFAULT_AREA = "AAAAAAAAAA";
    private static final String UPDATED_AREA = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACIDAD = 1;
    private static final Integer UPDATED_CAPACIDAD = 2;

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMesaMockMvc;

    private Mesa mesa;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MesaResource mesaResource = new MesaResource(mesaRepository);
        this.restMesaMockMvc = MockMvcBuilders.standaloneSetup(mesaResource)
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
    public static Mesa createEntity(EntityManager em) {
        Mesa mesa = new Mesa()
            .nombre(DEFAULT_NOMBRE)
            .ubicacion(DEFAULT_UBICACION)
            .area(DEFAULT_AREA)
            .capacidad(DEFAULT_CAPACIDAD)
            .estado(DEFAULT_ESTADO)
            .activo(DEFAULT_ACTIVO);
        return mesa;
    }

    @Before
    public void initTest() {
        mesa = createEntity(em);
    }

    @Test
    @Transactional
    public void createMesa() throws Exception {
        int databaseSizeBeforeCreate = mesaRepository.findAll().size();

        // Create the Mesa
        restMesaMockMvc.perform(post("/api/mesas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mesa)))
            .andExpect(status().isCreated());

        // Validate the Mesa in the database
        List<Mesa> mesaList = mesaRepository.findAll();
        assertThat(mesaList).hasSize(databaseSizeBeforeCreate + 1);
        Mesa testMesa = mesaList.get(mesaList.size() - 1);
        assertThat(testMesa.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMesa.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
        assertThat(testMesa.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testMesa.getCapacidad()).isEqualTo(DEFAULT_CAPACIDAD);
        assertThat(testMesa.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testMesa.isActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    @Transactional
    public void createMesaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mesaRepository.findAll().size();

        // Create the Mesa with an existing ID
        mesa.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMesaMockMvc.perform(post("/api/mesas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mesa)))
            .andExpect(status().isBadRequest());

        // Validate the Mesa in the database
        List<Mesa> mesaList = mesaRepository.findAll();
        assertThat(mesaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMesas() throws Exception {
        // Initialize the database
        mesaRepository.saveAndFlush(mesa);

        // Get all the mesaList
        restMesaMockMvc.perform(get("/api/mesas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mesa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION.toString())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
            .andExpect(jsonPath("$.[*].capacidad").value(hasItem(DEFAULT_CAPACIDAD)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void getMesa() throws Exception {
        // Initialize the database
        mesaRepository.saveAndFlush(mesa);

        // Get the mesa
        restMesaMockMvc.perform(get("/api/mesas/{id}", mesa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mesa.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION.toString()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.toString()))
            .andExpect(jsonPath("$.capacidad").value(DEFAULT_CAPACIDAD))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMesa() throws Exception {
        // Get the mesa
        restMesaMockMvc.perform(get("/api/mesas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMesa() throws Exception {
        // Initialize the database
        mesaRepository.saveAndFlush(mesa);
        int databaseSizeBeforeUpdate = mesaRepository.findAll().size();

        // Update the mesa
        Mesa updatedMesa = mesaRepository.findOne(mesa.getId());
        updatedMesa
            .nombre(UPDATED_NOMBRE)
            .ubicacion(UPDATED_UBICACION)
            .area(UPDATED_AREA)
            .capacidad(UPDATED_CAPACIDAD)
            .estado(UPDATED_ESTADO)
            .activo(UPDATED_ACTIVO);

        restMesaMockMvc.perform(put("/api/mesas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMesa)))
            .andExpect(status().isOk());

        // Validate the Mesa in the database
        List<Mesa> mesaList = mesaRepository.findAll();
        assertThat(mesaList).hasSize(databaseSizeBeforeUpdate);
        Mesa testMesa = mesaList.get(mesaList.size() - 1);
        assertThat(testMesa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMesa.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testMesa.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testMesa.getCapacidad()).isEqualTo(UPDATED_CAPACIDAD);
        assertThat(testMesa.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testMesa.isActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    public void updateNonExistingMesa() throws Exception {
        int databaseSizeBeforeUpdate = mesaRepository.findAll().size();

        // Create the Mesa

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMesaMockMvc.perform(put("/api/mesas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mesa)))
            .andExpect(status().isCreated());

        // Validate the Mesa in the database
        List<Mesa> mesaList = mesaRepository.findAll();
        assertThat(mesaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMesa() throws Exception {
        // Initialize the database
        mesaRepository.saveAndFlush(mesa);
        int databaseSizeBeforeDelete = mesaRepository.findAll().size();

        // Get the mesa
        restMesaMockMvc.perform(delete("/api/mesas/{id}", mesa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Mesa> mesaList = mesaRepository.findAll();
        assertThat(mesaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mesa.class);
        Mesa mesa1 = new Mesa();
        mesa1.setId(1L);
        Mesa mesa2 = new Mesa();
        mesa2.setId(mesa1.getId());
        assertThat(mesa1).isEqualTo(mesa2);
        mesa2.setId(2L);
        assertThat(mesa1).isNotEqualTo(mesa2);
        mesa1.setId(null);
        assertThat(mesa1).isNotEqualTo(mesa2);
    }
}
