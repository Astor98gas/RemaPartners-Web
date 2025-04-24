package com.arsansys.RemaPartners.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.entities.CategoriaEntity;
import com.arsansys.RemaPartners.services.CategoriaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("admin/categoria/create")
    public ResponseEntity<?> createCategoria(@RequestBody CategoriaEntity categoriaEntity) {
        try {
            CategoriaEntity entity = categoriaService.createCategoria(categoriaEntity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Categoria added successfully with ID: " + entity.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error adding categoria: " + e.getMessage());
        }
    }

    @PostMapping("admin/categoria/update/{id}")
    public ResponseEntity<?> updateCategoria(@PathVariable String id, @RequestBody CategoriaEntity categoriaEntity) {
        try {
            categoriaEntity.setId(id);
            CategoriaEntity entity = categoriaService.updateCategoria(categoriaEntity);
            return ResponseEntity.ok("Categoria updated successfully with ID: " + entity.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating categoria: " + e.getMessage());
        }
    }

    @DeleteMapping("admin/categoria/delete/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable String id) {
        try {
            categoriaService.deleteCategoriaById(id);
            return ResponseEntity.ok("Categoria deleted successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error deleting categoria: " + e.getMessage());
        }
    }

    @GetMapping("admin/categoria/getAll")
    public ResponseEntity<?> getCategorias() {
        try {
            Iterable<CategoriaEntity> categorias = categoriaService.getCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching categorias: " + e.getMessage());
        }
    }

    @GetMapping("admin/categoria/getById/{id}")
    public ResponseEntity<?> getCategoriaById(@PathVariable String id) {
        try {
            CategoriaEntity categoria = categoriaService.getCategoriaById(id);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error fetching categoria: " + e.getMessage());
        }
    }
}
