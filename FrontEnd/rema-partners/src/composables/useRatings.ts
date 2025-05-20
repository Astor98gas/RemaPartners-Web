import { ref } from 'vue'
import { ratingService } from '@/services/rating.service'
import type { Rating, RatingFormData, RatingReplyData } from '@/models/rating'
import { useToast } from 'vue-toastification'

export function useRatings() {
    const ratings = ref<Rating[]>([])
    const userRating = ref<Rating | null>(null)
    const error = ref<string | null>(null)
    const success = ref<string | null>(null)
    const loading = ref<boolean>(false)
    const toast = useToast()

    // Cargar valoraciones de un vendedor
    const getSellerRatings = async (sellerId: string) => {
        try {
            loading.value = true
            error.value = null
            const response = await ratingService.getRatingsBySeller(sellerId)
            ratings.value = response.data
            return response.data
        } catch (err: any) {
            console.error('Error fetching seller ratings:', err)
            error.value = err.response?.data?.message || 'Error al cargar las valoraciones'
            throw err
        } finally {
            loading.value = false
        }
    }

    // Crear una nueva valoración
    const createRating = async (ratingData: RatingFormData) => {
        try {
            loading.value = true
            error.value = null
            const response = await ratingService.createRating(ratingData)
            success.value = 'Valoración enviada correctamente'
            // Actualizar la lista de valoraciones
            await getSellerRatings(ratingData.sellerId)
            return response.data
        } catch (err: any) {
            console.error('Error creating rating:', err)
            error.value = err.response?.data?.message || 'Error al crear la valoración'
            throw err
        } finally {
            loading.value = false
        }
    }

    // Actualizar una valoración existente
    const updateRating = async (ratingId: string, ratingData: RatingFormData) => {
        try {
            loading.value = true
            error.value = null
            const response = await ratingService.updateRating(ratingId, ratingData)
            success.value = 'Valoración actualizada correctamente'
            // Actualizar la lista de valoraciones
            await getSellerRatings(ratingData.sellerId)
            return response.data
        } catch (err: any) {
            console.error('Error updating rating:', err)
            error.value = err.response?.data?.message || 'Error al actualizar la valoración'
            throw err
        } finally {
            loading.value = false
        }
    }

    // Obtener la valoración de un usuario para un vendedor
    const getUserRatingForSeller = async (userId: string, sellerId: string) => {
        try {
            loading.value = true
            error.value = null
            const response = await ratingService.getUserRatingForSeller(userId, sellerId)
            userRating.value = response.data
            return response.data
        } catch (err: any) {
            // Si es 404, significa que no hay valoración, lo cual no es un error
            if (err.response?.status === 404) {
                userRating.value = null
                return null
            }

            console.error('Error fetching user rating:', err)
            error.value = err.response?.data?.message || 'Error al cargar la valoración'
            throw err
        } finally {
            loading.value = false
        }
    }

    // Añadir o actualizar respuesta a una valoración
    const addReplyToRating = async (replyData: RatingReplyData) => {
        try {
            loading.value = true
            error.value = null
            const response = await ratingService.addReplyToRating(replyData)
            success.value = 'Respuesta añadida correctamente'

            // Actualizar la lista de valoraciones (necesitamos el sellerId)
            // Esto requeriría conocer el sellerId, así que podríamos hacer:
            // await getSellerRatings(sellerId) si tenemos acceso al sellerId
            // O podemos simplemente actualizar el rating específico en la lista de ratings

            return response.data
        } catch (err: any) {
            console.error('Error adding reply:', err)
            error.value = err.response?.data?.message || 'Error al añadir la respuesta'
            throw err
        } finally {
            loading.value = false
        }
    }

    // Eliminar una valoración
    const deleteRating = async (ratingId: string, sellerId: string) => {
        try {
            loading.value = true
            error.value = null
            await ratingService.deleteRating(ratingId)
            success.value = 'Valoración eliminada correctamente'
            // Actualizar la lista de valoraciones
            await getSellerRatings(sellerId)
        } catch (err: any) {
            console.error('Error deleting rating:', err)
            error.value = err.response?.data?.message || 'Error al eliminar la valoración'
            throw err
        } finally {
            loading.value = false
        }
    }

    return {
        ratings,
        userRating,
        error,
        success,
        loading,
        getSellerRatings,
        createRating,
        updateRating,
        getUserRatingForSeller,
        addReplyToRating,
        deleteRating
    }
}
