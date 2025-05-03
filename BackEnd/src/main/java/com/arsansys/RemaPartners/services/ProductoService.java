package com.arsansys.RemaPartners.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.models.enums.EEstado;
import com.arsansys.RemaPartners.models.enums.EMoneda;

@Service
public interface ProductoService {

    abstract List<ProductoEntity> getProductos();

    abstract ProductoEntity getProductoById(String id);

    abstract List<ProductoEntity> getProductosByEstado(EEstado estado);

    abstract List<ProductoEntity> getProductosByMoneda(EMoneda moneda);

    abstract List<ProductoEntity> getProductosByIdCategoria(String idCategoria);

    abstract List<ProductoEntity> getProductosByMarca(String marca);

    abstract List<ProductoEntity> getProductosByModelo(String modelo);

    abstract List<ProductoEntity> getProductosByTitulo(String titulo);

    abstract List<ProductoEntity> getProductosByDescripcion(String descripcion);

    abstract ProductoEntity createProducto(ProductoEntity productoEntity);

    abstract ProductoEntity updateProducto(ProductoEntity productoEntity);

    abstract void deleteProductoById(String id);

    abstract void toggleStatus(String id);

}
