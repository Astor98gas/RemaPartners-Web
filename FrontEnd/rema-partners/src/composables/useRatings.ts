import { ref } from 'vue'
import { ratingService } from '@/services/rating.service'
import type { Rating, RatingFormData, RatingReplyData } from '@/models/rating'
import { useToast } from 'vue-toastification'

/**
 * Composable para gestionar valoraciones (ratings).
 * Permite obtener, crear, actualizar, eliminar valoraciones y añadir respuestas.
 */
export function useRatings() {
    const ratings = ref<Rating[]>([])
    const userRating = ref<Rating | null>(null)
    const error = ref<string | null>(null)
    const success = ref<string | null>(null)
    const loading = ref<boolean>(false)
    const toast = useToast()

    /**
     * Carga valoraciones de un vendedor.
     * @param {string} sellerId - ID del vendedor.
     * @returns {Promise<any[]>} Lista de valoraciones.
     * @throws Error si ocurre un problema al cargar.
     */
    const getSellerRatings = async (sellerId: string) => {
        try {
            loading.value = true
            error.value = null
            const response = await ratingService.getRatingsBySeller(sellerId)
            ratings.value = response.data
            return response.data
        } catch (err: any) {
            console.error('Error obteniendo valoraciones del vendedor:', err)
            error.value = err.response?.data?.message || 'Error al cargar las valoraciones'
            throw err
        } finally {
            loading.value = false
        }
    }

    /**
     * Crea una nueva valoración.
     * @param {RatingFormData} ratingData - Datos de la valoración.
     * @returns {Promise<any>} Valoración creada.
     * @throws Error si ocurre un problema al crear.
     */
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
            console.error('Error creando valoración:', err)
            error.value = err.response?.data?.message || 'Error al crear la valoración'
            throw err
        } finally {
            loading.value = false
        }
    }

    /**
     * Actualiza una valoración existente.
     * @param {string} ratingId - ID de la valoración.
     * @param {RatingFormData} ratingData - Datos a actualizar.
     * @returns {Promise<any>} Valoración actualizada.
     * @throws Error si ocurre un problema al actualizar.
     */
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
            console.error('Error actualizando valoración:', err)
            error.value = err.response?.data?.message || 'Error al actualizar la valoración'
            throw err
        } finally {
            loading.value = false
        }
    }

    /**
     * Obtiene la valoración de un usuario para un vendedor.
     * @param {string} userId - ID del usuario.
     * @param {string} sellerId - ID del vendedor.
     * @returns {Promise<any|null>} Valoración encontrada o null.
     * @throws Error si ocurre un problema al buscar.
     */
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

            console.error('Error obteniendo valoración del usuario:', err)
            error.value = err.response?.data?.message || 'Error al cargar la valoración'
            throw err
        } finally {
            loading.value = false
        }
    }

    /**
     * Añade o actualiza respuesta a una valoración.
     * @param {RatingReplyData} replyData - Datos de la respuesta.
     * @returns {Promise<any>} Respuesta añadida.
     * @throws Error si ocurre un problema al añadir.
     */
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
            console.error('Error añadiendo respuesta:', err)
            error.value = err.response?.data?.message || 'Error al añadir la respuesta'
            throw err
        } finally {
            loading.value = false
        }
    }

    /**
     * Elimina una valoración.
     * @param {string} ratingId - ID de la valoración.
     * @param {string} sellerId - ID del vendedor.
     * @returns {Promise<void>}
     * @throws Error si ocurre un problema al eliminar.
     */
    const deleteRating = async (ratingId: string, sellerId: string) => {
        try {
            loading.value = true
            error.value = null
            await ratingService.deleteRating(ratingId)
            success.value = 'Valoración eliminada correctamente'
            // Actualizar la lista de valoraciones
            await getSellerRatings(sellerId)
        } catch (err: any) {
            console.error('Error eliminando valoración:', err)
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
