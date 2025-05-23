package com.arsansys.RemaPartners.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.FacturaEntity;

/**
 * Repositorio para la entidad FacturaEntity.
 * Permite operaciones CRUD y consultas personalizadas sobre facturas en
 * MongoDB.
 */
@Repository
public interface FacturaRepository extends MongoRepository<FacturaEntity, String> {

    /**
     * Buscar facturas por ID del comprador.
     * 
     * @param idComprador ID del comprador
     * @return Lista de facturas
     */
    List<FacturaEntity> findByIdComprador(String idComprador);

    /**
     * Buscar facturas por ID del vendedor.
     * 
     * @param idVendedor ID del vendedor
     * @return Lista de facturas
     */
    List<FacturaEntity> findByIdVendedor(String idVendedor);

    /**
     * Buscar facturas por ID del producto.
     * 
     * @param idProducto ID del producto
     * @return Lista de facturas
     */
    List<FacturaEntity> findByIdProducto(String idProducto);

    /**
     * Buscar facturas por ID del chat.
     * 
     * @param idChat ID del chat
     * @return Lista de facturas
     */
    List<FacturaEntity> findByIdChat(String idChat);
}
