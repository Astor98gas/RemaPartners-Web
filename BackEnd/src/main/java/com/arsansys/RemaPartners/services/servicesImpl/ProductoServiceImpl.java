package com.arsansys.RemaPartners.services.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.models.enums.EEstado;
import com.arsansys.RemaPartners.models.enums.EMoneda;
import com.arsansys.RemaPartners.repositories.ProductoRepository;
import com.arsansys.RemaPartners.services.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<ProductoEntity> getProductos() {
        try {
            return productoRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products: " + e.getMessage());
        }
    }

    @Override
    public ProductoEntity getProductoById(String id) {
        try {
            return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving product by ID: " + e.getMessage());
        }
    }

    @Override
    public List<ProductoEntity> getProductosByEstado(EEstado estado) {
        try {
            return productoRepository.findByEstado(estado);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by state: " + e.getMessage());
        }
    }

    @Override
    public List<ProductoEntity> getProductosByMoneda(EMoneda moneda) {
        try {
            return productoRepository.findByMoneda(moneda);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by currency: " + e.getMessage());
        }
    }

    @Override
    public List<ProductoEntity> getProductosByIdCategoria(String idCategoria) {
        try {
            return productoRepository.findByIdCategoria(idCategoria);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by category ID: " + e.getMessage());
        }
    }

    @Override
    public List<ProductoEntity> getProductosByMarca(String marca) {
        try {
            return productoRepository.findByMarca(marca);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by brand: " + e.getMessage());
        }
    }

    @Override
    public List<ProductoEntity> getProductosByModelo(String modelo) {
        try {
            return productoRepository.findByModelo(modelo);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by model: " + e.getMessage());
        }
    }

    @Override
    public List<ProductoEntity> getProductosByTitulo(String titulo) {
        try {
            return productoRepository.findByTitulo(titulo);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by title: " + e.getMessage());
        }
    }

    @Override
    public List<ProductoEntity> getProductosByDescripcion(String descripcion) {
        try {
            return productoRepository.findByDescripcion(descripcion);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by description: " + e.getMessage());
        }
    }

    @Override
    public ProductoEntity createProducto(ProductoEntity productoEntity) {
        try {
            // Check if the product already exists
            if (productoRepository.findAll().stream()
                    .anyMatch(producto -> producto.getTitulo().equalsIgnoreCase(productoEntity.getTitulo()))) {
                throw new RuntimeException("Product already exists");
            }
            return productoRepository.save(productoEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating product: " + e.getMessage());
        }
    }

    @Override
    public ProductoEntity updateProducto(ProductoEntity productoEntity) {
        try {
            // Check if the product exists
            if (!productoRepository.existsById(productoEntity.getId())) {
                throw new RuntimeException("Product not found");
            }
            return productoRepository.save(productoEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public void deleteProductoById(String id) {
        try {
            // Check if the product exists
            if (!productoRepository.existsById(id)) {
                throw new RuntimeException("Product not found");
            }
            productoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting product: " + e.getMessage());
        }
    }

    @Override
    public void toggleStatus(String id) {
        try {
            // Check if the product exists
            ProductoEntity producto = productoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            // Toggle the status
            producto.setActivo(!producto.getActivo());
            productoRepository.save(producto);
        } catch (Exception e) {
            throw new RuntimeException("Error toggling product status: " + e.getMessage());
        }
    }

}
