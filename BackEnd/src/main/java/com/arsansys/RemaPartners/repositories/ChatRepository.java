package com.arsansys.RemaPartners.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.ChatEntity;

/**
 * Repositorio para la entidad ChatEntity.
 * Permite operaciones CRUD y consultas personalizadas sobre chats en MongoDB.
 */
@Repository
public interface ChatRepository extends MongoRepository<ChatEntity, String> {

    /**
     * Busca chats por ID de producto.
     * 
     * @param idProducto ID del producto
     * @return Lista de chats
     */
    List<ChatEntity> findByIdProducto(String idProducto);

    /**
     * Busca chats por ID de comprador.
     * 
     * @param idComprador ID del comprador
     * @return Lista de chats
     */
    List<ChatEntity> findByIdComprador(String idComprador);

    /**
     * Busca chats por ID de vendedor.
     * 
     * @param idVendedor ID del vendedor
     * @return Lista de chats
     */
    List<ChatEntity> findByIdVendedor(String idVendedor);

    /**
     * Busca chats por ID de producto, comprador y vendedor.
     * 
     * @param idProducto  ID del producto
     * @param idComprador ID del comprador
     * @param idVendedor  ID del vendedor
     * @return Lista de chats
     */
    List<ChatEntity> findByIdProductoAndIdCompradorAndIdVendedor(String idProducto, String idComprador,
            String idVendedor);

}
