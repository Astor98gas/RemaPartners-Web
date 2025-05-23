<template>
    <div class="mb-8">
        <div class="flex justify-between items-center mb-6  border-b-1 border-gray-200 pb-2">
            <h2 class="text-2xl font-semibold text-gray-800">Valoraciones</h2>
            <div v-if="!loading && ratings.length > 0" class="flex items-center">
                <div class="flex items-center gap-2">
                    <StarRating :modelValue="averageRating" :showRatingText="true" />
                    <span class="text-sm text-gray-500">({{ ratings.length }} valoraciones)</span>
                </div>
            </div>
        </div>

        <!-- Loading state -->
        <div v-if="loading" class="flex flex-col items-center justify-center py-8">
            <div class="w-8 h-8 rounded-full border-3 border-blue-300 border-t-blue-500 animate-spin mb-4"></div>
            <p>Cargando valoraciones...</p>
        </div>

        <!-- Error state -->
        <div v-else-if="error" class="bg-red-100 border border-red-200 rounded-lg p-4 text-center">
            <p class="text-red-700 mb-3">{{ error }}</p>
            <button @click="loadRatings"
                class="py-1.5 px-3 text-sm text-white bg-red-500 hover:bg-red-600 rounded transition duration-200 border-none cursor-pointer">
                Reintentar
            </button>
        </div>

        <!-- No ratings yet -->
        <div v-else-if="ratings.length === 0"
            class="text-center py-10 border border-dashed border-gray-300 rounded-lg bg-gray-50">
            <p class="text-gray-500 mb-4">Este vendedor aún no tiene valoraciones.</p>
            <button v-if="canAddRating" @click="showRatingForm = true"
                class="py-2 px-4 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition duration-200">
                Sé el primero en valorar
            </button>
        </div>

        <!-- Rating form for adding/editing -->
        <div v-if="showRatingForm" class="mb-6">
            <RatingForm :sellerId="sellerId" :existingRating="userRating" :isEditing="userRating !== null"
                @submit="handleRatingSubmit" @cancel="showRatingForm = false" />
        </div>

        <!-- Rating reply form -->
        <div v-if="showReplyForm" class="mb-6">
            <RatingForm :sellerId="sellerId" :existingRating="selectedRating" :isReplying="true"
                @submit="handleReplySubmit" @cancel="showReplyForm = false" />
        </div>

        <!-- Add rating button -->
        <div v-if="canAddRating && !showRatingForm && ratings.length > 0" class="mb-6 flex justify-end">
            <button v-if="!userRating" @click="showRatingForm = true"
                class="inline-flex items-center gap-1.5 py-2 px-4 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition duration-200 border-none">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd"
                        d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z"
                        clip-rule="evenodd" />
                </svg>
                <span>Añadir valoración</span>
            </button>
            <button v-else @click="showRatingForm = true"
                class="inline-flex items-center gap-1.5 py-2 px-4 bg-gray-100 text-gray-600 rounded-md hover:bg-gray-200 transition duration-200 border border-gray-200">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path
                        d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                </svg>
                <span>Editar mi valoración</span>
            </button>
        </div>

        <!-- Ratings list -->
        <div v-if="!loading && ratings.length > 0" class="mt-6">
            <TransitionGroup name="rating-item">
                <RatingItem v-for="rating in sortedRatings" :key="rating.id" :rating="rating"
                    :canEdit="canEditRating(rating)" :canReply="canReplyToRating(rating)"
                    :canDelete="canDeleteRating(rating)" :highlight="!!userRating && userRating.id === rating.id"
                    @edit="editRating(rating)" @reply="replyToRating(rating)" @delete="confirmDeleteRating(rating)" />
            </TransitionGroup>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, watch } from 'vue';
import type { Rating, RatingFormData, RatingReplyData } from '@/models/rating';
import { useRatings } from '@/composables/useRatings';
import { useUsers } from '@/composables/useUsers';
import StarRating from './StarRating.vue';
import RatingItem from './RatingItem.vue';
import RatingForm from './RatingForm.vue';
import { useToast } from 'vue-toastification';
import Swal from 'sweetalert2';

/**
 * Componente para mostrar y gestionar la sección de valoraciones de un vendedor.
 * Permite ver, añadir, editar, responder y eliminar valoraciones.
 * 
 * @component
 * @example
 * <RatingsSection :sellerId="sellerId" :currentUserId="userId" :isCurrentUserSeller="true" />
 */
