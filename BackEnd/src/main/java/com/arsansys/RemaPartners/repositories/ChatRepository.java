package com.arsansys.RemaPartners.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsansys.RemaPartners.models.entities.ChatEntity;

@Repository
public interface ChatRepository extends MongoRepository<ChatEntity, String> {

    List<ChatEntity> findByIdProducto(String idProducto);

    List<ChatEntity> findByIdComprador(String idComprador);

    List<ChatEntity> findByIdVendedor(String idVendedor);

    List<ChatEntity> findByIdProductoAndIdCompradorAndIdVendedor(String idProducto, String idComprador,
            String idVendedor);

}
