package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Platillo;

import com.mycompany.myapp.repository.PlatilloRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Platillo.
 */
@RestController
@RequestMapping("/api")
public class PlatilloResource {

    private final Logger log = LoggerFactory.getLogger(PlatilloResource.class);

    private static final String ENTITY_NAME = "platillo";

    private final PlatilloRepository platilloRepository;

    public PlatilloResource(PlatilloRepository platilloRepository) {
        this.platilloRepository = platilloRepository;
    }

    /**
     * POST  /platillos : Create a new platillo.
     *
     * @param platillo the platillo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new platillo, or with status 400 (Bad Request) if the platillo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/platillos")
    @Timed
    public ResponseEntity<Platillo> createPlatillo(@RequestBody Platillo platillo) throws URISyntaxException {
        log.debug("REST request to save Platillo : {}", platillo);
        if (platillo.getId() != null) {
            throw new BadRequestAlertException("A new platillo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Platillo result = platilloRepository.save(platillo);
        return ResponseEntity.created(new URI("/api/platillos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /platillos : Updates an existing platillo.
     *
     * @param platillo the platillo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated platillo,
     * or with status 400 (Bad Request) if the platillo is not valid,
     * or with status 500 (Internal Server Error) if the platillo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/platillos")
    @Timed
    public ResponseEntity<Platillo> updatePlatillo(@RequestBody Platillo platillo) throws URISyntaxException {
        log.debug("REST request to update Platillo : {}", platillo);
        if (platillo.getId() == null) {
            return createPlatillo(platillo);
        }
        Platillo result = platilloRepository.save(platillo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, platillo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /platillos : get all the platillos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of platillos in body
     */
    @GetMapping("/platillos")
    @Timed
    public ResponseEntity<List<Platillo>> getAllPlatillos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Platillos");
        Page<Platillo> page = platilloRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/platillos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /platillos/:id : get the "id" platillo.
     *
     * @param id the id of the platillo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the platillo, or with status 404 (Not Found)
     */
    @GetMapping("/platillos/{id}")
    @Timed
    public ResponseEntity<Platillo> getPlatillo(@PathVariable Long id) {
        log.debug("REST request to get Platillo : {}", id);
        Platillo platillo = platilloRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(platillo));
    }

    /**
     * DELETE  /platillos/:id : delete the "id" platillo.
     *
     * @param id the id of the platillo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/platillos/{id}")
    @Timed
    public ResponseEntity<Void> deletePlatillo(@PathVariable Long id) {
        log.debug("REST request to delete Platillo : {}", id);
        platilloRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
