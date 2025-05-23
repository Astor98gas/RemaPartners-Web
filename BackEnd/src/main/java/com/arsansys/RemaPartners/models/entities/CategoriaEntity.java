package com.arsansys.RemaPartners.models.entities;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una categoría de productos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "categorias")
public class CategoriaEntity {

    /**
     * Identificador único de la categoría.
     */
    @Id
    private String id;

    /**
     * Título de la categoría.
     */
    @NotBlank
    @Indexed(unique = true)
    @Size(max = 50)
    private String titulo;

    /**
     * Descripción de la categoría.
     */
    @NotBlank
    @Size(max = 500)
    private String descripcion;

    /**
     * Lista de nombres de campos personalizados para la categoría.
     */
    @NotBlank
    @Size(max = 20)
    private List<@NotBlank @Size(max = 20) String> campos;

}
