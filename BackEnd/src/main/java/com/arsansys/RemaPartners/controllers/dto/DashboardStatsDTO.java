package com.arsansys.RemaPartners.controllers.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardStatsDTO {
    private int totalProductos;
    private long totalVisitas;
    private Map<String, Long> visitasPorMes;
    private List<ProductoVisitaDTO> productosTopVisitas;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductoVisitaDTO {
        private String id;
        private String titulo;
        private Long visitas;
    }
}
