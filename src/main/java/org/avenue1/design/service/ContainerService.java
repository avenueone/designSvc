package org.avenue1.design.service;

import org.avenue1.design.domain.Container;
import org.avenue1.design.repository.ContainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Container.
 */
@Service
public class ContainerService {

    private final Logger log = LoggerFactory.getLogger(ContainerService.class);

    private final ContainerRepository containerRepository;

    public ContainerService(ContainerRepository containerRepository) {
        this.containerRepository = containerRepository;
    }

    /**
     * Save a container.
     *
     * @param container the entity to save
     * @return the persisted entity
     */
    public Container save(Container container) {
        log.debug("Request to save Container : {}", container);
        return containerRepository.save(container);
    }

    /**
     * Get all the containers.
     *
     * @return the list of entities
     */
    public List<Container> findAll() {
        log.debug("Request to get all Containers");
        return containerRepository.findAll();
    }


    /**
     * Get one container by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<Container> findOne(String id) {
        log.debug("Request to get Container : {}", id);
        return containerRepository.findById(id);
    }

    /**
     * Delete the container by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Container : {}", id);
        containerRepository.deleteById(id);
    }
}
