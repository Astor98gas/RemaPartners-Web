<template>
    <div class="rating-card" :class="{ 'highlight': highlight }">
        <div class="rating-header">
            <div class="user-info">
                <div class="user-avatar">
                    <div v-if="rating.username" class="avatar-text">
                        {{ getInitials(rating.username) }}
                    </div>
                </div>
                <div class="user-details">
                    <h4 class="username">{{ rating.username }}</h4>
                    <div class="rating-date">
                        {{ formatDate(rating.updatedAt !== rating.createdAt ? rating.updatedAt : rating.createdAt) }}
                        <span v-if="rating.updatedAt !== rating.createdAt" class="edited-badge">
                            (editado)
                        </span>
                    </div>
                </div>
            </div>
            <div class="rating-score">
                <StarRating :modelValue="rating.rating" :showRatingText="true" />
            </div>
        </div>

        <div class="rating-content">
            <p class="rating-comment">{{ rating.comment }}</p>
        </div>

        <!-- Reply section -->
        <div v-if="rating.reply" class="rating-reply">
            <div class="reply-header">
                <div class="reply-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M7.707 3.293a1 1 0 010 1.414L5.414 7H11a7 7 0 017 7v2a1 1 0 11-2 0v-2a5 5 0 00-5-5H5.414l2.293 2.293a1 1 0 11-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                            clip-rule="evenodd" />
                    </svg>
                </div>
                <div class="reply-title">
                    <strong>Respuesta del vendedor</strong>
                </div>
            </div>
            <p class="reply-content">{{ rating.reply }}</p>
        </div>

        <!-- Actions for editing/replying (if user has permissions) -->
        <div class="rating-actions" v-if="canEdit || canReply">
            <button v-if="canEdit" @click="$emit('edit')" class="action-btn edit-btn" title="Editar valoración">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
                <span>Editar</span>
            </button>

            <button v-if="canReply" @click="$emit('reply')" class="action-btn reply-btn"
                title="Responder a esta valoración">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6" />
                </svg>
                <span>Responder</span>
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import type { Rating } from '@/models/rating';
import StarRating from './StarRating.vue';

export default defineComponent({
    name: 'RatingItem',
    components: {
        StarRating
    },
    props: {
        rating: {
            type: Object as () => Rating,
            required: true
        },
        canEdit: {
            type: Boolean,
            default: false
        },
        canReply: {
            type: Boolean,
            default: false
        },
        highlight: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        getInitials(name: string): string {
            if (!name) return '';
            return name.split(' ')
                .map(word => word.charAt(0).toUpperCase())
                .join('')
                .substring(0, 2);
        },
        formatDate(dateStr: string): string {
            if (!dateStr) return '';

            const date = new Date(dateStr);
            return date.toLocaleDateString('es-ES', {
                year: 'numeric',
                month: 'short',
                day: 'numeric'
            });
        }
    }
});
</script>

<style scoped>
.rating-card {
    border: 1px solid #e5e7eb;
    border-radius: 0.5rem;
    padding: 1rem;
    margin-bottom: 1rem;
    background-color: white;
    transition: box-shadow 0.2s ease, border-color 0.2s ease;
}

.rating-card.highlight {
    border-color: #3b82f6;
    box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.5);
}

.rating-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 0.75rem;
}

.user-info {
    display: flex;
    align-items: center;
    gap: 0.75rem;
}

.user-avatar {
    width: 2.5rem;
    height: 2.5rem;
    border-radius: 50%;
    background-color: #e5e7eb;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #374151;
    font-weight: 600;
}

.avatar-text {
    font-size: 1rem;
}

.user-details {
    display: flex;
    flex-direction: column;
}

.username {
    font-weight: 600;
    font-size: 0.9rem;
    color: #111827;
    margin: 0;
}

.rating-date {
    font-size: 0.75rem;
    color: #6b7280;
}

.edited-badge {
    font-style: italic;
}

.rating-content {
    margin-bottom: 1rem;
}

.rating-comment {
    margin: 0;
    white-space: pre-line;
    line-height: 1.5;
}

.rating-reply {
    background-color: #f3f4f6;
    border-radius: 0.375rem;
    padding: 0.75rem;
    margin-top: 0.75rem;
}

.reply-header {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    margin-bottom: 0.5rem;
}

.reply-icon {
    color: #6b7280;
    width: 1.25rem;
    height: 1.25rem;
}

.reply-title {
    font-size: 0.875rem;
    color: #4b5563;
}

.reply-content {
    margin: 0;
    font-size: 0.9rem;
    white-space: pre-line;
    line-height: 1.5;
    color: #1f2937;
}

.rating-actions {
    display: flex;
    gap: 0.5rem;
    margin-top: 1rem;
    justify-content: flex-end;
}

.action-btn {
    display: flex;
    align-items: center;
    gap: 0.375rem;
    padding: 0.375rem 0.5rem;
    font-size: 0.75rem;
    border-radius: 0.25rem;
    cursor: pointer;
    transition: background-color 0.2s ease;
    border: none;
}

.edit-btn {
    background-color: #eff6ff;
    color: #2563eb;
}

.edit-btn:hover {
    background-color: #dbeafe;
}

.reply-btn {
    background-color: #f3f4f6;
    color: #4b5563;
}

.reply-btn:hover {
    background-color: #e5e7eb;
}
</style>
