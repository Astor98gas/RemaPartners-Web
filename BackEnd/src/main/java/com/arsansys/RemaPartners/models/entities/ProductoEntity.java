package com.arsansys.RemaPartners.models.entities;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arsansys.RemaPartners.models.enums.EEstado;
import com.arsansys.RemaPartners.models.enums.EMoneda;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 * Entidad que representa un producto publicado en la plataforma.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "productos")
public class ProductoEntity {

    /**
     * Identificador único del producto.
     */
    @Id
    private String id;

    /**
     * Identificador del usuario vendedor.
     */
    @NotBlank
    private String idUsuario;

    /**
     * Identificador de la categoría del producto.
     */
    @NotBlank
    private String idCategoria;

    /**
     * Lista de URLs de imágenes del producto.
     */
    @NotBlank
    @Size(max = 10)
    private List<@NotBlank String> imagenes;

    /**
     * Marca del producto.
     */
    @NotBlank
    @Size(max = 50)
    private String marca;

    /**
     * Modelo del producto.
     */
    @NotBlank
    @Size(max = 50)
    private String modelo;

    /**
     * Título del anuncio del producto.
     */
    @NotBlank
    @Size(max = 50)
    private String titulo;

    /**
     * Descripción del producto.
     */
    @NotBlank
    @Size(max = 500)
    private String descripcion;

    /**
     * Estado del producto (nuevo, usado, etc.).
     */
    @NotBlank
    private EEstado estado;

    /**
     * Precio en céntimos.
     */
    @NotBlank
    @Size(max = 20)
    private Integer precioCentimos;

    /**
     * Moneda del precio.
     */
    @NotBlank
    @Size(max = 20)
    private EMoneda moneda;

    /**
     * Stock disponible.
     */
    @NotBlank
    @Size(max = 20)
    private Integer stock;

    /**
     * Fecha de creación del producto.
     */
    @NotBlank
    private String fechaCreacion;

    /**
     * Fecha de última actualización del producto.
     */
    @NotBlank
    private String fechaActualizacion;

    /**
     * Fecha de publicación del producto.
     */
    @NotBlank
    private String fechaPublicacion;

    /**
     * Fecha de baja del producto (opcional).
     */
    private String fechaBaja;

    /**
     * Dirección donde se encuentra el producto.
     */
    @NotBlank
    private String direccion;

    /**
     * Indica si el producto está activo.
     */
    @NotBlank
    @Default
    private Boolean activo = true;

    /**
     * Indica si el producto es destacado.
     */
    @NotBlank
    private Boolean destacado;

    /**
     * Campos personalizados de la categoría.
     */
    @NotBlank
    private CampoCategoriaEntity[] camposCategoria;

    /**
     * Número de visitas al producto.
     */
    @NotBlank
    @Default
    private Long visitas = 0L;

}
