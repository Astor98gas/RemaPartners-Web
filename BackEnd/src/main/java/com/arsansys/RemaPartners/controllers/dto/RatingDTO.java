package com.arsansys.RemaPartners.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear o actualizar una valoración.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDTO {
    /**
     * ID del vendedor valorado.
     */
    private String sellerId;

    /**
     * Puntuación otorgada.
     */
    private Integer rating;

    /**
     * Comentario de la valoración.
     */
    private String comment;
}
