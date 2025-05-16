package com.arsansys.RemaPartners.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.FacturaEntity;

@Repository
public interface FacturaRepository extends MongoRepository<FacturaEntity, String> {

    // Buscar facturas por ID del comprador
    List<FacturaEntity> findByIdComprador(String idComprador);

    // Buscar facturas por ID del vendedor
    List<FacturaEntity> findByIdVendedor(String idVendedor);

    // Buscar facturas por ID del producto
    List<FacturaEntity> findByIdProducto(String idProducto);

    // Buscar facturas por ID del chat
    List<FacturaEntity> findByIdChat(String idChat);
}
