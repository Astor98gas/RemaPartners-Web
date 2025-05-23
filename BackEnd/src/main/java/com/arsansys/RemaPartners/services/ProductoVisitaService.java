package com.arsansys.RemaPartners.services;

import java.util.List;
import java.util.Map;

import com.arsansys.RemaPartners.models.entities.ProductoVisitaEntity;

public interface ProductoVisitaService {

    /**
     * Registra una visita a un producto por parte de un vendedor.
     * 
     * @param productoId ID del producto visitado.
     * @param vendedorId ID del vendedor que recibe la visita.
     * @return Entidad de visita registrada.
     */
    ProductoVisitaEntity registrarVisita(String productoId, String vendedorId);

    /**
     * Obtiene todas las visitas realizadas a un producto específico.
     * 
     * @param productoId ID del producto.
     * @return Lista de visitas al producto.
     */
    List<ProductoVisitaEntity> obtenerVisitasPorProducto(String productoId);

    /**
     * Obtiene todas las visitas recibidas por un vendedor.
     * 
     * @param vendedorId ID del vendedor.
     * @return Lista de visitas recibidas por el vendedor.
     */
    List<ProductoVisitaEntity> obtenerVisitasPorVendedor(String vendedorId);

    /**
     * Obtiene todas las visitas realizadas en un mes y año específicos.
     * 
     * @param año Año de las visitas.
     * @param mes Mes de las visitas (1-12).
     * @return Lista de visitas del mes y año indicados.
     */
    List<ProductoVisitaEntity> obtenerVisitasPorMes(int año, int mes);

    /**
     * Obtiene todas las visitas recibidas por un vendedor en un mes y año
     * específicos.
     * 
     * @param vendedorId ID del vendedor.
     * @param año        Año de las visitas.
     * @param mes        Mes de las visitas (1-12).
     * @return Lista de visitas recibidas por el vendedor en el mes y año indicados.
     */
    List<ProductoVisitaEntity> obtenerVisitasPorVendedorYMes(String vendedorId, int año, int mes);

    /**
     * Obtiene estadísticas de visitas por mes para un vendedor en un año
     * específico.
     * 
     * @param vendedorId ID del vendedor.
     * @param año        Año de las visitas.
     * @return Mapa donde la clave es el mes y el valor es la cantidad de visitas.
     */
    Map<String, Long> obtenerEstadisticasVisitasPorMes(String vendedorId, int año);

    /**
     * Obtiene el total de visitas recibidas por un vendedor.
     * 
     * @param vendedorId ID del vendedor.
     * @return Número total de visitas recibidas.
     */
    long getTotalVisitasPorVendedor(String vendedorId);
}
