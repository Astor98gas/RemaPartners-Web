package com.arsansys.RemaPartners.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.ChatEntity;
import com.arsansys.RemaPartners.models.entities.MensajeEntity;

@Service
public interface ChatService {

    /**
     * Crea un nuevo chat.
     * 
     * @param chatEntity Entidad de chat a crear.
     * @return Chat creado.
     */
    abstract ChatEntity createChat(ChatEntity chatEntity);

    /**
     * Añade un mensaje a un chat existente.
     * 
     * @param chatEntity    Chat al que se añade el mensaje.
     * @param mensajeEntity Mensaje a añadir.
     * @return Chat actualizado con el nuevo mensaje.
     */
    abstract ChatEntity addMensaje(ChatEntity chatEntity, MensajeEntity mensajeEntity);

    /**
     * Obtiene un chat por su ID.
     * 
     * @param id ID del chat.
     * @return Chat encontrado o null si no existe.
     */
    abstract ChatEntity getChatById(String id);

    /**
     * Busca chats por el ID del producto.
     * 
     * @param idProducto ID del producto.
     * @return Lista de chats asociados al producto.
     */
    List<ChatEntity> findByIdProducto(String idProducto);

    /**
     * Busca chats por el ID del comprador.
     * 
     * @param idComprador ID del comprador.
     * @return Lista de chats asociados al comprador.
     */
    List<ChatEntity> findByIdComprador(String idComprador);

    /**
     * Busca chats por el ID del vendedor.
     * 
     * @param idVendedor ID del vendedor.
     * @return Lista de chats asociados al vendedor.
     */
    List<ChatEntity> findByIdVendedor(String idVendedor);

    /**
     * Obtiene un chat por producto, comprador y vendedor.
     * 
     * @param idProducto  ID del producto.
     * @param idComprador ID del comprador.
     * @param idVendedor  ID del vendedor.
     * @return Chat encontrado o null si no existe.
     */
    abstract ChatEntity getChatByIdProductoAndIdCompradorAndIdVendedor(String idProducto, String idComprador,
            String idVendedor);

    /**
     * Elimina un chat por su ID.
     * 
     * @param id ID del chat a eliminar.
     */
    abstract void deleteChat(String id);

}
