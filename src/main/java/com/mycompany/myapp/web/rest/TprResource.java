package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Tpr;

import com.mycompany.myapp.repository.TprRepository;
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
 * REST controller for managing Tpr.
 */
@RestController
@RequestMapping("/api")
public class TprResource {

    private final Logger log = LoggerFactory.getLogger(TprResource.class);

    private static final String ENTITY_NAME = "tpr";

    private final TprRepository tprRepository;

    public TprResource(TprRepository tprRepository) {
        this.tprRepository = tprRepository;
    }

    /**
     * POST  /tprs : Create a new tpr.
     *
     * @param tpr the tpr to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tpr, or with status 400 (Bad Request) if the tpr has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tprs")
    @Timed
    public ResponseEntity<Tpr> createTpr(@RequestBody Tpr tpr) throws URISyntaxException {
        log.debug("REST request to save Tpr : {}", tpr);
        if (tpr.getId() != null) {
            throw new BadRequestAlertException("A new tpr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tpr result = tprRepository.save(tpr);
        return ResponseEntity.created(new URI("/api/tprs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tprs : Updates an existing tpr.
     *
     * @param tpr the tpr to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tpr,
     * or with status 400 (Bad Request) if the tpr is not valid,
     * or with status 500 (Internal Server Error) if the tpr couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tprs")
    @Timed
    public ResponseEntity<Tpr> updateTpr(@RequestBody Tpr tpr) throws URISyntaxException {
        log.debug("REST request to update Tpr : {}", tpr);
        if (tpr.getId() == null) {
            return createTpr(tpr);
        }
        Tpr result = tprRepository.save(tpr);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tpr.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tprs : get all the tprs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tprs in body
     */
    @GetMapping("/tprs")
    @Timed
    public ResponseEntity<List<Tpr>> getAllTprs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Tprs");
        Page<Tpr> page = tprRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tprs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tprs/:id : get the "id" tpr.
     *
     * @param id the id of the tpr to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tpr, or with status 404 (Not Found)
     */
    @GetMapping("/tprs/{id}")
    @Timed
    public ResponseEntity<Tpr> getTpr(@PathVariable Long id) {
        log.debug("REST request to get Tpr : {}", id);
        Tpr tpr = tprRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tpr));
    }

    /**
     * DELETE  /tprs/:id : delete the "id" tpr.
     *
     * @param id the id of the tpr to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tprs/{id}")
    @Timed
    public ResponseEntity<Void> deleteTpr(@PathVariable Long id) {
        log.debug("REST request to delete Tpr : {}", id);
        tprRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
