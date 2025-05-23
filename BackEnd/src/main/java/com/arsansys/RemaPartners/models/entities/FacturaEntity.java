package com.arsansys.RemaPartners.models.entities;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 * Entidad que representa una factura de venta.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "facturas")
public class FacturaEntity {

    /**
     * Identificador único de la factura.
     */
    @Id
    private String id;

    /**
     * ID del producto vendido.
     */
    private String idProducto;

    /**
     * ID del comprador.
     */
    private String idComprador;

    /**
     * ID del vendedor.
     */
    private String idVendedor;

    /**
     * Cantidad de unidades vendidas.
     */
    private Integer cantidad;

    /**
     * Precio unitario en céntimos.
     */
    private Integer precioCentimos;

    /**
     * Importe total de la factura en céntimos (precio * cantidad).
     */
    private Integer importeTotalCentimos;

    /**
     * Moneda utilizada en la factura.
     */
    private String moneda;

    /**
     * Título del producto en el momento de la venta.
     */
    private String tituloProducto;

    /**
     * Fecha de emisión de la factura.
     */
    @Default
    private LocalDateTime fechaEmision = LocalDateTime.now();

    /**
     * Referencia al chat donde se realizó la venta.
     */
    private String idChat;

    /**
     * Estado de la factura (por ejemplo: EMITIDA, PAGADA, CANCELADA, etc.).
     */
    @Default
    private String estado = "EMITIDA";
}
