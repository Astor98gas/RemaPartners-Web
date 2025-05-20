<template>
    <div class="border border-gray-200 rounded-lg p-4 mb-4 bg-white transition-all duration-200"
        :class="{ 'border-blue-500 shadow-sm shadow-blue-100': highlight }">
        <div class="flex justify-between items-start mb-3">
            <div class="flex items-center gap-3">
                <div
                    class="w-10 h-10 rounded-full bg-gray-200 flex justify-center items-center text-gray-700 font-semibold">
                    <div v-if="rating.username" class="text-base">
                        {{ getInitials(rating.username) }}
                    </div>
                </div>
                <div class="flex flex-col">
                    <h4 class="font-semibold text-sm text-gray-900 m-0">{{ rating.username }}</h4>
                    <div class="text-xs text-gray-500">
                        {{ formatDate(rating.updatedAt !== rating.createdAt ? rating.updatedAt : rating.createdAt) }}
                        <span v-if="rating.updatedAt !== rating.createdAt" class="italic">
                            ({{ t('ratings.edited') }})
                        </span>
                    </div>
                </div>
            </div>
            <div class="rating-score">
                <StarRating :modelValue="rating.rating" :showRatingText="true" />
            </div>
        </div>

        <div class="mb-4">
            <p class="m-0 whitespace-pre-line leading-6">{{ rating.comment }}</p>
        </div>

        <!-- Reply section -->
        <div v-if="rating.reply" class="bg-gray-100 rounded-md p-3 mt-3">
            <div class="flex items-center gap-2 mb-2">
                <div class="text-gray-500 w-5 h-5">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd"
                            d="M7.707 3.293a1 1 0 010 1.414L5.414 7H11a7 7 0 017 7v2a1 1 0 11-2 0v-2a5 5 0 00-5-5H5.414l2.293 2.293a1 1 0 11-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                            clip-rule="evenodd" />
                    </svg>
                </div>
                <div class="text-sm text-gray-600">
                    <strong>{{ t('ratings.seller_response') }}</strong>
                </div>
            </div>
            <p class="m-0 text-sm whitespace-pre-line leading-6 text-gray-800">{{ rating.reply }}</p>
        </div>

        <!-- Actions for editing/replying (if user has permissions) -->
        <div v-if="canEdit || canReply || canDelete" class="flex gap-2 mt-4 justify-end">
            <button v-if="canEdit" @click="$emit('edit')"
                class="flex items-center gap-1.5 py-1.5 px-2 text-xs bg-blue-50 text-blue-600 rounded hover:bg-blue-100 transition-colors duration-200 border-none"
                :title="t('ratings.edit')">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
                <span>{{ t('ratings.edit') }}</span>
            </button>

            <button v-if="canReply" @click="$emit('reply')"
                class="flex items-center gap-1.5 py-1.5 px-2 text-xs bg-gray-100 text-gray-600 rounded hover:bg-gray-200 transition-colors duration-200 border-none"
                :title="t('ratings.reply')">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6" />
                </svg>
                <span>{{ t('ratings.reply') }}</span>
            </button>

            <button v-if="canDelete" @click="$emit('delete')"
                class="flex items-center gap-1.5 py-1.5 px-2 text-xs bg-red-50 text-red-600 rounded hover:bg-red-100 transition-colors duration-200 border-none"
                :title="t('ratings.delete')">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
                <span>{{ t('ratings.delete') }}</span>
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import type { Rating } from '@/models/rating';
import StarRating from './StarRating.vue';
import { useutf8Store } from '@/stores/counter';

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
        canDelete: {
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
        },
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        }
    }
});
</script>
