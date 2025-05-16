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
                <span class="inline-block bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full mt-1">{{
                    seller.rol?.name }}</span>

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
                <div class="mt-3">
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
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { useutf8Store } from '@/stores/counter';
import type { User } from '@/models/user';

export default defineComponent({
    name: 'SellerProfileCard',
    props: {
        seller: {
            type: Object as () => User | null,
            required: true
        }
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
