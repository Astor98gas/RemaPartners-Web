package com.arsansys.RemaPartners.services.servicesImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ChatEntity;
import com.arsansys.RemaPartners.models.entities.MensajeEntity;
import com.arsansys.RemaPartners.repositories.ChatRepository;
import com.arsansys.RemaPartners.services.ChatService;

/**
 * Implementación del servicio para la gestión de chats.
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    /**
     * Crea un nuevo chat.
     * 
     * @param chatEntity Entidad de chat a crear.
     * @return Entidad de chat creada.
     */
    @Override
    public ChatEntity createChat(ChatEntity chatEntity) {
        try {
            return chatRepository.save(chatEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating chat: " + e.getMessage());
        }
    }

    /**
     * Añade un mensaje a un chat existente.
     * 
     * @param chatEntity    Entidad de chat.
     * @param mensajeEntity Entidad de mensaje a añadir.
     * @return Entidad de chat actualizada.
     */
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

    /**
     * Obtiene un chat por su identificador.
     * 
     * @param id Identificador del chat.
     * @return Entidad de chat encontrada.
     */
    @Override
    public ChatEntity getChatById(String id) {
        try {
            return chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Chat not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chat: " + e.getMessage());
        }
    }

    /**
     * Busca chats por identificador de producto.
     * 
     * @param idProducto Identificador del producto.
     * @return Lista de chats asociados al producto.
     */
    @Override
    public List<ChatEntity> findByIdProducto(String idProducto) {
        try {
            return chatRepository.findByIdProducto(idProducto);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chats by product ID: " + e.getMessage());
        }
    }

    /**
     * Busca chats por identificador de comprador.
     * 
     * @param idComprador Identificador del comprador.
     * @return Lista de chats asociados al comprador.
     */
    @Override
    public List<ChatEntity> findByIdComprador(String idComprador) {
        try {
            return chatRepository.findByIdComprador(idComprador);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chats by buyer ID: " + e.getMessage());
        }
    }

    /**
     * Busca chats por identificador de vendedor.
     * 
     * @param idVendedor Identificador del vendedor.
     * @return Lista de chats asociados al vendedor.
     */
    @Override
    public List<ChatEntity> findByIdVendedor(String idVendedor) {
        try {
            return chatRepository.findByIdVendedor(idVendedor);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving chats by seller ID: " + e.getMessage());
        }
    }

    /**
     * Obtiene un chat por identificador de producto, comprador y vendedor.
     * 
     * @param idProducto  Identificador del producto.
     * @param idComprador Identificador del comprador.
     * @param idVendedor  Identificador del vendedor.
     * @return Entidad de chat encontrada.
     */
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

    /**
     * Elimina un chat por su identificador.
     * 
     * @param id Identificador del chat a eliminar.
     */
    @Override
    public void deleteChat(String id) {
        try {
            // Verificar que el chat existe antes de eliminarlo
            ChatEntity chat = getChatById(id);
            chatRepository.delete(chat);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting chat: " + e.getMessage());
        }
    }

}
