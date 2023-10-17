package com.bancogalicia.templatemicroservicegenerator.repository;

import com.bancogalicia.templatemicroservicegenerator.models.TemplateMicroservice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateMicroservicesRespository extends MongoRepository<TemplateMicroservice, String> {
    @Query("{name: ?0}")
    Optional<TemplateMicroservice> findByName(String name);
}
