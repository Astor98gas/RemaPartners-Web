package com.arsansys.RemaPartners.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.models.enums.EEstado;
import com.arsansys.RemaPartners.models.enums.EMoneda;

/**
 * Repositorio para la entidad ProductoEntity.
 * Permite operaciones CRUD y consultas personalizadas sobre productos en
 * MongoDB.
 */
@Repository
public interface ProductoRepository extends MongoRepository<ProductoEntity, String> {

    /**
     * Busca productos por estado.
     * 
     * @param estado Estado del producto
     * @return Lista de productos
     */
    List<ProductoEntity> findByEstado(EEstado estado);

    /**
     * Busca productos por moneda.
     * 
     * @param moneda Moneda del producto
     * @return Lista de productos
     */
    List<ProductoEntity> findByMoneda(EMoneda moneda);

    /**
     * Busca productos por ID de categoría.
     * 
     * @param idCategoria ID de la categoría
     * @return Lista de productos
     */
    List<ProductoEntity> findByIdCategoria(String idCategoria);

    /**
     * Busca productos por marca.
     * 
     * @param marca Marca del producto
     * @return Lista de productos
     */
    List<ProductoEntity> findByMarca(String marca);

    /**
     * Busca productos por modelo.
     * 
     * @param modelo Modelo del producto
     * @return Lista de productos
     */
    List<ProductoEntity> findByModelo(String modelo);

    /**
     * Busca productos por título.
     * 
     * @param titulo Título del producto
     * @return Lista de productos
     */
    List<ProductoEntity> findByTitulo(String titulo);

    /**
     * Busca productos por descripción.
     * 
     * @param descripcion Descripción del producto
     * @return Lista de productos
     */
    List<ProductoEntity> findByDescripcion(String descripcion);

}
