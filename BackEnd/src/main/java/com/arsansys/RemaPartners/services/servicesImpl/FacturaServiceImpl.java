package com.arsansys.RemaPartners.services.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.FacturaEntity;
import com.arsansys.RemaPartners.repositories.FacturaRepository;
import com.arsansys.RemaPartners.services.FacturaService;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public FacturaEntity createFactura(FacturaEntity facturaEntity) {
        try {
            return facturaRepository.save(facturaEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating invoice: " + e.getMessage());
        }
    }

    @Override
    public FacturaEntity getFacturaById(String id) {
        try {
            return facturaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving invoice: " + e.getMessage());
        }
    }

    @Override
    public List<FacturaEntity> getFacturasByIdComprador(String idComprador) {
        try {
            return facturaRepository.findByIdComprador(idComprador);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving buyer invoices: " + e.getMessage());
        }
    }

    @Override
    public List<FacturaEntity> getFacturasByIdVendedor(String idVendedor) {
        try {
            return facturaRepository.findByIdVendedor(idVendedor);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving seller invoices: " + e.getMessage());
        }
    }

    @Override
    public List<FacturaEntity> getFacturasByIdProducto(String idProducto) {
        try {
            return facturaRepository.findByIdProducto(idProducto);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving product invoices: " + e.getMessage());
        }
    }

    @Override
    public List<FacturaEntity> getFacturasByIdChat(String idChat) {
        try {
            return facturaRepository.findByIdChat(idChat);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chat invoices: " + e.getMessage());
        }
    }

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
