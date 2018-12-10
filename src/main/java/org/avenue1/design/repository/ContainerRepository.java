package org.avenue1.design.repository;

import org.avenue1.design.domain.Container;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Container entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContainerRepository extends MongoRepository<Container, String> {

}
