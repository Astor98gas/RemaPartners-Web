package com.arsansys.RemaPartners.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un enlace a redes sociales de un usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialLinkEntity {

    private String platform; // Ej: "facebook", "twitter", "instagram", "linkedin", etc.
    private String url; // URL completa del perfil
    private String icon; // Opcional: nombre del icono para usar en el frontend
}
