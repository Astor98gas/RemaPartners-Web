import { ref } from "vue";
import { chatService } from "@/services/chat.service";
import { userService } from "@/services/user.service";
import type { ChatEntity, MensajeEntity } from "@/models/chat";
import type { User } from "@/models/user";

/**
 * Composable para gestionar chats y mensajes.
 * Permite obtener, crear, eliminar chats y añadir mensajes.
 */
export function useChat() {
    const currentChat = ref<ChatEntity | null>(null);
    const chats = ref<ChatEntity[]>([]);
    const error = ref<string | null>(null);
    const success = ref<string | null>(null);
    const loading = ref<boolean>(false);

    /**
     * Obtiene un chat por su ID único.
     * @param {string} id - ID del chat.
     * @returns {Promise<any>} Chat encontrado.
     * @throws Error si ocurre un problema al buscar.
     */
    const getChatById = async (id: string) => {
        try {
            loading.value = true;
            error.value = null;
            // Comentado: console.log de solicitud de chat
            // console.log("Solicitando chat por ID:", id);
            const response = await chatService.getChatById(id);
            // Comentado: console.log de respuesta
            // console.log("Respuesta del chat por ID:", response);
            currentChat.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error obteniendo chat por ID:", err);
            error.value = err.response?.data?.message || "Error obteniendo chat por ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todos los chats asociados a un producto específico.
     * @param {string} idProducto - ID del producto.
     * @returns {Promise<void>}
     * @throws Error si ocurre un problema al buscar.
     */
    const getChatsByProductId = async (idProducto: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await chatService.getChatsByProductId(idProducto);
            chats.value = response.data;
        } catch (err: any) {
            console.error("Error obteniendo chats por ID de producto:", err);
            error.value = err.response?.data?.message || "Error obteniendo chats por ID de producto";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todos los chats donde el usuario es comprador.
     * @param {string} idComprador - ID del comprador.
     * @returns {Promise<void>}
     * @throws Error si ocurre un problema al buscar.
     */
    const getChatsByBuyerId = async (idComprador: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await chatService.getChatsByBuyerId(idComprador);
            chats.value = response.data;
        } catch (err: any) {
            console.error("Error obteniendo chats por ID de comprador:", err);
            error.value = err.response?.data?.message || "Error obteniendo chats por ID de comprador";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todos los chats donde el usuario es vendedor.
     * @param {string} idVendedor - ID del vendedor.
     * @returns {Promise<void>}
     * @throws Error si ocurre un problema al buscar.
     */
    const getChatsBySellerId = async (idVendedor: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await chatService.getChatsBySellerId(idVendedor);
            chats.value = response.data;
        } catch (err: any) {
            console.error("Error obteniendo chats por ID de vendedor:", err);
            error.value = err.response?.data?.message || "Error obteniendo chats por ID de vendedor";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene o crea un chat entre un comprador y un vendedor para un producto específico.
     * @param {string} idProducto - ID del producto.
     * @param {string} idComprador - ID del comprador.
     * @param {string} idVendedor - ID del vendedor.
     * @returns {Promise<any>} Chat existente o creado.
     * @throws Error si ocurre un problema al buscar o crear.
     */
    const getChatByParticipants = async (idProducto: string, idComprador: string, idVendedor: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await chatService.getChatByParticipants(idProducto, idComprador, idVendedor);
            currentChat.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error obteniendo/creando chat por participantes:", err);
            error.value = err.response?.data?.message || "Error obteniendo/creando chat por participantes";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Añade un nuevo mensaje a un chat existente.
     * @param {string} chatId - ID del chat.
     * @param {string} idEmisor - ID del usuario emisor.
     * @param {string} mensaje - Texto del mensaje.
     * @returns {Promise<any>} Chat actualizado.
     * @throws Error si ocurre un problema al añadir.
     */
    const addMessage = async (chatId: string, idEmisor: string, mensaje: string) => {
        try {
            loading.value = true;
            error.value = null;

            const mensajeEntity: MensajeEntity = {
                idEmisor,
                mensaje,
                leido: false
            };

            const response = await chatService.addMessage(chatId, mensajeEntity);
            currentChat.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error añadiendo mensaje al chat:", err);
            error.value = err.response?.data?.message || "Error añadiendo mensaje al chat";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Elimina un chat existente por su ID.
     * @param {string} chatId - ID del chat.
     * @returns {Promise<any>} Respuesta de la operación.
     * @throws Error si ocurre un problema al eliminar.
     */
    const deleteChat = async (chatId: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await chatService.deleteChat(chatId);
            success.value = "Chat eliminado exitosamente";

            // Remover el chat de la lista de chats si existe
            const index = chats.value.findIndex(chat => chat.id === chatId);
            if (index !== -1) {
                chats.value.splice(index, 1);
            }

            // Limpiar el chat actual si era el que se eliminó
            if (currentChat.value?.id === chatId) {
                currentChat.value = null;
            }

            return response.data;
        } catch (err: any) {
            console.error("Error eliminando chat:", err);
            error.value = err.response?.data?.message || "Error eliminando chat";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene el nombre de usuario por su ID.
     * @param {string} userId - ID del usuario.
     * @returns {Promise<string>} Nombre de usuario.
     */
    const getUserNameById = async (userId: string): Promise<string> => {
        try {
            if (!userId) return '';

            const response = await userService.getUserById(userId);
            const user = response.data;

            if (user && user.username) {
                return user.username;
            }
            return '';
        } catch (err) {
            console.error("Error obteniendo nombre de usuario:", err);
            return '';
        }
    };

    /**
     * Obtiene el nombre del interlocutor en un chat.
     * @param {string} chatId - ID del chat.
     * @param {string} [currentUserId] - ID del usuario actual (opcional).
     * @returns {Promise<string>} Nombre del interlocutor.
     */
    const getChatPartnerName = async (chatId: string, currentUserId?: string): Promise<string> => {
        try {
            // Obtener el chat
            const chat = await getChatById(chatId);
            if (!chat) return '';

            // Determinar quién es el interlocutor (si no se especifica currentUserId, asumir que es el comprador)
            const partnerId = currentUserId ?
                (chat.idComprador === currentUserId ? chat.idVendedor : chat.idComprador) :
                chat.idVendedor;

            // Obtener el nombre del interlocutor
            return await getUserNameById(partnerId);
        } catch (err) {
            console.error("Error obteniendo nombre del interlocutor del chat:", err);
            return '';
        }
    };

    return {
        currentChat,
        chats,
        error,
        success,
        loading,
        getChatById,
        getChatsByProductId,
        getChatsByBuyerId,
        getChatsBySellerId,
        getChatByParticipants,
        addMessage,
        deleteChat,
        getUserNameById,
        getChatPartnerName
    };
}