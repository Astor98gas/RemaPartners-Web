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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "productos")
public class ProductoEntity {

    @Id
    private String id;

    @NotBlank
    private String idUsuario;

    @NotBlank
    private String idCategoria;

    @NotBlank
    @Size(max = 10)
    private List<@NotBlank String> imagenes;

    @NotBlank
    @Size(max = 50)
    private String marca;

    @NotBlank
    @Size(max = 50)
    private String modelo;

    @NotBlank
    @Size(max = 50)
    private String titulo;

    @NotBlank
    @Size(max = 500)
    private String descripcion;

    @NotBlank
    private EEstado estado;

    @NotBlank
    @Size(max = 20)
    private Integer precioCentimos;

    @NotBlank
    @Size(max = 20)
    private EMoneda moneda;

    @NotBlank
    @Size(max = 20)
    private Integer stock;

    @NotBlank
    private String fechaCreacion;

    @NotBlank
    private String fechaActualizacion;

    @NotBlank
    private String fechaPublicacion;

    private String fechaBaja;

    @NotBlank
    private String direccion;

    @NotBlank
    @Default
    private Boolean activo = true;

    @NotBlank
    private Boolean destacado;

    @NotBlank
    private CampoCategoriaEntity[] camposCategoria;

    @NotBlank
    @Default
    private Long visitas = 0L;

}
