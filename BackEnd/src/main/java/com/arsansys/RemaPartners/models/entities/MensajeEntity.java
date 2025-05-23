package com.arsansys.RemaPartners.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 * Entidad que representa un mensaje enviado en un chat entre usuarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MensajeEntity {

    /**
     * Identificador único del mensaje.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * Identificador del usuario emisor del mensaje.
     */
    private String idEmisor;

    /**
     * Fecha y hora en que se envió el mensaje.
     */
    @Default
    private LocalDateTime fecha = LocalDateTime.now();

    /**
     * Contenido del mensaje.
     */
    private String mensaje;

    /**
     * Indica si el mensaje ha sido leído.
     */
    private Boolean leido;

}
