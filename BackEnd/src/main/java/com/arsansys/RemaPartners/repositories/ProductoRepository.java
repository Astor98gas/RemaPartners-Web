package com.arsansys.RemaPartners.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.ProductoEntity;
import com.arsansys.RemaPartners.models.enums.EEstado;
import com.arsansys.RemaPartners.models.enums.EMoneda;

@Repository
public interface ProductoRepository extends MongoRepository<ProductoEntity, String> {

    List<ProductoEntity> findByEstado(EEstado estado);

    List<ProductoEntity> findByMoneda(EMoneda moneda);

    List<ProductoEntity> findByIdCategoria(String idCategoria);

    List<ProductoEntity> findByMarca(String marca);

    List<ProductoEntity> findByModelo(String modelo);

    List<ProductoEntity> findByTitulo(String titulo);

    List<ProductoEntity> findByDescripcion(String descripcion);

}
