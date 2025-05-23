package com.arsansys.RemaPartners.controllers.dto;

import java.util.List;

import com.arsansys.RemaPartners.models.entities.SocialLinkEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de usuarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {

    /**
     * Nombre de usuario.
     */
    private String username;

    /**
     * Correo electrónico del usuario.
     */
    @NotBlank
    private String email;

    /**
     * Contraseña del usuario.
     */
    @NotBlank
    private String password;

    /**
     * Token de Google del usuario (opcional).
     */
    private String googleToken;

    /**
     * Descripción del usuario.
     */
    private String description;

    /**
     * URL de la imagen de perfil.
     */
    private String profileImage;

    /**
     * Lista de enlaces sociales del usuario.
     */
    private List<SocialLinkEntity> socialLinks;
}
