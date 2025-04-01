package com.arsansys.RemaPartners.controllers.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per a la creació d'usuaris.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {

    /**
     * Nom de l'usuari.
     */
    @NotBlank
    private String nom;

    /**
     * Cognoms de l'usuari.
     */
    @NotBlank
    private String cognoms;

    /**
     * Username.
     */
    @NotBlank
    private String username;

    /**
     * Contrasenya de l'usuari.
     */

    @NotBlank
    private String password;

    /**
     * DNI de l'usuari.
     */

    @NotBlank
    private String dni;

    /**
     * Conjunt de rols de l'usuari.
     */
    private Set<String> rols;

}
