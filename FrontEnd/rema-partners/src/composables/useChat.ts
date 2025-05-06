import { ref } from "vue";
import { chatService } from "@/services/chat.service";
import { userService } from "@/services/user.service";
import type { ChatEntity, MensajeEntity } from "@/models/chat";
import type { User } from "@/models/user";

export function useChat() {
    const currentChat = ref<ChatEntity | null>(null);
    const chats = ref<ChatEntity[]>([]);
    const error = ref<string | null>(null);
    const success = ref<string | null>(null);
    const loading = ref<boolean>(false);

    /**
     * Obtiene un chat por su ID único.
     * 
     * @param id - El identificador único del chat a obtener
     * @returns El objeto chat completo con sus mensajes y datos de participantes
     * @throws Error si no se puede cargar el chat
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
            console.error("Error fetching chat by ID:", err);
            error.value = err.response?.data?.message || "Error fetching chat by ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todos los chats asociados a un producto específico.
     * 
     * @param idProducto - El ID del producto del cual se quieren obtener los chats
     * @throws Error si hay problemas para cargar los chats
     */
    const getChatsByProductId = async (idProducto: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await chatService.getChatsByProductId(idProducto);
            chats.value = response.data;
        } catch (err: any) {
            console.error("Error fetching chats by product ID:", err);
            error.value = err.response?.data?.message || "Error fetching chats by product ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todos los chats donde el usuario especificado es el comprador.
     * 
     * @param idComprador - El ID del usuario comprador
     * @throws Error si hay problemas para cargar los chats
     */
    const getChatsByBuyerId = async (idComprador: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await chatService.getChatsByBuyerId(idComprador);
            chats.value = response.data;
        } catch (err: any) {
            console.error("Error fetching chats by buyer ID:", err);
            error.value = err.response?.data?.message || "Error fetching chats by buyer ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene todos los chats donde el usuario especificado es el vendedor.
     * 
     * @param idVendedor - El ID del usuario vendedor
     * @throws Error si hay problemas para cargar los chats
     */
    const getChatsBySellerId = async (idVendedor: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await chatService.getChatsBySellerId(idVendedor);
            chats.value = response.data;
        } catch (err: any) {
            console.error("Error fetching chats by seller ID:", err);
            error.value = err.response?.data?.message || "Error fetching chats by seller ID";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene o crea un chat entre un comprador y un vendedor para un producto específico.
     * Si ya existe un chat con estos participantes, lo devuelve; si no, crea uno nuevo.
     * 
     * @param idProducto - El ID del producto sobre el que se está conversando
     * @param idComprador - El ID del usuario comprador
     * @param idVendedor - El ID del usuario vendedor
     * @returns El objeto chat existente o el nuevo chat creado
     * @throws Error si hay problemas al obtener o crear el chat
     */
    const getChatByParticipants = async (idProducto: string, idComprador: string, idVendedor: string) => {
        try {
            loading.value = true;
            error.value = null;
            const response = await chatService.getChatByParticipants(idProducto, idComprador, idVendedor);
            currentChat.value = response.data;
            return response.data;
        } catch (err: any) {
            console.error("Error fetching/creating chat by participants:", err);
            error.value = err.response?.data?.message || "Error fetching/creating chat by participants";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Añade un nuevo mensaje a un chat existente.
     * 
     * @param chatId - El ID del chat donde se añadirá el mensaje
     * @param idEmisor - El ID del usuario que envía el mensaje
     * @param mensaje - El texto del mensaje a enviar
     * @returns El chat actualizado con el nuevo mensaje
     * @throws Error si hay problemas al añadir el mensaje
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
            console.error("Error adding message to chat:", err);
            error.value = err.response?.data?.message || "Error adding message to chat";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

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
            console.error("Error fetching user name:", err);
            return '';
        }
    };

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
            console.error("Error fetching chat partner name:", err);
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
        getUserNameById,
        getChatPartnerName
    };
}