package com.arsansys.RemaPartners.controllers.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para estadísticas del dashboard de la plataforma.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardStatsDTO {
    /**
     * Número total de productos.
     */
    private int totalProductos;

    /**
     * Número total de visitas.
     */
    private long totalVisitas;

    /**
     * Mapa con el número de visitas por mes.
     */
    private Map<String, Long> visitasPorMes;

    /**
     * Lista de productos con más visitas.
     */
    private List<ProductoVisitaDTO> productosTopVisitas;

    /**
     * DTO para información de visitas de un producto.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductoVisitaDTO {
        /**
         * ID del producto.
         */
        private String id;

        /**
         * Título del producto.
         */
        private String titulo;

        /**
         * Número de visitas del producto.
         */
        private Long visitas;
    }
}
