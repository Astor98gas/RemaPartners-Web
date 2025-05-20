<template>
    <div class="ratings-section">
        <div class="ratings-header">
            <h2 class="section-title">Valoraciones</h2>
            <div class="ratings-summary" v-if="!loading && ratings.length > 0">
                <div class="average-rating">
                    <StarRating :modelValue="averageRating" :showRatingText="true" />
                    <span class="total-count">({{ ratings.length }} valoraciones)</span>
                </div>
            </div>
        </div>

        <!-- Loading state -->
        <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>Cargando valoraciones...</p>
        </div>

        <!-- Error state -->
        <div v-else-if="error" class="error-container">
            <p class="error-message">{{ error }}</p>
            <button @click="loadRatings" class="retry-btn">Reintentar</button>
        </div>

        <!-- No ratings yet -->
        <div v-else-if="ratings.length === 0" class="empty-state">
            <p class="empty-message">Este vendedor aún no tiene valoraciones.</p>
            <button v-if="canAddRating" @click="showRatingForm = true" class="add-rating-btn">
                Sé el primero en valorar
            </button>
        </div>

        <!-- Rating form for adding/editing -->
        <div v-if="showRatingForm" class="rating-form-wrapper">
            <RatingForm :sellerId="sellerId" :existingRating="userRating" :isEditing="userRating !== null"
                @submit="handleRatingSubmit" @cancel="showRatingForm = false" />
        </div>

        <!-- Rating reply form -->
        <div v-if="showReplyForm" class="rating-form-wrapper">
            <RatingForm :sellerId="sellerId" :existingRating="selectedRating" :isReplying="true"
                @submit="handleReplySubmit" @cancel="showReplyForm = false" />
        </div>

        <!-- Add rating button -->
        <div v-if="canAddRating && !showRatingForm && ratings.length > 0" class="add-rating-container">
            <button v-if="!userRating" @click="showRatingForm = true" class="add-rating-btn">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd"
                        d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z"
                        clip-rule="evenodd" />
                </svg>
                <span>Añadir valoración</span>
            </button>
            <button v-else @click="showRatingForm = true" class="edit-rating-btn">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path
                        d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                </svg>
                <span>Editar mi valoración</span>
            </button>
        </div>

        <!-- Ratings list -->
        <div v-if="!loading && ratings.length > 0" class="ratings-list">
            <TransitionGroup name="rating-item">
                <RatingItem v-for="rating in sortedRatings" :key="rating.id" :rating="rating"
                    :canEdit="canEditRating(rating)" :canReply="canReplyToRating(rating)"
                    :highlight="rating.id === userRating?.id" @edit="editRating(rating)"
                    @reply="replyToRating(rating)" />
            </TransitionGroup>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, watch } from 'vue';
import type { Rating, RatingFormData, RatingReplyData } from '@/models/rating';
import { useRatings } from '@/composables/useRatings';
import StarRating from './StarRating.vue';
import RatingItem from './RatingItem.vue';
import RatingForm from './RatingForm.vue';
import { useToast } from 'vue-toastification';

