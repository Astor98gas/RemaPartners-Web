package com.arsansys.RemaPartners.models.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad que representa una valoración de un vendedor.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "ratings")
public class RatingEntity {

    /**
     * Identificador único de la valoración.
     */
    @Id
    private String id;

    /**
     * ID del vendedor valorado.
     */
    private String sellerId;

    /**
     * ID del usuario que deja la valoración.
     */
    private String userId;

    /**
     * Nombre del usuario que deja la valoración.
     */
    private String username;

    /**
     * Puntuación de 1 a 5 estrellas.
     */
    private Integer rating;

    /**
     * Comentario de la valoración.
     */
    private String comment;

    /**
     * Respuesta del vendedor a la valoración (opcional).
     */
    private String reply;

    /**
     * Fecha de creación de la valoración.
     */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Fecha de última actualización de la valoración.
     */
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
