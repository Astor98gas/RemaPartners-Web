package com.arsansys.RemaPartners.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.FacturaEntity;

@Service
public interface FacturaService {

    /**
     * Crea una nueva factura para un producto vendido
     * 
     * @param facturaEntity la entidad de factura a crear
     * @return la factura creada
     */
    FacturaEntity createFactura(FacturaEntity facturaEntity);

    /**
     * Obtiene una factura por su ID
     * 
     * @param id el ID de la factura
     * @return la factura encontrada o null si no existe
     */
    FacturaEntity getFacturaById(String id);

    /**
     * Obtiene todas las facturas donde el usuario es el comprador
     * 
     * @param idComprador el ID del comprador
     * @return lista de facturas donde el usuario es comprador
     */
    List<FacturaEntity> getFacturasByIdComprador(String idComprador);

    /**
     * Obtiene todas las facturas donde el usuario es el vendedor
     * 
     * @param idVendedor el ID del vendedor
     * @return lista de facturas donde el usuario es vendedor
     */
    List<FacturaEntity> getFacturasByIdVendedor(String idVendedor);

    /**
     * Obtiene todas las facturas asociadas a un producto
     * 
     * @param idProducto el ID del producto
     * @return lista de facturas del producto
     */
    List<FacturaEntity> getFacturasByIdProducto(String idProducto);

    /**
     * Obtiene todas las facturas asociadas a un chat
     * 
     * @param idChat el ID del chat
     * @return lista de facturas asociadas al chat
     */
    List<FacturaEntity> getFacturasByIdChat(String idChat);

    /**
     * Actualiza una factura existente
     * 
     * @param facturaEntity la factura con los datos actualizados
     * @return la factura actualizada
     */
    FacturaEntity updateFactura(FacturaEntity facturaEntity);
}
