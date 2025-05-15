package com.arsansys.RemaPartners.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.ProductoVisitaEntity;

@Repository
public interface ProductoVisitaRepository extends MongoRepository<ProductoVisitaEntity, String> {

    List<ProductoVisitaEntity> findByProductoId(String productoId);

    List<ProductoVisitaEntity> findByVendedorId(String vendedorId);

    List<ProductoVisitaEntity> findByAñoAndMes(int año, int mes);

    List<ProductoVisitaEntity> findByVendedorIdAndAñoAndMes(String vendedorId, int año, int mes);

    Optional<ProductoVisitaEntity> findByProductoIdAndAñoAndMes(String productoId, int año, int mes);

    List<ProductoVisitaEntity> findByProductoIdOrderByAñoAscMesAsc(String productoId);
}
