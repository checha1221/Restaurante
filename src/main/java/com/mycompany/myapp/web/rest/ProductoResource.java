package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Producto;

import com.mycompany.myapp.repository.ProductoRepository;
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
 * REST controller for managing Producto.
 */
@RestController
@RequestMapping("/api")
public class ProductoResource {

    private final Logger log = LoggerFactory.getLogger(ProductoResource.class);

    private static final String ENTITY_NAME = "producto";

    private final ProductoRepository productoRepository;

    public ProductoResource(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * POST  /productos : Create a new producto.
     *
     * @param producto the producto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new producto, or with status 400 (Bad Request) if the producto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/productos")
    @Timed
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) throws URISyntaxException {
        log.debug("REST request to save Producto : {}", producto);
        if (producto.getId() != null) {
            throw new BadRequestAlertException("A new producto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Producto result = productoRepository.save(producto);
        return ResponseEntity.created(new URI("/api/productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /productos : Updates an existing producto.
     *
     * @param producto the producto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated producto,
     * or with status 400 (Bad Request) if the producto is not valid,
     * or with status 500 (Internal Server Error) if the producto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/productos")
    @Timed
    public ResponseEntity<Producto> updateProducto(@RequestBody Producto producto) throws URISyntaxException {
        log.debug("REST request to update Producto : {}", producto);
        if (producto.getId() == null) {
            return createProducto(producto);
        }
        Producto result = productoRepository.save(producto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, producto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /productos : get all the productos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of productos in body
     */
    @GetMapping("/productos")
    @Timed
    public ResponseEntity<List<Producto>> getAllProductos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Productos");
        Page<Producto> page = productoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/productos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /productos/:id : get the "id" producto.
     *
     * @param id the id of the producto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the producto, or with status 404 (Not Found)
     */
    @GetMapping("/productos/{id}")
    @Timed
    public ResponseEntity<Producto> getProducto(@PathVariable Long id) {
        log.debug("REST request to get Producto : {}", id);
        Producto producto = productoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(producto));
    }

    /**
     * DELETE  /productos/:id : delete the "id" producto.
     *
     * @param id the id of the producto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/productos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        log.debug("REST request to delete Producto : {}", id);
        productoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
