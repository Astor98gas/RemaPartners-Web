package com.arsansys.RemaPartners.services.servicesImpl;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.models.enums.EEstado;
import com.arsansys.RemaPartners.models.enums.EMoneda;
import com.arsansys.RemaPartners.repositories.ProductoRepository;
import com.arsansys.RemaPartners.services.ProductoService;

/**
 * Implementación del servicio para la gestión de productos.
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Obtiene todos los productos.
     * 
     * @return Lista de productos.
     */
    @Override
    public List<ProductoEntity> getProductos() {
        try {
            return productoRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products: " + e.getMessage());
        }
    }

    /**
     * Obtiene un producto por su identificador.
     * 
     * @param id Identificador del producto.
     * @return Entidad de producto encontrada.
     */
    @Override
    public ProductoEntity getProductoById(String id) {
        try {
            return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving product by ID: " + e.getMessage());
        }
    }

    /**
     * Obtiene productos por estado.
     * 
     * @param estado Estado del producto.
     * @return Lista de productos con el estado especificado.
     */
    @Override
    public List<ProductoEntity> getProductosByEstado(EEstado estado) {
        try {
            return productoRepository.findByEstado(estado);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by state: " + e.getMessage());
        }
    }

    /**
     * Obtiene productos por moneda.
     * 
     * @param moneda Moneda del producto.
     * @return Lista de productos con la moneda especificada.
     */
    @Override
    public List<ProductoEntity> getProductosByMoneda(EMoneda moneda) {
        try {
            return productoRepository.findByMoneda(moneda);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by currency: " + e.getMessage());
        }
    }

    /**
     * Obtiene productos por identificador de categoría.
     * 
     * @param idCategoria Identificador de la categoría.
     * @return Lista de productos de la categoría.
     */
    @Override
    public List<ProductoEntity> getProductosByIdCategoria(String idCategoria) {
        try {
            return productoRepository.findByIdCategoria(idCategoria);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by category ID: " + e.getMessage());
        }
    }

    /**
     * Obtiene productos por marca.
     * 
     * @param marca Marca del producto.
     * @return Lista de productos de la marca.
     */
    @Override
    public List<ProductoEntity> getProductosByMarca(String marca) {
        try {
            return productoRepository.findByMarca(marca);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by brand: " + e.getMessage());
        }
    }

    /**
     * Obtiene productos por modelo.
     * 
     * @param modelo Modelo del producto.
     * @return Lista de productos del modelo.
     */
    @Override
    public List<ProductoEntity> getProductosByModelo(String modelo) {
        try {
            return productoRepository.findByModelo(modelo);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by model: " + e.getMessage());
        }
    }

    /**
     * Obtiene productos por título.
     * 
     * @param titulo Título del producto.
     * @return Lista de productos con el título especificado.
     */
    @Override
    public List<ProductoEntity> getProductosByTitulo(String titulo) {
        try {
            return productoRepository.findByTitulo(titulo);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by title: " + e.getMessage());
        }
    }

    /**
     * Obtiene productos por descripción.
     * 
     * @param descripcion Descripción del producto.
     * @return Lista de productos con la descripción especificada.
     */
    @Override
    public List<ProductoEntity> getProductosByDescripcion(String descripcion) {
        try {
            return productoRepository.findByDescripcion(descripcion);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving products by description: " + e.getMessage());
        }
    }

    /**
     * Crea un nuevo producto.
     * 
     * @param productoEntity Entidad de producto a crear.
     * @return Entidad de producto creada.
     */
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

    /**
     * Actualiza un producto existente.
     * 
     * @param productoEntity Entidad de producto a actualizar.
     * @return Entidad de producto actualizada.
     */
    @Override
    public ProductoEntity updateProducto(ProductoEntity productoEntity) {
        try {
            // Check if the product exists
            if (!productoRepository.existsById(productoEntity.getId())) {
                throw new RuntimeException("Product not found");
            }
            productoEntity.setFechaActualizacion(Instant.now().toString());
            return productoRepository.save(productoEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error updating product: " + e.getMessage());
        }
    }

    /**
     * Elimina un producto por su identificador.
     * 
     * @param id Identificador del producto a eliminar.
     */
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

    /**
     * Cambia el estado de activo/inactivo de un producto.
     * 
     * @param id Identificador del producto.
     */
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
