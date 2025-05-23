package com.arsansys.RemaPartners.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.JwtEntity;

/**
 * Repositorio para la entidad JwtEntity.
 * Permite operaciones CRUD y consultas personalizadas sobre JWT en MongoDB.
 */
@Repository
public interface JwtRepository extends MongoRepository<JwtEntity, String> {
    /**
     * Busca un JWT por nombre de usuario.
     * 
     * @param username Nombre de usuario
     * @return JWT si existe
     */
    JwtEntity findByUsername(String username);

    /**
     * Busca un JWT por token.
     * 
     * @param token Token JWT
     * @return JWT si existe
     */
    JwtEntity findByToken(String token);
}
