<template>
    <div class="star-rating">
        <div class="stars-container" :class="{ 'interactive': interactive }">
            <button v-for="n in 5" :key="n" type="button" class="star-btn" @click="interactive ? setRating(n) : null"
                @mouseover="interactive ? hoverRating = n : null" @mouseleave="interactive ? hoverRating = 0 : null"
                :title="interactive ? `Valorar con ${n} ${n === 1 ? 'estrella' : 'estrellas'}` : ''"
                :disabled="!interactive || disabled">
                <svg xmlns="http://www.w3.org/2000/svg" class="star-icon" :class="{
                    'filled': (hoverRating || modelValue) >= n,
                    'hover': interactive && hoverRating >= n,
                    'selected': !hoverRating && modelValue >= n
                }" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                    <path d="M12 17.8l-6.18 3.25 1.18-6.9L2 9.27l6.91-1.01L12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.9z"
                        :fill="(hoverRating || modelValue) >= n ? 'currentColor' : 'none'" />
                </svg>
            </button>
        </div>
        <div v-if="showRatingText && modelValue > 0" class="rating-text">
            {{ modelValue }}/5
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
    name: 'StarRating',
    props: {
        modelValue: {
            type: Number,
            default: 0
        },
        interactive: {
            type: Boolean,
            default: false
        },
        showRatingText: {
            type: Boolean,
            default: false
        },
        disabled: {
            type: Boolean,
            default: false
        }
    },
    setup(props, { emit }) {
        const hoverRating = ref(0);

        const setRating = (rating: number) => {
            if (props.interactive && !props.disabled) {
                // Si el rating actual es igual al que se está pulsando, 
                // deseleccionamos (valor 0)
                if (props.modelValue === rating) {
                    emit('update:modelValue', 0);
                } else {
                    emit('update:modelValue', rating);
                }
            }
        };

        return {
            hoverRating,
            setRating
        };
    }
});
</script>

<style scoped>
.star-rating {
    display: flex;
    align-items: center;
    gap: 8px;
}

.stars-container {
    display: flex;
    gap: 2px;
}

.star-btn {
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: center;
}

.interactive .star-btn {
    transition: transform 0.1s ease;
}

.interactive .star-btn:hover {
    transform: scale(1.1);
}

.interactive .star-btn:disabled {
    cursor: not-allowed;
}

.star-icon {
    width: 24px;
    height: 24px;
    color: #d1d5db;
    /* gray-300 */
}

.star-icon.hover {
    color: #facc15;
    /* yellow-400 */
}

.star-icon.selected {
    color: #f59e0b;
    /* amber-500 */
}

.rating-text {
    font-size: 0.875rem;
    color: #6b7280;
    /* gray-500 */
    font-weight: 500;
}
</style>
