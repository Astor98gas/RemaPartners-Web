package com.arsansys.RemaPartners.services;

import java.util.Map;

/**
 * Servicio para estadísticas del dashboard de ventas
 */
public interface VentasDashboardService {

    /**
     * Obtiene estadísticas generales para el dashboard de ventas
     * 
     * @param year Año para filtrar las estadísticas
     * @return Mapa con las estadísticas generales
     */
    Map<String, Object> getEstadisticasGenerales(int year);

    /**
     * Obtiene estadísticas de ventas para un producto específico
     * 
     * @param productoId ID del producto
     * @return Mapa con estadísticas del producto
     */
    Map<String, Object> getEstadisticasProducto(String productoId);

    /**
     * Obtiene estadísticas detalladas de ventas de un mes específico
     * 
     * @param año Año del mes a consultar
     * @param mes Mes a consultar (1-12)
     * @return Mapa con estadísticas del mes
     */
    Map<String, Object> getEstadisticasMes(int año, int mes);

    /**
     * Obtiene estadísticas de ventas para un usuario (como vendedor)
     * 
     * @param userId ID del usuario
     * @param year   Año para filtrar las estadísticas
     * @return Mapa con estadísticas de ventas del usuario
     */
    Map<String, Object> getEstadisticasVentasUsuario(String userId, int year);

    /**
     * Obtiene estadísticas de compras para un usuario (como comprador)
     * 
     * @param userId ID del usuario
     * @param year   Año para filtrar las estadísticas
     * @return Mapa con estadísticas de compras del usuario
     */
    Map<String, Object> getEstadisticasComprasUsuario(String userId, int year);
}
