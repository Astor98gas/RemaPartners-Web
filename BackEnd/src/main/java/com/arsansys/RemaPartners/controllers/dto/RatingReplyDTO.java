package com.arsansys.RemaPartners.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para añadir una respuesta a una valoración
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingReplyDTO {
    private String ratingId;
    private String reply;
}
