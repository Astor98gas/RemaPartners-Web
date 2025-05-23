<template>
    <div class="rating-form-container">
        <h3 class="form-title">{{ isEditing ? 'Editar valoración' : isReplying ? 'Responder a valoración' :
            'Añadir valoración' }}</h3>

        <div v-if="!isReplying" class="rating-stars-container">
            <label for="rating" class="form-label">Puntuación</label>
            <StarRating v-model="formData.rating" :interactive="true" :disabled="submitting" />
        </div>

        <div class="form-group">
            <label for="comment" class="form-label">{{ isReplying ? 'Tu respuesta' : 'Comentario' }}</label>
            <textarea id="comment" v-model="formData.comment" rows="4" class="form-textarea"
                :placeholder="isReplying ? 'Escribe tu respuesta a esta valoración...' : 'Comparte tu experiencia con este vendedor...'"
                :disabled="submitting" :maxlength="maxCommentLength"></textarea>
            <div class="comment-length">
                {{ formData.comment.length }} / {{ maxCommentLength }}
            </div>
        </div>

        <div class="form-actions">
            <button type="button" @click="cancel" class="btn cancel-btn" :disabled="submitting">
                Cancelar
            </button>
            <button type="button" @click="submit" class="btn submit-btn" :disabled="!isValid || submitting">
                <span v-if="submitting" class="loading-spinner"></span>
                <span>{{ isEditing ? 'Guardar cambios' : isReplying ? 'Enviar respuesta' : 'Enviar valoración' }}</span>
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch } from 'vue';
import type { Rating, RatingFormData, RatingReplyData } from '@/models/rating';
import StarRating from './StarRating.vue';

/**
 * Componente de formulario para crear, editar o responder valoraciones.
 * 
 * Permite al usuario añadir una valoración, editar una existente o responder como vendedor.
 * 
 * @component
 * @example
 * <RatingForm :sellerId="sellerId" :existingRating="rating" :isEditing="true" @submit="onSubmit" @cancel="onCancel" />
 */
export default defineComponent({
    name: 'RatingForm',
    components: {
        StarRating
    },
    props: {
        /**
         * ID del vendedor al que se le realiza la valoración.
         * @type {string}
         * @required
         */
        sellerId: {
            type: String,
            required: true
        },
        /**
         * Valoración existente para editar o responder.
         * @type {Rating|null}
         * @default null
         */
        existingRating: {
            type: Object as () => Rating | null,
            default: null
        },
        /**
         * Indica si el formulario está en modo edición.
         * @type {boolean}
         * @default false
         */
        isEditing: {
            type: Boolean,
            default: false
        },
        /**
         * Indica si el formulario es para responder a una valoración.
         * @type {boolean}
         * @default false
         */
        isReplying: {
            type: Boolean,
            default: false
        }
    },
    emits: ['submit', 'cancel'],
    setup(props, { emit }) {
        const maxCommentLength = 500;
        const submitting = ref(false);

        /**
         * Datos del formulario reactivos.
         */
        const formData = ref({
            sellerId: props.sellerId,
            rating: props.existingRating?.rating || 0,
            comment: props.isReplying
                ? ''
                : (props.existingRating?.comment || '')
        });

        // Si cambian las props, actualizar el formulario
        watch(() => props.existingRating, (newRating) => {
            if (newRating) {
                formData.value.rating = newRating.rating || 0;
                formData.value.comment = props.isReplying ? '' : (newRating.comment || '');
            }
        });

        watch(() => props.sellerId, (newSellerId) => {
            formData.value.sellerId = newSellerId;
        });

        /**
         * Valida si el formulario es válido para enviar.
         */
        const isValid = computed(() => {
            // Para respuestas, solo necesitamos un comentario
            if (props.isReplying) {
                return formData.value.comment.trim().length > 0;
            }

            // Para valoraciones, necesitamos puntuación y comentario
            return formData.value.rating > 0 && formData.value.comment.trim().length > 0;
        });

        /**
         * Envía el formulario, emitiendo el evento correspondiente.
         */
        const submit = async () => {
            if (!isValid.value) return;

            submitting.value = true;
            try {
                if (props.isReplying && props.existingRating) {
                    // Enviar respuesta
                    const replyData: RatingReplyData = {
                        ratingId: props.existingRating.id,
                        reply: formData.value.comment
                    };
                    emit('submit', replyData);
                } else {
                    // Enviar valoración (nueva o editada)
                    const ratingData: RatingFormData = {
                        sellerId: formData.value.sellerId,
                        rating: formData.value.rating,
                        comment: formData.value.comment
                    };
                    emit('submit', ratingData);
                }
            } finally {
                submitting.value = false;
            }
        };

        /**
         * Cancela el formulario, emitiendo el evento correspondiente.
         */
        const cancel = () => {
            emit('cancel');
        };

        return {
            formData,
            maxCommentLength,
            isValid,
            submitting,
            submit,
            cancel
        };
    }
});
</script>

<style scoped>
.rating-form-container {
    background-color: white;
    border-radius: 0.5rem;
    padding: 1.25rem;
    margin-bottom: 1.5rem;
    border: 1px solid #e5e7eb;
}

.form-title {
    margin-top: 0;
    margin-bottom: 1rem;
    font-size: 1.25rem;
    font-weight: 600;
    color: #1f2937;
}

.rating-stars-container {
    margin-bottom: 1rem;
}

.form-label {
    display: block;
    font-size: 0.875rem;
    font-weight: 500;
    color: #4b5563;
    margin-bottom: 0.5rem;
}

.form-textarea {
    width: 100%;
    padding: 0.625rem;
    border: 1px solid #d1d5db;
    border-radius: 0.375rem;
    font-size: 0.875rem;
    line-height: 1.5;
    transition: border-color 0.2s ease;
    resize: vertical;
}

.form-textarea:focus {
    outline: none;
    border-color: #3b82f6;
    box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.5);
}

.form-textarea:disabled {
    background-color: #f9fafb;
    cursor: not-allowed;
}

.comment-length {
    font-size: 0.75rem;
    color: #6b7280;
    text-align: right;
    margin-top: 0.25rem;
}

.form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 0.75rem;
    margin-top: 1rem;
}

.btn {
    padding: 0.5rem 1rem;
    font-size: 0.875rem;
    font-weight: 500;
    border-radius: 0.375rem;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s ease;
    border: 1px solid transparent;
}

.cancel-btn {
    background-color: white;
    color: #4b5563;
    border-color: #d1d5db;
}

.cancel-btn:hover:not(:disabled) {
    background-color: #f3f4f6;
}

.submit-btn {
    background-color: #3b82f6;
    color: white;
}

.submit-btn:hover:not(:disabled) {
    background-color: #2563eb;
}

.btn:disabled {
    opacity: 0.7;
    cursor: not-allowed;
}

.loading-spinner {
    width: 1rem;
    height: 1rem;
    border-radius: 50%;
    border: 2px solid rgba(255, 255, 255, 0.3);
    border-top-color: white;
    animation: spin 1s linear infinite;
    margin-right: 0.5rem;
}

@keyframes spin {
    to {
        transform: rotate(360deg);
    }
}
</style>
