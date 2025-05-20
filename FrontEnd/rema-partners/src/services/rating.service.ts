import axios from 'axios'
import type { Rating, RatingFormData, RatingReplyData } from '@/models/rating'

const API_BASE_URL = 'http://localhost:8080'

export const ratingService = {
    // Obtener todas las valoraciones de un vendedor
    async getRatingsBySeller(sellerId: string) {
        return axios.get(`${API_BASE_URL}/ratings/seller/${sellerId}`)
    },

    // Crear una nueva valoración
    async createRating(ratingData: RatingFormData) {
        return axios.post(`${API_BASE_URL}/ratings/create`, ratingData)
    },

    // Actualizar una valoración existente
    async updateRating(ratingId: string, ratingData: RatingFormData) {
        return axios.put(`${API_BASE_URL}/ratings/${ratingId}`, ratingData)
    },

    // Obtener la valoración que un usuario hizo a un vendedor específico
    async getUserRatingForSeller(userId: string, sellerId: string) {
        return axios.get(`${API_BASE_URL}/ratings/user/${userId}/seller/${sellerId}`)
    },

    // Añadir o actualizar la respuesta del vendedor a una valoración
    async addReplyToRating(replyData: RatingReplyData) {
        return axios.post(`${API_BASE_URL}/ratings/reply`, replyData)
    },

    // Eliminar una valoración
    async deleteRating(ratingId: string) {
        return axios.delete(`${API_BASE_URL}/ratings/${ratingId}`)
    }
}
