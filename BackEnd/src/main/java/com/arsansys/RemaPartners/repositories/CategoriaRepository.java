package com.arsansys.RemaPartners.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.CategoriaEntity;

/**
 * Repositorio para la entidad CategoriaEntity.
 * Proporciona operaciones CRUD sobre la colección de categorías en MongoDB.
 */
@Repository
public interface CategoriaRepository extends MongoRepository<CategoriaEntity, String> {

}
