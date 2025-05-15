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

    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String rol;

    private Set<String> googleToken;

}
