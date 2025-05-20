<template>
    <div class="bg-white shadow-md rounded-lg p-4 overflow-hidden mb-6 border border-gray-200" v-if="seller">
        <div class="flex flex-col sm:flex-row items-center sm:items-start gap-4">
            <!-- Seller Image -->
            <div class="flex-shrink-0">
                <div class="w-20 h-20 rounded-full overflow-hidden border-2 border-blue-500">
                    <img v-if="seller.profileImage" :src="seller.profileImage" :alt="seller.username"
                        class="w-full h-full object-cover" @error="onImageError" />
                    <div v-else class="w-full h-full flex items-center justify-center bg-gray-100 text-gray-400">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                        </svg>
                    </div>
                </div>
            </div>

            <!-- Seller Info -->
            <div class="flex-1 text-center sm:text-left">
                <h3 class="text-lg font-semibold text-gray-800">{{ seller.username }}</h3>

                <!-- Seller Rating -->
                <div v-if="ratings.length > 0" class="flex items-center mt-2">
                    <div class="flex items-center">
                        <div v-for="i in 5" :key="i">
                            <svg :class="{ 'text-yellow-400': i <= averageRating, 'text-gray-300': i > averageRating }"
                                xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                fill="currentColor">
                                <path
                                    d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118l-2.8-2.034c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                            </svg>
                        </div>
                    </div>
                    <span class="ml-2 text-sm text-gray-600">
                        {{ averageRating.toFixed(1) }} ({{ ratings.length }} {{ ratings.length === 1 ?
                            t('ratings.review') : t('ratings.reviews') }})
                    </span>
                </div>
                <div v-else class="mt-2 text-sm text-gray-500">
                    {{ t('ratings.no_reviews_yet') }}
                </div>

                <p v-if="seller.description" class="text-gray-600 mt-2 line-clamp-2">{{ seller.description }}</p>

                <!-- Social Links -->
                <div v-if="seller.socialLinks && seller.socialLinks.length > 0" class="mt-2 flex flex-wrap gap-2">
                    <a v-for="(link, index) in seller.socialLinks" :key="index" :href="link.url" target="_blank"
                        rel="noopener noreferrer"
                        class="text-blue-600 hover:text-blue-800 text-xs flex items-center gap-1">
                        <i v-if="link.icon" :class="link.icon"></i>
                        <span>{{ link.platform }}</span>
                    </a>
                </div>

                <!-- Actions -->
                <div class="mt-3 flex flex-wrap gap-2">
                    <router-link :to="`/user/${seller.id}`"
                        class="inline-flex items-center text-sm text-blue-600 hover:text-blue-800">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                        </svg>
                        {{ t('profile.view_profile') }}
                    </router-link>
                    <router-link :to="`/user/${seller.id}#ratings`"
                        class="inline-flex items-center text-sm text-blue-600 hover:text-blue-800">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z" />
                        </svg>
                        {{ t('ratings.see_all_reviews') }}
                    </router-link>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from 'vue';
import { useutf8Store } from '@/stores/counter';
import type { User } from '@/models/user';
import type { Rating } from '@/models/rating';
import { useRatings } from '@/composables/useRatings';

export default defineComponent({
    name: 'SellerProfileCard',
    props: {
        seller: {
            type: Object as () => User | null,
            required: true
        }
    },
    setup(props) {
        const ratings = ref<Rating[]>([]);
        const loading = ref(false);
        const error = ref<string | null>(null);
        const ratingsService = useRatings();

        const averageRating = computed(() => {
            if (ratings.value.length === 0) return 0;
            const sum = ratings.value.reduce((acc, rating) => acc + rating.rating, 0);
            return sum / ratings.value.length;
        });

        const loadRatings = async () => {
            if (!props.seller?.id) return;

            try {
                loading.value = true;
                const data = await ratingsService.getSellerRatings(props.seller.id);
                ratings.value = data || [];
            } catch (err) {
                console.error('Error loading seller ratings:', err);
                error.value = 'Error loading ratings';
            } finally {
                loading.value = false;
            }
        };

        onMounted(() => {
            loadRatings();
        });

        return {
            ratings,
            averageRating,
            loading,
            error
        };
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },
        onImageError(event: Event) {
            (event.target as HTMLImageElement).src = new URL('@/assets/logoCuadrado.jpeg', import.meta.url).href;
        }
    }
});
</script>

<style scoped>
.line-clamp-2 {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
</style>
