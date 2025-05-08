package com.arsansys.RemaPartners.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.Suscripcion;

@Repository
public interface SuscripcionRepository extends MongoRepository<Suscripcion, String> {

}
