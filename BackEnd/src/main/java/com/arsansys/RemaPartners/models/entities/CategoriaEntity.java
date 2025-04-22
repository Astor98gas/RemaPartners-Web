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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "categorias")
public class CategoriaEntity {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    @Size(max = 50)
    private String titulo;

    @NotBlank
    @Size(max = 500)
    private String descripcion;

    @NotBlank
    @Size(max = 20)
    private List<@NotBlank @Size(max = 500) String> campos;

}
