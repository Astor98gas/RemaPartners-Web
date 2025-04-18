package com.arsansys.RemaPartners.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.JwtEntity;

@Repository
public interface JwtRepository extends MongoRepository<JwtEntity, String> {
    JwtEntity findByUsername(String username);

    JwtEntity findByToken(String token);
}
