package org.avenue1.design.service;

import org.avenue1.design.domain.Design;
import org.avenue1.design.repository.DesignRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Design.
 */
@Service
public class DesignService {

    private final Logger log = LoggerFactory.getLogger(DesignService.class);

    private final DesignRepository designRepository;

    public DesignService(DesignRepository designRepository) {
        this.designRepository = designRepository;
    }

    /**
     * Save a design.
     *
     * @param design the entity to save
     * @return the persisted entity
     */
    public Design save(Design design) {
        log.debug("Request to save Design : {}", design);
        return designRepository.save(design);
    }

    /**
     * Get all the designs.
     *
     * @return the list of entities
     */
    public List<Design> findAll() {
        log.debug("Request to get all Designs");
        return designRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the Design with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Design> findAllWithEagerRelationships(Pageable pageable) {
        return designRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one design by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<Design> findOne(String id) {
        log.debug("Request to get Design : {}", id);
        return designRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the design by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Design : {}", id);
        designRepository.deleteById(id);
    }
}
