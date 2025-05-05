package com.arsansys.RemaPartners.services.servicesImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ChatEntity;
import com.arsansys.RemaPartners.models.entities.MensajeEntity;
import com.arsansys.RemaPartners.repositories.ChatRepository;
import com.arsansys.RemaPartners.services.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public ChatEntity createChat(ChatEntity chatEntity) {
        try {
            return chatRepository.save(chatEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating chat: " + e.getMessage());
        }
    }

    @Override
    public ChatEntity addMensaje(ChatEntity chatEntity, MensajeEntity mensajeEntity) {
        try {
            // Add the message to the chat
            chatEntity.getMensajes().add(mensajeEntity);
            // Update the last update date
            chatEntity.setUltimaActualizacion(LocalDateTime.now());
            // Save the updated chat
            return chatRepository.save(chatEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error adding message to chat: " + e.getMessage());
        }
    }

    @Override
    public ChatEntity getChatById(String id) {
        try {
            return chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Chat not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chat: " + e.getMessage());
        }
    }

    @Override
    public List<ChatEntity> findByIdProducto(String idProducto) {
        try {
            return chatRepository.findByIdProducto(idProducto);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chats by product ID: " + e.getMessage());
        }
    }

    @Override
    public List<ChatEntity> findByIdComprador(String idComprador) {
        try {
            return chatRepository.findByIdComprador(idComprador);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chats by buyer ID: " + e.getMessage());
        }
    }

    @Override
    public List<ChatEntity> findByIdVendedor(String idVendedor) {
        try {
            return chatRepository.findByIdVendedor(idVendedor);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chats by seller ID: " + e.getMessage());
        }
    }

    @Override
    public ChatEntity getChatByIdProductoAndIdCompradorAndIdVendedor(String idProducto, String idComprador,
            String idVendedor) {
        try {
            return chatRepository.findByIdProductoAndIdCompradorAndIdVendedor(idProducto, idComprador, idVendedor)
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("Chat not found"));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error retrieving chat by product ID, buyer ID, and seller ID: " + e.getMessage());
        }
    }

}
