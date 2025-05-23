import axios from "axios";
import type { ChatEntity, MensajeEntity } from "@/models/chat";

const API_BASE_URL = "http://localhost:8080";
const CHAT_API_URL = `${API_BASE_URL}/api/chat`;

export const chatService = {
    /**
     * Obtiene un chat por su ID.
     * @param {string} id - ID del chat.
     * @returns {Promise} Promesa con los datos del chat.
     */
    async getChatById(id: string) {
        return axios.get(`${CHAT_API_URL}/getById/${id}`);
    },

    /**
     * Obtiene los chats asociados a un producto.
     * @param {string} idProducto - ID del producto.
     * @returns {Promise} Promesa con la lista de chats.
     */
    async getChatsByProductId(idProducto: string) {
        return axios.get(`${CHAT_API_URL}/getByProductId/${idProducto}`);
    },

    /**
     * Obtiene los chats donde el usuario es comprador.
     * @param {string} idComprador - ID del comprador.
     * @returns {Promise} Promesa con la lista de chats.
     */
    async getChatsByBuyerId(idComprador: string) {
        return axios.get(`${CHAT_API_URL}/getByBuyerId/${idComprador}`);
    },

    /**
     * Obtiene los chats donde el usuario es vendedor.
     * @param {string} idVendedor - ID del vendedor.
     * @returns {Promise} Promesa con la lista de chats.
     */
    async getChatsBySellerId(idVendedor: string) {
        return axios.get(`${CHAT_API_URL}/getBySellerId/${idVendedor}`);
    },

    /**
     * Obtiene un chat por los participantes o lo crea si no existe.
     * @param {string} idProducto - ID del producto.
     * @param {string} idComprador - ID del comprador.
     * @param {string} idVendedor - ID del vendedor.
     * @returns {Promise} Promesa con los datos del chat.
     */
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

    /**
     * Añade un mensaje a un chat existente.
     * @param {string} chatId - ID del chat.
     * @param {MensajeEntity} mensaje - Mensaje a añadir.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async addMessage(chatId: string, mensaje: MensajeEntity) {
        return axios.post(`${CHAT_API_URL}/addMessage/${chatId}`, mensaje);
    },

    /**
     * Elimina un chat por su ID.
     * @param {string} chatId - ID del chat.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async deleteChat(chatId: string) {
        return axios.delete(`${CHAT_API_URL}/delete/${chatId}`);
    }
};