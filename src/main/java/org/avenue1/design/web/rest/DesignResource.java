package org.avenue1.design.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.avenue1.design.domain.Design;
import org.avenue1.design.service.DesignService;
import org.avenue1.design.web.rest.errors.BadRequestAlertException;
import org.avenue1.design.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Design.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class DesignResource {

    private final Logger log = LoggerFactory.getLogger(DesignResource.class);

    private static final String ENTITY_NAME = "designSvcDesign";

    private final DesignService designService;

    public DesignResource(DesignService designService) {
        this.designService = designService;
    }

    /**
     * POST  /designs : Create a new design.
     *
     * @param design the design to create
     * @return the ResponseEntity with status 201 (Created) and with body the new design, or with status 400 (Bad Request) if the design has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/designs")
    @Timed
    public ResponseEntity<Design> createDesign(@Valid @RequestBody Design design) throws URISyntaxException {
        log.debug("REST request to save Design : {}", design);
        if (design.getId() != null) {
            throw new BadRequestAlertException("A new design cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Design result = designService.save(design);
        return ResponseEntity.created(new URI("/api/designs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /designs : Updates an existing design.
     *
     * @param design the design to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated design,
     * or with status 400 (Bad Request) if the design is not valid,
     * or with status 500 (Internal Server Error) if the design couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/designs")
    @Timed
    public ResponseEntity<Design> updateDesign(@Valid @RequestBody Design design) throws URISyntaxException {
        log.debug("REST request to update Design : {}", design);
        if (design.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Design result = designService.save(design);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, design.getId().toString()))
            .body(result);
    }

    /**
     * GET  /designs : get all the designs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of designs in body
     */
    @GetMapping("/designs")
    @Timed
    public List<Design> getAllDesigns(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Designs");
        return designService.findAll();
    }

    /**
     * GET  /designs/:id : get the "id" design.
     *
     * @param id the id of the design to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the design, or with status 404 (Not Found)
     */
    @GetMapping("/designs/{id}")
    @Timed
    public ResponseEntity<Design> getDesign(@PathVariable String id) {
        log.debug("REST request to get Design : {}", id);
        Optional<Design> design = designService.findOne(id);
        return ResponseUtil.wrapOrNotFound(design);
    }

    /**
     * DELETE  /designs/:id : delete the "id" design.
     *
     * @param id the id of the design to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/designs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDesign(@PathVariable String id) {
        log.debug("REST request to delete Design : {}", id);
        designService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
