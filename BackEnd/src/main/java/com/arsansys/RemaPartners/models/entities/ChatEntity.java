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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chats")
public class ChatEntity {
    @Id
    private String id;

    @NotBlank
    private String idProducto;

    @NotBlank
    private String idComprador;

    @NotBlank
    private String idVendedor;

    private List<MensajeEntity> mensajes;

    @Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private LocalDateTime ultimaActualizacion;

    private Boolean activo;
}
