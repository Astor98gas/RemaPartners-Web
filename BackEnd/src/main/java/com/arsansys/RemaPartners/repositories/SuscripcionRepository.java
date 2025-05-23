package com.arsansys.RemaPartners.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.Suscripcion;

/**
 * Repositorio para la entidad Suscripcion.
 * Permite operaciones CRUD y consultas personalizadas sobre suscripciones en
 * MongoDB.
 */
@Repository
public interface SuscripcionRepository extends MongoRepository<Suscripcion, String> {

    /**
     * Busca las suscripciones de un usuario específico.
     * 
     * @param idUsuario ID del usuario
     * @return Lista de suscripciones
     */
    List<Suscripcion> findByIdUsuario(String idUsuario);

}
