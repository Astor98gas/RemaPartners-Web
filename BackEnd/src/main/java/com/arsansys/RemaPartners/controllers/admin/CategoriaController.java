package com.arsansys.RemaPartners.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.entities.CategoriaEntity;
import com.arsansys.RemaPartners.services.CategoriaService;
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

}
