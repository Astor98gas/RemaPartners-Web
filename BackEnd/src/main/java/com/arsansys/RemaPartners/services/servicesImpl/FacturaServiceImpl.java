package com.arsansys.RemaPartners.services.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.FacturaEntity;
import com.arsansys.RemaPartners.repositories.FacturaRepository;
import com.arsansys.RemaPartners.services.FacturaService;

/**
 * Implementación del servicio para la gestión de facturas.
 */
@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    /**
     * Crea una nueva factura.
     * 
     * @param facturaEntity Entidad de factura a crear.
     * @return Entidad de factura creada.
     */
    @Override
    public FacturaEntity createFactura(FacturaEntity facturaEntity) {
        try {
            return facturaRepository.save(facturaEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating invoice: " + e.getMessage());
        }
    }

    /**
     * Obtiene una factura por su identificador.
     * 
     * @param id Identificador de la factura.
     * @return Entidad de factura encontrada.
     */
    @Override
    public FacturaEntity getFacturaById(String id) {
        try {
            return facturaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving invoice: " + e.getMessage());
        }
    }

    /**
     * Obtiene las facturas por identificador de comprador.
     * 
     * @param idComprador Identificador del comprador.
     * @return Lista de facturas asociadas al comprador.
     */
    @Override
    public List<FacturaEntity> getFacturasByIdComprador(String idComprador) {
        try {
            return facturaRepository.findByIdComprador(idComprador);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving buyer invoices: " + e.getMessage());
        }
    }

    /**
     * Obtiene las facturas por identificador de vendedor.
     * 
     * @param idVendedor Identificador del vendedor.
     * @return Lista de facturas asociadas al vendedor.
     */
    @Override
    public List<FacturaEntity> getFacturasByIdVendedor(String idVendedor) {
        try {
            return facturaRepository.findByIdVendedor(idVendedor);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving seller invoices: " + e.getMessage());
        }
    }

    /**
     * Obtiene las facturas por identificador de producto.
     * 
     * @param idProducto Identificador del producto.
     * @return Lista de facturas asociadas al producto.
     */
    @Override
    public List<FacturaEntity> getFacturasByIdProducto(String idProducto) {
        try {
            return facturaRepository.findByIdProducto(idProducto);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving product invoices: " + e.getMessage());
        }
    }

    /**
     * Obtiene las facturas por identificador de chat.
     * 
     * @param idChat Identificador del chat.
     * @return Lista de facturas asociadas al chat.
     */
    @Override
    public List<FacturaEntity> getFacturasByIdChat(String idChat) {
        try {
            return facturaRepository.findByIdChat(idChat);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chat invoices: " + e.getMessage());
        }
    }

    /**
     * Actualiza una factura existente.
     * 
     * @param facturaEntity Entidad de factura a actualizar.
     * @return Entidad de factura actualizada.
     */
    @Override
    public FacturaEntity updateFactura(FacturaEntity facturaEntity) {
        try {
            if (!facturaRepository.existsById(facturaEntity.getId())) {
                throw new RuntimeException("Invoice not found with ID: " + facturaEntity.getId());
            }
            return facturaRepository.save(facturaEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error updating invoice: " + e.getMessage());
        }
    }
}
