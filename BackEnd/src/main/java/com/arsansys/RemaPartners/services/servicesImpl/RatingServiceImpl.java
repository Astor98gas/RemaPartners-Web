package com.arsansys.RemaPartners.services.servicesImpl;

import com.arsansys.RemaPartners.models.entities.RatingEntity;
import com.arsansys.RemaPartners.repositories.RatingRepository;
import com.arsansys.RemaPartners.services.RatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio para la gestión de valoraciones (ratings).
 */
@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    /**
     * Obtiene todas las valoraciones de un vendedor.
     * 
     * @param sellerId Identificador del vendedor.
     * @return Lista de valoraciones del vendedor.
     */
    @Override
    public List<RatingEntity> getRatingsBySeller(String sellerId) {
        return ratingRepository.findBySellerId(sellerId);
    }

    /**
     * Obtiene una valoración por su identificador.
     * 
     * @param ratingId Identificador de la valoración.
     * @return Entidad de valoración encontrada.
     */
    @Override
    public RatingEntity getRatingById(String ratingId) {
        return ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + ratingId));
    }

    /**
     * Obtiene la valoración de un usuario para un vendedor específico.
     * 
     * @param userId   Identificador del usuario.
     * @param sellerId Identificador del vendedor.
     * @return Entidad de valoración encontrada.
     */
    @Override
    public RatingEntity getUserRatingForSeller(String userId, String sellerId) {
        return ratingRepository.findByUserIdAndSellerId(userId, sellerId)
                .orElseThrow(() -> new RuntimeException("No rating found for this user and seller"));
    }

    /**
     * Crea una nueva valoración.
     * 
     * @param rating Entidad de valoración a crear.
     * @return Entidad de valoración creada.
     */
    @Override
    public RatingEntity createRating(RatingEntity rating) {
        // Verificar si el usuario ya ha valorado a este vendedor
        boolean exists = ratingRepository.findByUserIdAndSellerId(rating.getUserId(), rating.getSellerId()).isPresent();

        if (exists) {
            throw new RuntimeException("User has already rated this seller");
        }

        // Establecer fechas de creación y actualización
        LocalDateTime now = LocalDateTime.now();
        rating.setCreatedAt(now);
        rating.setUpdatedAt(now);

        return ratingRepository.save(rating);
    }

    /**
     * Actualiza una valoración existente.
     * 
     * @param ratingId      Identificador de la valoración.
     * @param updatedRating Entidad de valoración con los datos actualizados.
     * @return Entidad de valoración actualizada.
     */
    @Override
    public RatingEntity updateRating(String ratingId, RatingEntity updatedRating) {
        RatingEntity existingRating = getRatingById(ratingId);

        // Actualizar solo los campos editables por el usuario
        existingRating.setRating(updatedRating.getRating());
        existingRating.setComment(updatedRating.getComment());
        existingRating.setUpdatedAt(LocalDateTime.now());

        return ratingRepository.save(existingRating);
    }

    /**
     * Añade una respuesta a una valoración.
     * 
     * @param ratingId Identificador de la valoración.
     * @param reply    Respuesta a añadir.
     * @return Entidad de valoración actualizada.
     */
    @Override
    public RatingEntity addReplyToRating(String ratingId, String reply) {
        RatingEntity rating = getRatingById(ratingId);

        rating.setReply(reply);
        rating.setUpdatedAt(LocalDateTime.now());

        return ratingRepository.save(rating);
    }

    /**
     * Elimina una valoración por su identificador.
     * 
     * @param ratingId Identificador de la valoración a eliminar.
     */
    @Override
    public void deleteRating(String ratingId) {
        RatingEntity rating = getRatingById(ratingId);
        ratingRepository.delete(rating);
    }
}
