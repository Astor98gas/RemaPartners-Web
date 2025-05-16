package com.arsansys.RemaPartners.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ChatEntity;
import com.arsansys.RemaPartners.models.entities.MensajeEntity;

@Service
public interface ChatService {

    abstract ChatEntity createChat(ChatEntity chatEntity);

    abstract ChatEntity addMensaje(ChatEntity chatEntity, MensajeEntity mensajeEntity);

    abstract ChatEntity getChatById(String id);

    List<ChatEntity> findByIdProducto(String idProducto);

    List<ChatEntity> findByIdComprador(String idComprador);

    List<ChatEntity> findByIdVendedor(String idVendedor);

    abstract ChatEntity getChatByIdProductoAndIdCompradorAndIdVendedor(String idProducto, String idComprador,
            String idVendedor);

    abstract void deleteChat(String id);

}
