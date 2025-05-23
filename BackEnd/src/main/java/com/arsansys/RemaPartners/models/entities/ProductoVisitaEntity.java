package com.arsansys.RemaPartners.models.entities;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa el registro de visitas de un producto por mes y año.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "producto_visitas")
public class ProductoVisitaEntity {

    /**
     * Identificador único del registro de visitas.
     */
    @Id
    private String id;

    /**
     * Identificador del producto.
     */
    private String productoId;

    /**
     * Identificador del vendedor del producto.
     */
    private String vendedorId;

    /**
     * Año del registro de visitas.
     */
    private int año;

    /**
     * Mes del registro de visitas.
     */
    private int mes;

    /**
     * Cantidad de visitas en el mes y año indicados.
     */
    private long cantidadVisitas;

    /**
     * Fecha y hora de la última actualización del registro.
     */
    private LocalDateTime ultimaActualizacion;
}
