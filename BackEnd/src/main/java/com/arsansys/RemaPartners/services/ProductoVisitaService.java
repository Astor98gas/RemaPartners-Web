package com.arsansys.RemaPartners.services;

import java.util.List;
import java.util.Map;

import com.arsansys.RemaPartners.models.entities.ProductoVisitaEntity;

public interface ProductoVisitaService {

    ProductoVisitaEntity registrarVisita(String productoId, String vendedorId);

    List<ProductoVisitaEntity> obtenerVisitasPorProducto(String productoId);

    List<ProductoVisitaEntity> obtenerVisitasPorVendedor(String vendedorId);

    List<ProductoVisitaEntity> obtenerVisitasPorMes(int año, int mes);

    List<ProductoVisitaEntity> obtenerVisitasPorVendedorYMes(String vendedorId, int año, int mes);

    Map<String, Long> obtenerEstadisticasVisitasPorMes(String vendedorId, int año);

    long getTotalVisitasPorVendedor(String vendedorId);
}
