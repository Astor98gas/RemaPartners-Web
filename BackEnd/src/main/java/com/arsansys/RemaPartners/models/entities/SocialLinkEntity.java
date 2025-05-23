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

    /**
     * Plataforma de la red social (Ej: "facebook", "twitter", etc.).
     */
    private String platform;

    /**
     * URL completa del perfil de la red social.
     */
    private String url;

    /**
     * Nombre del icono para usar en el frontend (opcional).
     */
    private String icon;
}
