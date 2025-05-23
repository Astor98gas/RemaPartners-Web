package com.arsansys.RemaPartners.services;

import com.arsansys.RemaPartners.models.entities.RatingEntity;

import java.util.List;

/**
 * Servicio para la gestión de valoraciones de vendedores.
 */
public interface RatingService {

    /**
     * Obtiene todas las valoraciones de un vendedor.
     * 
     * @param sellerId ID del vendedor.
     * @return Lista de valoraciones.
     */
    List<RatingEntity> getRatingsBySeller(String sellerId);

    /**
     * Obtiene una valoración específica por su ID.
     * 
     * @param ratingId ID de la valoración.
     * @return La valoración si existe.
     */
    RatingEntity getRatingById(String ratingId);

    /**
     * Obtiene la valoración que un usuario hizo a un vendedor específico.
     * 
     * @param userId   ID del usuario.
     * @param sellerId ID del vendedor.
     * @return La valoración si existe.
     */
    RatingEntity getUserRatingForSeller(String userId, String sellerId);

    /**
     * Crea una nueva valoración.
     * 
     * @param rating Entidad de valoración a crear.
     * @return La valoración creada.
     */
    RatingEntity createRating(RatingEntity rating);

    /**
     * Actualiza una valoración existente.
     * 
     * @param ratingId ID de la valoración a actualizar.
     * @param rating   Datos actualizados.
     * @return La valoración actualizada.
     */
    RatingEntity updateRating(String ratingId, RatingEntity rating);

    /**
     * Añade o actualiza una respuesta a una valoración.
     * 
     * @param ratingId ID de la valoración.
     * @param reply    Texto de la respuesta.
     * @return La valoración actualizada con la respuesta.
     */
    RatingEntity addReplyToRating(String ratingId, String reply);

    /**
     * Elimina una valoración.
     * 
     * @param ratingId ID de la valoración a eliminar.
     */
    void deleteRating(String ratingId);
}
