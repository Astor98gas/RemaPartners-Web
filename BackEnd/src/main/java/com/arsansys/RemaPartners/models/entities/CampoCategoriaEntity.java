package com.arsansys.RemaPartners.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un campo personalizado de una categoría de producto.
 * Por ejemplo, para la categoría "Coches", un campo podría ser "Año de fabricación".
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampoCategoriaEntity {
    /**
     * Nombre del campo personalizado.
     */
    private String nombreCampo;

    /**
     * Datos o valor asociado al campo.
     */
    private String datos;
}
