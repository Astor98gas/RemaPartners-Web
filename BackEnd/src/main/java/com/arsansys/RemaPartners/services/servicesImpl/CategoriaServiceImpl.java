package com.arsansys.RemaPartners.services.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.CategoriaEntity;
import com.arsansys.RemaPartners.repositories.CategoriaRepository;
import com.arsansys.RemaPartners.services.CategoriaService;

/**
 * Implementación del servicio para la gestión de categorías.
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Obtiene todas las categorías disponibles.
     * 
     * @return Lista de entidades de categoría.
     */
    @Override
    public List<CategoriaEntity> getCategorias() {
        try {
            return categoriaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving categories: " + e.getMessage());
        }
    }

    /**
     * Obtiene una categoría por su identificador.
     * 
     * @param id Identificador de la categoría.
     * @return Entidad de categoría encontrada.
     */
    @Override
    public CategoriaEntity getCategoriaById(String id) {
        try {
            return categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving category by ID: " + e.getMessage());
        }
    }

    /**
     * Obtiene una categoría por su título.
     * 
     * @param titulo Título de la categoría.
     * @return Entidad de categoría encontrada.
     */
    @Override
    public CategoriaEntity getCategoriaByTitulo(String titulo) {
        try {
            return categoriaRepository.findAll().stream()
                    .filter(categoria -> categoria.getTitulo().equalsIgnoreCase(titulo))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving category by title: " + e.getMessage());
        }
    }

    /**
     * Crea una nueva categoría.
     * 
     * @param categoriaEntity Entidad de categoría a crear.
     * @return Entidad de categoría creada.
     */
    @Override
    public CategoriaEntity createCategoria(CategoriaEntity categoriaEntity) {
        try {
            // Check if the category already exists
            if (categoriaRepository.findAll().stream()
                    .anyMatch(categoria -> categoria.getTitulo().equalsIgnoreCase(categoriaEntity.getTitulo()))) {
                throw new RuntimeException("Category already exists");
            }
            return categoriaRepository.save(categoriaEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating category: " + e.getMessage());
        }
    }

    /**
     * Actualiza una categoría existente.
     * 
     * @param categoriaEntity Entidad de categoría a actualizar.
     * @return Entidad de categoría actualizada.
     */
    @Override
    public CategoriaEntity updateCategoria(CategoriaEntity categoriaEntity) {
        try {
            // Check if the category exists
            if (!categoriaRepository.existsById(categoriaEntity.getId())) {
                throw new RuntimeException("Category not found");
            }
            return categoriaRepository.save(categoriaEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error updating category: " + e.getMessage());
        }
    }

    /**
     * Elimina una categoría por su identificador.
     * 
     * @param id Identificador de la categoría a eliminar.
     */
    @Override
    public void deleteCategoriaById(String id) {
        try {
            // Check if the category exists
            if (!categoriaRepository.existsById(id)) {
                throw new RuntimeException("Category not found");
            }
            categoriaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting category: " + e.getMessage());
        }
    }

}
