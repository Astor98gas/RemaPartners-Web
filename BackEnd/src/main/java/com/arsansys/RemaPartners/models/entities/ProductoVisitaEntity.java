package com.arsansys.RemaPartners.models.entities;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "producto_visitas")
public class ProductoVisitaEntity {

    @Id
    private String id;

    private String productoId;

    private String vendedorId;

    private int año;

    private int mes;

    private long cantidadVisitas;

    private LocalDateTime ultimaActualizacion;
}
