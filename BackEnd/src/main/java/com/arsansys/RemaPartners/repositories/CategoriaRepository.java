package com.arsansys.RemaPartners.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.arsansys.RemaPartners.models.entities.CategoriaEntity;

public interface CategoriaRepository extends MongoRepository<CategoriaEntity, String> {

}