export default defineComponent({
    name: 'RatingsSection',
    components: {
        StarRating,
        RatingItem,
        RatingForm
    },
    props: {
        /**
         * ID del vendedor cuyas valoraciones se muestran.
         * @type {string}
         * @required
         */
        sellerId: {
            type: String,
            required: true
        },
        /**
         * ID del usuario autenticado actual.
         * @type {string}
         * @default null
         */
        currentUserId: {
            type: String,
            default: null
        },
        /**
         * Indica si el usuario actual es el vendedor del perfil.
         * @type {boolean}
         * @default false
         */
        isCurrentUserSeller: {
            type: Boolean,
            default: false
        }
    },
    setup(props) {
        const ratingsComposable = useRatings();
        const usersComposable = useUsers();
        const toast = useToast();

        // State
        const ratings = ref<Rating[]>([]);
        const userRating = ref<Rating | null>(null);
        const loading = ref(true);
        const error = ref<string | null>(null);
        const showRatingForm = ref(false);
        const showReplyForm = ref(false);
        const selectedRating = ref<Rating | null>(null);
        const userData = ref<any>(null); // Para almacenar información del usuario actual incluyendo rol

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
        /**
         * Carga todas las valoraciones del vendedor.
         */
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

        /**
         * Carga la valoración del usuario actual para este vendedor, si existe.
         */
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

        /**
         * Carga los datos del usuario autenticado (incluyendo rol).
         */
        const loadUserData = async () => {
            if (props.currentUserId) {
                try {
                    const data = await usersComposable.isLoggedIn();
                    userData.value = data;
                } catch (error) {
                    console.error('Error cargando información del usuario:', error);
                }
            }
        };

        /**
         * Maneja el envío del formulario de valoración (crear o editar).
         * @param {RatingFormData} formData - Datos del formulario de valoración.
         */
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

        /**
         * Maneja el envío del formulario de respuesta a una valoración.
         * @param {RatingReplyData} replyData - Datos de la respuesta.
         */
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

        /**
         * Permite editar una valoración seleccionada.
         * @param {Rating} rating - Valoración a editar.
         */
        const editRating = (rating: Rating) => {
            selectedRating.value = rating;
            showReplyForm.value = false;
            showRatingForm.value = true;
        };

        /**
         * Permite responder a una valoración seleccionada.
         * @param {Rating} rating - Valoración a responder.
         */
        const replyToRating = (rating: Rating) => {
            selectedRating.value = rating;
            showRatingForm.value = false;
            showReplyForm.value = true;
        };

        /**
         * Determina si el usuario puede editar una valoración.
         * @param {Rating} rating
         * @returns {boolean}
         */
        const canEditRating = (rating: Rating): boolean => {
            // Un usuario puede editar una valoración si es el autor
            return props.currentUserId === rating.userId;
        };

        /**
         * Determina si el usuario puede responder a una valoración.
         * @param {Rating} rating
         * @returns {boolean}
         */
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

        /**
         * Determina si el usuario puede eliminar una valoración.
         * @param {Rating} rating
         * @returns {boolean}
         */
        const canDeleteRating = (rating: Rating): boolean => {
            // Un usuario puede eliminar una valoración si:
            // 1. Es administrador (ADMIN)
            // 2. Es trabajador (TRABAJADOR)
            // 3. Es el autor de la valoración
            return !!props.currentUserId && (
                userData.value?.rol?.name === 'ADMIN' ||
                userData.value?.rol?.name === 'TRABAJADOR' ||
                props.currentUserId === rating.userId
            );
        };

        /**
         * Muestra un diálogo de confirmación y elimina la valoración si se confirma.
         * @param {Rating} rating
         */
        const confirmDeleteRating = async (rating: Rating) => {
            // Determinar si el usuario actual es el propietario de la valoración
            const isOwnRating = props.currentUserId === rating.userId;

            // Crear un mensaje de confirmación más específico según el contexto
            let title = '';
            let text = '';
            let confirmButtonText = 'Eliminar';

            if (isOwnRating) {
                title = 'Eliminar valoración';
                text = '¿Estás seguro de que deseas eliminar tu valoración? Esta acción no se puede deshacer.';
            } else {
                title = 'Eliminar valoración de otro usuario';
                text = `¿Estás seguro de que deseas eliminar esta valoración de ${rating.username}? Esta acción no se puede deshacer y se realiza con tu autoridad de administrador.`;
            }

            // Mostrar SweetAlert2 para confirmar
            const result = await Swal.fire({
                title: title,
                text: text,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#dc2626',
                cancelButtonColor: '#6b7280',
                confirmButtonText: confirmButtonText,
                cancelButtonText: 'Cancelar'
            });

            if (result.isConfirmed) {
                try {
                    await ratingsComposable.deleteRating(rating.id, props.sellerId);

                    // Mostrar mensaje de éxito con SweetAlert2 con botón de confirmación
                    await Swal.fire({
                        title: 'Eliminada',
                        text: isOwnRating ?
                            'Tu valoración ha sido eliminada correctamente' :
                            'La valoración ha sido eliminada correctamente',
                        icon: 'success',
                        confirmButtonText: 'Aceptar',
                        confirmButtonColor: '#3b82f6',
                        // Eliminamos el timer para que no se cierre automáticamente
                        // timer: 2000,
                        showConfirmButton: true
                    });

                    // Recargar las valoraciones al hacer clic en "Aceptar"
                    await loadRatings();

                } catch (err: any) {
                    // Mostrar error con SweetAlert2
                    await Swal.fire({
                        title: 'Error',
                        text: err.message || 'Ha ocurrido un error al eliminar la valoración',
                        icon: 'error',
                    });
                }
            }
        };

        // Watch for changes in sellerId to reload ratings
        watch(() => props.sellerId, () => {
            loadRatings();
        });

        // Initial load
        onMounted(() => {
            loadRatings();
            loadUserData();
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
            canReplyToRating,
            canDeleteRating,
            confirmDeleteRating
        };
    }
});
</script>

<style scoped>
/* Mantener sólo las transiciones que no son fáciles de lograr con Tailwind */
@keyframes spin {
    to {
        transform: rotate(360deg);
    }
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
