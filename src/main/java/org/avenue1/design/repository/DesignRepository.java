package org.avenue1.design.repository;

import org.avenue1.design.domain.Design;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Design entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesignRepository extends MongoRepository<Design, String> {
    @Query("{}")
    Page<Design> findAllWithEagerRelationships(Pageable pageable);

    List<Design> findAllByInstrumentType(String instrumentType);

    @Query("{}")
    List<Design> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Design> findOneWithEagerRelationships(String id);

}