export default defineComponent({
    name: 'RatingsSection',
    components: {
        StarRating,
        RatingItem,
        RatingForm
    },
    props: {
        sellerId: {
            type: String,
            required: true
        },
        currentUserId: {
            type: String,
            default: null
        },
        isCurrentUserSeller: {
            type: Boolean,
            default: false
        }
    },
    setup(props) {
        const ratingsComposable = useRatings();
        const toast = useToast();

        // State
        const ratings = ref<Rating[]>([]);
        const userRating = ref<Rating | null>(null);
        const loading = ref(true);
        const error = ref<string | null>(null);
        const showRatingForm = ref(false);
        const showReplyForm = ref(false);
        const selectedRating = ref<Rating | null>(null);

        // Computed
        const averageRating = computed(() => {
            if (ratings.value.length === 0) return 0;

            const sum = ratings.value.reduce((acc, rating) => acc + rating.rating, 0);
            return Math.round((sum / ratings.value.length) * 10) / 10; // 1 decimal place
        });

        const sortedRatings = computed(() => {
            // Sort by date, most recent first
            return [...ratings.value].sort((a, b) => {
                return new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime();
            });
        });

        const canAddRating = computed(() => {
            // Un usuario puede añadir una valoración si:
            // 1. Está autenticado (currentUserId existe)
            // 2. No es el propio vendedor
            // 3. No tiene ya una valoración (si userRating existe, puede editarla pero no crear otra)
            return !!props.currentUserId && props.currentUserId !== props.sellerId;
        });

        // Methods
        const loadRatings = async () => {
            loading.value = true;
            error.value = null;

            try {
                // Carga las valoraciones del vendedor
                const data = await ratingsComposable.getSellerRatings(props.sellerId);
                ratings.value = data || [];

                // Si el usuario está autenticado, busca si ya tiene una valoración
                if (props.currentUserId) {
                    await loadUserRating();
                }
            } catch (err: any) {
                console.error('Error loading ratings:', err);
                error.value = 'No se pudieron cargar las valoraciones. Por favor, inténtalo de nuevo más tarde.';
            } finally {
                loading.value = false;
            }
        };

        const loadUserRating = async () => {
            if (!props.currentUserId) return;

            try {
                const data = await ratingsComposable.getUserRatingForSeller(
                    props.currentUserId,
                    props.sellerId
                );
                userRating.value = data;
            } catch (err) {
                // Si el error es 404, es que no hay valoración del usuario, lo cual no es un error
                console.log('No user rating found or error fetching it');
                userRating.value = null;
            }
        };

        const handleRatingSubmit = async (formData: RatingFormData) => {
            try {
                if (userRating.value) {
                    // Actualizar valoración existente
                    await ratingsComposable.updateRating(userRating.value.id, formData);
                    toast.success('Tu valoración ha sido actualizada correctamente');
                } else {
                    // Crear nueva valoración
                    await ratingsComposable.createRating(formData);
                    toast.success('Tu valoración ha sido publicada correctamente');
                }

                // Recargar valoraciones y cerrar formulario
                await loadRatings();
                showRatingForm.value = false;
            } catch (err: any) {
                toast.error(err.message || 'Ha ocurrido un error al enviar tu valoración');
            }
        };

        const handleReplySubmit = async (replyData: RatingReplyData) => {
            try {
                await ratingsComposable.addReplyToRating(replyData);
                toast.success('Tu respuesta ha sido publicada correctamente');

                // Recargar valoraciones y cerrar formulario
                await loadRatings();
                showReplyForm.value = false;
                selectedRating.value = null;
            } catch (err: any) {
                toast.error(err.message || 'Ha ocurrido un error al enviar tu respuesta');
            }
        };

        const editRating = (rating: Rating) => {
            selectedRating.value = rating;
            showReplyForm.value = false;
            showRatingForm.value = true;
        };

        const replyToRating = (rating: Rating) => {
            selectedRating.value = rating;
            showRatingForm.value = false;
            showReplyForm.value = true;
        };

        const canEditRating = (rating: Rating): boolean => {
            // Un usuario puede editar una valoración si es el autor
            return props.currentUserId === rating.userId;
        };

        const canReplyToRating = (rating: Rating): boolean => {
            // Un vendedor puede responder a una valoración si:
            // 1. Es el vendedor del perfil
            // 2. Es su perfil (sellerId == currentUserId)
            // 3. La valoración no tiene respuesta aún
            return props.isCurrentUserSeller &&
                props.currentUserId === props.sellerId &&
                !rating.reply &&
                rating.userId !== props.currentUserId;
        };

        // Watch for changes in sellerId to reload ratings
        watch(() => props.sellerId, () => {
            loadRatings();
        });

        // Initial load
        onMounted(() => {
            loadRatings();
        });

        return {
            ratings,
            userRating,
            loading,
            error,
            averageRating,
            sortedRatings,
            canAddRating,
            showRatingForm,
            showReplyForm,
            selectedRating,
            loadRatings,
            handleRatingSubmit,
            handleReplySubmit,
            editRating,
            replyToRating,
            canEditRating,
            canReplyToRating
        };
    }
});
</script>

<style scoped>
.ratings-section {
    margin-bottom: 2rem;
}

.ratings-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
}

.section-title {
    font-size: 1.5rem;
    font-weight: 600;
    color: #1f2937;
    margin: 0;
}

.ratings-summary {
    display: flex;
    align-items: center;
}

.average-rating {
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.total-count {
    font-size: 0.875rem;
    color: #6b7280;
}

.loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 2rem 0;
}

.loading-spinner {
    width: 2rem;
    height: 2rem;
    border-radius: 50%;
    border: 3px solid rgba(59, 130, 246, 0.3);
    border-top-color: #3b82f6;
    animation: spin 1s linear infinite;
    margin-bottom: 1rem;
}

@keyframes spin {
    to {
        transform: rotate(360deg);
    }
}

.error-container {
    background-color: #fee2e2;
    border: 1px solid #fecaca;
    border-radius: 0.5rem;
    padding: 1rem;
    text-align: center;
}

.error-message {
    color: #b91c1c;
    margin-bottom: 0.75rem;
}

.retry-btn {
    padding: 0.375rem 0.75rem;
    font-size: 0.875rem;
    color: white;
    background-color: #ef4444;
    border-radius: 0.25rem;
    border: none;
    cursor: pointer;
    transition: background-color 0.2s ease;
}

.retry-btn:hover {
    background-color: #dc2626;
}

.empty-state {
    text-align: center;
    padding: 2.5rem 0;
    border: 1px dashed #d1d5db;
    border-radius: 0.5rem;
    background-color: #f9fafb;
}

.empty-message {
    color: #6b7280;
    margin-bottom: 1rem;
}

.rating-form-wrapper {
    margin-bottom: 1.5rem;
}

.add-rating-container {
    margin-bottom: 1.5rem;
    display: flex;
    justify-content: flex-end;
}

.add-rating-btn,
.edit-rating-btn {
    display: inline-flex;
    align-items: center;
    gap: 0.375rem;
    padding: 0.5rem 1rem;
    border-radius: 0.375rem;
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s ease;
    border: none;
}

.add-rating-btn {
    background-color: #3b82f6;
    color: white;
}

.add-rating-btn:hover {
    background-color: #2563eb;
}

.edit-rating-btn {
    background-color: #f3f4f6;
    color: #4b5563;
    border: 1px solid #e5e7eb;
}

.edit-rating-btn:hover {
    background-color: #e5e7eb;
}

.ratings-list {
    margin-top: 1.5rem;
}

/* Transitions */
.rating-item-enter-active,
.rating-item-leave-active {
    transition: all 0.3s ease;
}

.rating-item-enter-from,
.rating-item-leave-to {
    opacity: 0;
    transform: translateY(20px);
}
</style>
