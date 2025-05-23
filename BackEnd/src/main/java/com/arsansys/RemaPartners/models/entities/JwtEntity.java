package com.arsansys.RemaPartners.models.entities;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entidad que representa un token JWT almacenado en la base de datos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Document(collection = "jwt")
public class JwtEntity {

    /**
     * Token JWT.
     */
    @Id
    private String token;

    /**
     * Nombre de usuario asociado al token.
     */
    @NonNull
    private String username;

    /**
     * Fecha de expiración del token.
     */
    @NonNull
    private Date expirationDate;

    /**
     * Indica si el token es válido.
     */
    @NonNull
    private Boolean isValid;

}
