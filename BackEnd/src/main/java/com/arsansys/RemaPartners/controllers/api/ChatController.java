package com.arsansys.RemaPartners.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arsansys.RemaPartners.models.entities.ChatEntity;
import com.arsansys.RemaPartners.models.entities.MensajeEntity;
import com.arsansys.RemaPartners.services.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * Crea un nuevo chat
     * 
     * @param chatEntity Datos del chat a crear
     * @return El chat creado
     */
    @PostMapping("/create")
    public ResponseEntity<ChatEntity> createChat(@RequestBody ChatEntity chatEntity) {
        try {
            if (chatEntity.getIdComprador() == chatEntity.getIdVendedor()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            ChatEntity newChat = chatService.createChat(chatEntity);
            return new ResponseEntity<>(newChat, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un chat por su ID
     * 
     * @param id ID del chat
     * @return El chat encontrado o error si no existe
     */
    @GetMapping("/getById/{id}")
    public ResponseEntity<ChatEntity> getChatById(@PathVariable("id") String id) {
        try {
            ChatEntity chat = chatService.getChatById(id);
            if (chat != null) {
                return new ResponseEntity<>(chat, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Agrega un mensaje a un chat existente
     * 
     * @param id      ID del chat
     * @param mensaje Mensaje a agregar
     * @return El chat actualizado con el nuevo mensaje
     */
    @PostMapping("/addMessage/{id}")
    public ResponseEntity<ChatEntity> addMessage(@PathVariable("id") String id, @RequestBody MensajeEntity mensaje) {
        try {
            ChatEntity chat = chatService.getChatById(id);
            if (chat != null) {
                ChatEntity updatedChat = chatService.addMensaje(chat, mensaje);
                return new ResponseEntity<>(updatedChat, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene chats por ID de producto
     * 
     * @param idProducto ID del producto
     * @return Lista de chats asociados al producto
     */
    @GetMapping("/getByProductId/{idProducto}")
    public ResponseEntity<List<ChatEntity>> getChatsByProductId(@PathVariable("idProducto") String idProducto) {
        try {
            List<ChatEntity> chats = chatService.findByIdProducto(idProducto);
            return new ResponseEntity<>(chats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene chats por ID de comprador
     * 
     * @param idComprador ID del comprador
     * @return Lista de chats del comprador
     */
    @GetMapping("/getByBuyerId/{idComprador}")
    public ResponseEntity<List<ChatEntity>> getChatsByBuyerId(@PathVariable("idComprador") String idComprador) {
        try {
            List<ChatEntity> chats = chatService.findByIdComprador(idComprador);
            return new ResponseEntity<>(chats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene chats por ID de vendedor
     * 
     * @param idVendedor ID del vendedor
     * @return Lista de chats del vendedor
     */
    @GetMapping("/getBySellerId/{idVendedor}")
    public ResponseEntity<List<ChatEntity>> getChatsBySellerId(@PathVariable("idVendedor") String idVendedor) {
        try {
            List<ChatEntity> chats = chatService.findByIdVendedor(idVendedor);
            return new ResponseEntity<>(chats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un chat por la combinación de producto, comprador y vendedor
     * 
     * @param idProducto  ID del producto
     * @param idComprador ID del comprador
     * @param idVendedor  ID del vendedor
     * @return El chat encontrado o error si no existe
     */
    @GetMapping("/getByParticipants/{idProducto}/{idComprador}/{idVendedor}")
    public ResponseEntity<ChatEntity> getChatByParticipants(
            @PathVariable("idProducto") String idProducto,
            @PathVariable("idComprador") String idComprador,
            @PathVariable("idVendedor") String idVendedor) {
        try {
            ChatEntity chat = chatService.getChatByIdProductoAndIdCompradorAndIdVendedor(
                    idProducto, idComprador, idVendedor);
            if (chat != null) {
                return new ResponseEntity<>(chat, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}