package com.arsansys.RemaPartners.models.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 * Entidad que representa un chat entre un comprador y un vendedor sobre un
 * producto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chats")
public class ChatEntity {
    /**
     * Identificador único del chat.
     */
    @Id
    private String id;

    /**
     * Identificador del producto sobre el que se conversa.
     */
    @NotBlank
    private String idProducto;

    /**
     * Identificador del comprador.
     */
    @NotBlank
    private String idComprador;

    /**
     * Identificador del vendedor.
     */
    @NotBlank
    private String idVendedor;

    /**
     * Lista de mensajes intercambiados en el chat.
     */
    private List<MensajeEntity> mensajes;

    /**
     * Fecha de creación del chat.
     */
    @Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    /**
     * Fecha de la última actualización del chat.
     */
    private LocalDateTime ultimaActualizacion;

    /**
     * Indica si el chat está activo.
     */
    private Boolean activo;
}
