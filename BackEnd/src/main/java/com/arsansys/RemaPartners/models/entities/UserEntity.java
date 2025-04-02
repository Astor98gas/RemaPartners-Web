package com.arsansys.RemaPartners.models.entities;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un usuari.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class UserEntity {

    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    @Indexed(unique = true)
    private String username;

    @NotBlank
    @Indexed(unique = true)
    private String email;

    @NotBlank
    @Size(max = 250)
    private String password;

    @NotBlank
    @Builder.Default
    private Boolean active = true;

    @DBRef
    private Set<RolEntity> rols;
}
