package com.arsansys.RemaPartners.controllers.dto;

import java.util.List;

import com.arsansys.RemaPartners.models.entities.SocialLinkEntity;

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

    private String googleToken;

    private String description;

    private String profileImage;

    private List<SocialLinkEntity> socialLinks;
}
