import axios from "axios";
import type { ChatEntity, MensajeEntity } from "@/models/chat";

const API_BASE_URL = "http://localhost:8080";
const CHAT_API_URL = `${API_BASE_URL}/api/chat`;

export const chatService = {
    // Get chat by ID
    async getChatById(id: string) {
        return axios.get(`${CHAT_API_URL}/getById/${id}`);
    },

    // Get chats by product ID
    async getChatsByProductId(idProducto: string) {
        return axios.get(`${CHAT_API_URL}/getByProductId/${idProducto}`);
    },

    // Get chats where user is buyer
    async getChatsByBuyerId(idComprador: string) {
        return axios.get(`${CHAT_API_URL}/getByBuyerId/${idComprador}`);
    },

    // Get chats where user is seller
    async getChatsBySellerId(idVendedor: string) {
        return axios.get(`${CHAT_API_URL}/getBySellerId/${idVendedor}`);
    },

    // Get chat by product, buyer and seller IDs or create if doesn't exist
    async getChatByParticipants(idProducto: string, idComprador: string, idVendedor: string) {
        try {
            // First, try to find an existing chat
            const response = await axios.get(
                `${CHAT_API_URL}/getByParticipants/${idProducto}/${idComprador}/${idVendedor}`
            );
            return response;
        } catch (error) {
            // If chat doesn't exist, create a new one
            const newChat: ChatEntity = {
                idProducto,
                idComprador,
                idVendedor,
                mensajes: [],
                fechaCreacion: new Date().toISOString(),
                ultimaActualizacion: new Date().toISOString(),
                activo: true
            };
            return axios.post(`${CHAT_API_URL}/create`, newChat);
        }
    },

    // Add message to existing chat
    async addMessage(chatId: string, mensaje: MensajeEntity) {
        return axios.post(`${CHAT_API_URL}/addMessage/${chatId}`, mensaje);
    }
};