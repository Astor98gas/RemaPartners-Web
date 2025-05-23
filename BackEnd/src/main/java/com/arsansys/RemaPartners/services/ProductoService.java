package com.arsansys.RemaPartners.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.models.enums.EEstado;
import com.arsansys.RemaPartners.models.enums.EMoneda;

@Service
public interface ProductoService {

    /**
     * Obtiene la lista de todos los productos.
     * 
     * @return Lista de entidades de producto.
     */
    abstract List<ProductoEntity> getProductos();

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id ID del producto.
     * @return Entidad de producto encontrada o null si no existe.
     */
    abstract ProductoEntity getProductoById(String id);

    /**
     * Obtiene productos por su estado.
     * 
     * @param estado Estado del producto.
     * @return Lista de productos con el estado especificado.
     */
    abstract List<ProductoEntity> getProductosByEstado(EEstado estado);

    /**
     * Obtiene productos por su moneda.
     * 
     * @param moneda Moneda del producto.
     * @return Lista de productos con la moneda especificada.
     */
    abstract List<ProductoEntity> getProductosByMoneda(EMoneda moneda);

    /**
     * Obtiene productos por el ID de la categoría.
     * 
     * @param idCategoria ID de la categoría.
     * @return Lista de productos de la categoría.
     */
    abstract List<ProductoEntity> getProductosByIdCategoria(String idCategoria);

    /**
     * Obtiene productos por la marca.
     * 
     * @param marca Marca del producto.
     * @return Lista de productos de la marca.
     */
    abstract List<ProductoEntity> getProductosByMarca(String marca);

    /**
     * Obtiene productos por el modelo.
     * 
     * @param modelo Modelo del producto.
     * @return Lista de productos del modelo.
     */
    abstract List<ProductoEntity> getProductosByModelo(String modelo);

    /**
     * Obtiene productos por el título.
     * 
     * @param titulo Título del producto.
     * @return Lista de productos con el título especificado.
     */
    abstract List<ProductoEntity> getProductosByTitulo(String titulo);

    /**
     * Obtiene productos por la descripción.
     * 
     * @param descripcion Descripción del producto.
     * @return Lista de productos con la descripción especificada.
     */
    abstract List<ProductoEntity> getProductosByDescripcion(String descripcion);

    /**
     * Crea un nuevo producto.
     * 
     * @param productoEntity Entidad de producto a crear.
     * @return Entidad de producto creada.
     */
    abstract ProductoEntity createProducto(ProductoEntity productoEntity);

    /**
     * Actualiza un producto existente.
     * 
     * @param productoEntity Entidad de producto con los datos actualizados.
     * @return Entidad de producto actualizada.
     */
    abstract ProductoEntity updateProducto(ProductoEntity productoEntity);

    /**
     * Elimina un producto por su ID.
     * 
     * @param id ID del producto a eliminar.
     */
    abstract void deleteProductoById(String id);

    /**
     * Cambia el estado (activo/inactivo) de un producto.
     * 
     * @param id ID del producto.
     */
    abstract void toggleStatus(String id);

}
