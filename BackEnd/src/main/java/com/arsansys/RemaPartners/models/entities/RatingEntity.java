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

    @Id
    private String id;

    private String sellerId; // ID del vendedor valorado
    private String userId; // ID del usuario que deja la valoración
    private String username; // Nombre del usuario que deja la valoración
    private Integer rating; // Puntuación de 1 a 5 estrellas
    private String comment; // Comentario de la valoración
    private String reply; // Respuesta del vendedor a la valoración (opcional)

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
