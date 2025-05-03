package com.arsansys.RemaPartners.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampoCategoriaEntity {
    private String nombreCampo;
    private String datos;
}
