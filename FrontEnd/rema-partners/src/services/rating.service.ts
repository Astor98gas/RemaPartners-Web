import axios from 'axios'
import type { Rating, RatingFormData, RatingReplyData } from '@/models/rating'

const API_BASE_URL = 'http://localhost:8080'

export const ratingService = {
    /**
     * Obtiene todas las valoraciones de un vendedor.
     * @param {string} sellerId - ID del vendedor.
     * @returns {Promise} Promesa con la lista de valoraciones.
     */
    async getRatingsBySeller(sellerId: string) {
        return axios.get(`${API_BASE_URL}/ratings/seller/${sellerId}`)
    },

    /**
     * Crea una nueva valoración.
     * @param {RatingFormData} ratingData - Datos de la valoración.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async createRating(ratingData: RatingFormData) {
        return axios.post(`${API_BASE_URL}/ratings/create`, ratingData)
    },

    /**
     * Actualiza una valoración existente.
     * @param {string} ratingId - ID de la valoración.
     * @param {RatingFormData} ratingData - Datos actualizados de la valoración.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async updateRating(ratingId: string, ratingData: RatingFormData) {
        return axios.put(`${API_BASE_URL}/ratings/${ratingId}`, ratingData)
    },

    /**
     * Obtiene la valoración que un usuario hizo a un vendedor específico.
     * @param {string} userId - ID del usuario.
     * @param {string} sellerId - ID del vendedor.
     * @returns {Promise} Promesa con la valoración encontrada.
     */
    async getUserRatingForSeller(userId: string, sellerId: string) {
        return axios.get(`${API_BASE_URL}/ratings/user/${userId}/seller/${sellerId}`)
    },

    /**
     * Añade o actualiza la respuesta del vendedor a una valoración.
     * @param {RatingReplyData} replyData - Datos de la respuesta.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async addReplyToRating(replyData: RatingReplyData) {
        return axios.post(`${API_BASE_URL}/ratings/reply`, replyData)
    },

    /**
     * Elimina una valoración.
     * @param {string} ratingId - ID de la valoración.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async deleteRating(ratingId: string) {
        return axios.delete(`${API_BASE_URL}/ratings/${ratingId}`)
    }
}
