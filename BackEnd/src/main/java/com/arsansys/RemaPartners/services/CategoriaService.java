package com.arsansys.RemaPartners.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.CategoriaEntity;

@Service
public interface CategoriaService {

    abstract List<CategoriaEntity> getCategorias();

    abstract CategoriaEntity getCategoriaById(String id);

    abstract CategoriaEntity getCategoriaByTitulo(String titulo);

    abstract CategoriaEntity createCategoria(CategoriaEntity categoriaEntity);

    abstract CategoriaEntity updateCategoria(CategoriaEntity categoriaEntity);

    abstract void deleteCategoriaById(String id);

}
