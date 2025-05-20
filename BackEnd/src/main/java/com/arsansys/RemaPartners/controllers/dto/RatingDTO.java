package com.arsansys.RemaPartners.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear o actualizar una valoración
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDTO {
    private String sellerId;
    private Integer rating;
    private String comment;
}
