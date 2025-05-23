package com.arsansys.RemaPartners.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.ProductoVisitaEntity;

/**
 * Repositorio para la entidad ProductoVisitaEntity.
 * Permite operaciones CRUD y consultas personalizadas sobre visitas a productos
 * en MongoDB.
 */
@Repository
public interface ProductoVisitaRepository extends MongoRepository<ProductoVisitaEntity, String> {

    /**
     * Busca visitas por ID de producto.
     * 
     * @param productoId ID del producto
     * @return Lista de visitas
     */
    List<ProductoVisitaEntity> findByProductoId(String productoId);

    /**
     * Busca visitas por ID de vendedor.
     * 
     * @param vendedorId ID del vendedor
     * @return Lista de visitas
     */
    List<ProductoVisitaEntity> findByVendedorId(String vendedorId);

    /**
     * Busca visitas por año y mes.
     * 
     * @param año Año
     * @param mes Mes
     * @return Lista de visitas
     */
    List<ProductoVisitaEntity> findByAñoAndMes(int año, int mes);

    /**
     * Busca visitas por vendedor, año y mes.
     * 
     * @param vendedorId ID del vendedor
     * @param año        Año
     * @param mes        Mes
     * @return Lista de visitas
     */
    List<ProductoVisitaEntity> findByVendedorIdAndAñoAndMes(String vendedorId, int año, int mes);

    /**
     * Busca una visita por producto, año y mes.
     * 
     * @param productoId ID del producto
     * @param año        Año
     * @param mes        Mes
     * @return Visita si existe
     */
    Optional<ProductoVisitaEntity> findByProductoIdAndAñoAndMes(String productoId, int año, int mes);

    /**
     * Busca visitas por producto y las ordena por año y mes ascendente.
     * 
     * @param productoId ID del producto
     * @return Lista de visitas ordenadas
     */
    List<ProductoVisitaEntity> findByProductoIdOrderByAñoAscMesAsc(String productoId);
}
