package com.arsansys.RemaPartners.models.entities;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import com.google.firebase.database.annotations.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 * Entidad que representa una suscripción de usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Document(collection = "suscripciones")
public class Suscripcion {

    /**
     * Identificador único de la suscripción.
     */
    @Id
    private String id;

    /**
     * Identificador del usuario suscrito.
     */
    @NotNull
    private String idUsuario;

    /**
     * Fecha de compra de la suscripción.
     */
    @NotNull
    @Default
    private LocalDate fechaCompra = LocalDate.now();

    /**
     * Fecha de vencimiento de la suscripción.
     */
    @NotNull
    @Default
    private LocalDate fechaVencimiento = LocalDate.now().plusDays(30);
}
