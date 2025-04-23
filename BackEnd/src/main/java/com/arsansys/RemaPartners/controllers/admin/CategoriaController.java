package com.arsansys.RemaPartners.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.entities.CategoriaEntity;
import com.arsansys.RemaPartners.services.CategoriaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("admin/categoria/create")
    public String createCategoria(@RequestBody CategoriaEntity categoriaEntity) {
        try {
            CategoriaEntity entity = categoriaService.createCategoria(categoriaEntity);
            return "Categoria added successfully with ID: " + entity.getId();
        } catch (Exception e) {
            return "Error adding categoria: " + e.getMessage();
        }
    }

    @PostMapping("admin/categoria/update")
    public String updateCategoria(@RequestBody CategoriaEntity categoriaEntity) {
        try {
            CategoriaEntity entity = categoriaService.updateCategoria(categoriaEntity);
            return "Categoria updated successfully with ID: " + entity.getId();
        } catch (Exception e) {
            return "Error updating categoria: " + e.getMessage();
        }
    }

    @PostMapping("admin/categoria/delete")
    public String deleteCategoria(@RequestBody CategoriaEntity categoriaEntity) {
        try {
            categoriaService.deleteCategoria(categoriaEntity);
            return "Categoria deleted successfully with ID: " + categoriaEntity.getId();
        } catch (Exception e) {
            return "Error deleting categoria: " + e.getMessage();
        }
    }

    @GetMapping("admin/categoria/getAll")
    public Iterable<CategoriaEntity> getCategorias() {
        try {
            return categoriaService.getCategorias();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching categorias: " + e.getMessage());
        }
    }

}
