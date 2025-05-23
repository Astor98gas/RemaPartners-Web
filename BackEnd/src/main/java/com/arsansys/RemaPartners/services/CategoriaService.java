package com.arsansys.RemaPartners.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.CategoriaEntity;

@Service
public interface CategoriaService {

    /**
     * Obtiene la lista de todas las categorías.
     * 
     * @return Lista de entidades de categoría.
     */
    abstract List<CategoriaEntity> getCategorias();

    /**
     * Obtiene una categoría por su ID.
     * 
     * @param id ID de la categoría.
     * @return Entidad de categoría encontrada o null si no existe.
     */
    abstract CategoriaEntity getCategoriaById(String id);

    /**
     * Obtiene una categoría por su título.
     * 
     * @param titulo Título de la categoría.
     * @return Entidad de categoría encontrada o null si no existe.
     */
    abstract CategoriaEntity getCategoriaByTitulo(String titulo);

    /**
     * Crea una nueva categoría.
     * 
     * @param categoriaEntity Entidad de categoría a crear.
     * @return Entidad de categoría creada.
     */
    abstract CategoriaEntity createCategoria(CategoriaEntity categoriaEntity);

    /**
     * Actualiza una categoría existente.
     * 
     * @param categoriaEntity Entidad de categoría con los datos actualizados.
     * @return Entidad de categoría actualizada.
     */
    abstract CategoriaEntity updateCategoria(CategoriaEntity categoriaEntity);

    /**
     * Elimina una categoría por su ID.
     * 
     * @param id ID de la categoría a eliminar.
     */
    abstract void deleteCategoriaById(String id);

}
