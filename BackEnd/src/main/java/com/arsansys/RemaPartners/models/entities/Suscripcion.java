package com.arsansys.RemaPartners.models.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Document(collection = "suscripciones")
public class Suscripcion {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private Integer precio;
}
