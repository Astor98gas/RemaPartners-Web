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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Document(collection = "suscripciones")
public class Suscripcion {

    @Id
    private String id;

    @NotNull
    private String idUsuario;

    @NotNull
    @Default
    private LocalDate fechaCompra = LocalDate.now();

    @NotNull
    @Default
    private LocalDate fechaVencimiento = LocalDate.now().plusDays(30);
}
