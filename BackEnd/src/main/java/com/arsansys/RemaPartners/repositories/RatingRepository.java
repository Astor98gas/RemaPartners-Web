package com.arsansys.RemaPartners.repositories;

import com.arsansys.RemaPartners.models.entities.RatingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends MongoRepository<RatingEntity, String> {

    /**
     * Busca todas las valoraciones de un vendedor específico
     * 
     * @param sellerId ID del vendedor
     * @return Lista de valoraciones
     */
    List<RatingEntity> findBySellerId(String sellerId);

    /**
     * Busca la valoración que un usuario hizo a un vendedor
     * 
     * @param userId   ID del usuario
     * @param sellerId ID del vendedor
     * @return Valoración si existe
     */
    Optional<RatingEntity> findByUserIdAndSellerId(String userId, String sellerId);
}
