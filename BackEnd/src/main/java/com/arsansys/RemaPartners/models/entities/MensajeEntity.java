package com.arsansys.RemaPartners.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MensajeEntity {

    private String idEmisor;

    @Default
    private LocalDateTime fecha = LocalDateTime.now();

    private String mensaje;

    private Boolean leido;

}
