<template>
    <div class="min-h-screen bg-gray-100 py-8 px-4 sm:px-6 lg:px-8">
        <div class="max-w-4xl mx-auto">
            <!-- Sección de carga -->
            <div v-if="loading" class="flex justify-center items-center h-64">
                <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>

            <!-- Sección de error -->
            <div v-else-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-6">
                <p>{{ error }}</p>
                <button @click="$router.go(-1)"
                    class="mt-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                    {{ t('common.go_back') }}
                </button>
            </div>

            <!-- Perfil de usuario -->
            <div v-else-if="userProfile" class="bg-white shadow overflow-hidden rounded-lg">
                <!-- Cabecera -->
                <div class="bg-gradient-to-r from-blue-500 to-indigo-600 px-6 py-5">
                    <h1 class="text-2xl font-semibold text-white">{{ t('profile.user_profile') }}</h1>
                </div>

                <!-- Información del perfil -->
                <div class="p-6">
                    <div class="rounded-lg bg-gray-50 p-6 shadow-md">
                        <!-- Imagen de perfil y nombre de usuario -->
                        <div class="flex flex-col items-center mb-6">
                            <div class="w-32 h-32 rounded-full overflow-hidden border-4 border-blue-500 mb-3 bg-white">
                                <img v-if="userProfile.profileImage" :src="userProfile.profileImage"
                                    :alt="userProfile.username" class="w-full h-full object-cover"
                                    @error="onImageError">
                                <div v-else
                                    class="w-full h-full flex items-center justify-center bg-gray-100 text-gray-400">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16" fill="none"
                                        viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                    </svg>
                                </div>
                            </div>
                            <h3 class="text-2xl font-bold text-gray-800">{{ userProfile.username }}</h3>
                        </div>

                        <!-- Descripción del perfil -->
                        <div v-if="userProfile.description" class="mb-6">
                            <p class="text-sm text-gray-500">{{ t('profile.description') }}</p>
                            <p class="text-lg font-medium whitespace-pre-line">{{ userProfile.description }}</p>
                        </div>

                        <!-- Enlaces a redes sociales -->
                        <div v-if="userProfile.socialLinks && userProfile.socialLinks.length > 0" class="mb-6">
                            <p class="text-sm text-gray-500 mb-2">{{ t('profile.social_links') }}</p>
                            <div class="flex flex-wrap gap-3">
                                <a v-for="(link, index) in userProfile.socialLinks" :key="index" :href="link.url"
                                    target="_blank" rel="noopener noreferrer"
                                    class="px-3 py-2 bg-gray-100 hover:bg-gray-200 text-gray-800 rounded-lg flex items-center transition-colors">
                                    <i v-if="link.icon" :class="link.icon" class="mr-2"></i>
                                    <span>{{ link.platform }}</span>
                                </a>
                            </div>
                        </div>

                        <!-- Acciones de contacto -->
                        <div v-if="currentUser && currentUser.id !== userProfile.id" class="mt-8 flex justify-center">

                        </div>
                    </div>

                    <!-- Productos del vendedor -->
                    <div v-if="isVendedor && products.length > 0" class="mt-8">
                        <h2 class="text-xl font-semibold border-b-1 border-gray-200 pb-2 mb-4">
                            {{ t('profile.user_products', { username: userProfile.username }) }}
                        </h2>
                        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                            <div v-for="product in products" :key="product.id"
                                class="bg-white shadow-md rounded-lg overflow-hidden hover:shadow-lg transition-shadow">
                                <div class="h-48 overflow-hidden">
                                    <img v-if="product.imagenes && product.imagenes.length > 0"
                                        :src="product.imagenes[0]" :alt="product.titulo"
                                        class="w-full h-full object-cover" @error="onProductImageError">
                                    <div v-else class="w-full h-full flex items-center justify-center bg-gray-100">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-gray-400"
                                            fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                        </svg>
                                    </div>
                                </div>
                                <div class="p-4">
                                    <h3 class="text-lg font-medium text-gray-900 mb-2">{{ product.titulo }}</h3>
                                    <span class="text-lg font-bold text-blue-600">
                                        {{ formatPrice(product.precioCentimos, product.moneda) }}
                                    </span>
                                    <div class="flex justify-center items-center">
                                        <router-link :to="`/producto/${product.id}`"
                                            class="px-3 py-1 bg-blue-100 text-blue-800 rounded-lg hover:bg-blue-200">
                                            {{ t('profile.view_product') }}
                                        </router-link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Valoraciones del vendedor -->
                    <div v-if="isVendedor" class="mt-8">
                        <RatingsSection :sellerId="userProfile.id" :currentUserId="currentUser?.id"
                            :isCurrentUserSeller="currentUser?.rol?.name === 'VENDEDOR' || currentUser?.rol?.name === 'ADMIN'" />
                    </div>
                </div>
            </div>

            <!-- Usuario no encontrado -->
            <div v-else class="bg-white shadow overflow-hidden rounded-lg p-6 text-center">
                <h2 class="text-xl font-semibold mb-4">{{ t('profile.user_not_found') }}</h2>
                <p class="mb-4">{{ t('profile.user_not_found_message') }}</p>
                <button @click="$router.go(-1)" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                    {{ t('common.go_back') }}
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUsers } from '@/composables/useUsers';
import { useutf8Store } from '@/stores/counter';
import { useProducto } from '@/composables/useProducto';
import type { User } from '@/models/user';
import type { Producto } from '@/models/producto';
import { useChat } from '@/composables/useChat';
import RatingsSection from '@/components/ratings/RatingsSection.vue';

export default defineComponent({
    name: 'PublicProfileView',
    components: {
        RatingsSection
    },
    setup() {
        const route = useRoute();
        const router = useRouter();
        const usersComposable = useUsers();
        const productoComposable = useProducto();
        const chatComposable = useChat();

        return {
            route,
            router,
            usersComposable,
            productoComposable,
            chatComposable
        };
    },
    data() {
        return {
            loading: true,
            error: null as string | null,
            userProfile: null as User | null,
            currentUser: null as User | null,
            products: [] as Producto[],
            utf8: useutf8Store()
        };
    },
    computed: {
        isVendedor(): boolean {
            return this.userProfile?.rol?.name === 'VENDEDOR' || this.userProfile?.rol?.name === 'ADMIN';
        }
    },
    async mounted() {
        try {
            this.loading = true;
            this.error = null;

            // Get the user ID from route params
            const userId = this.route.params.id as string;
            if (!userId) {
                this.error = this.t('profile.user_id_required');
                this.loading = false;
                return;
            }

            // Get current logged in user (if any)
            this.currentUser = await this.usersComposable.isLoggedIn();

            // Get the user profile
            this.userProfile = await this.usersComposable.getUserProfileById(userId);

            if (!this.userProfile) {
                this.error = this.t('profile.user_not_found');
                this.loading = false;
                return;
            }

            // If the user is a seller, get their products
            if (this.isVendedor) {
                await this.loadUserProducts(userId);
            }

        } catch (error: any) {
            console.error('Error loading user profile:', error);
            this.error = error.message || this.t('profile.load_error');
        } finally {
            this.loading = false;
        }
    },
    methods: {
        t(key: string, params?: Record<string, any>): string {
            const translation = this.utf8.t(key);
            if (params && translation) {
                return Object.entries(params).reduce(
                    (str, [key, value]) => str.replace(`{${key}}`, String(value)),
                    translation
                );
            }
            return translation || key;
        },

        onImageError(event: Event) {
            (event.target as HTMLImageElement).src = new URL('@/assets/logoCuadrado.jpeg', import.meta.url).href;
        },

        onProductImageError(event: Event) {
            (event.target as HTMLImageElement).src = new URL('@/assets/productDefault.png', import.meta.url).href;
        },

        formatPrice(precioCentimos: number, moneda: string): string {
            const precio = precioCentimos / 100;
            return new Intl.NumberFormat('es-ES', {
                style: 'currency',
                currency: moneda || 'EUR'
            }).format(precio);
        },

        async loadUserProducts(userId: string) {
            try {
                // Get products using the composable
                await this.productoComposable.getProductos();

                // Now access the reactive products value from the composable
                this.products = this.productoComposable.productos.value
                    .filter((product: Producto) =>
                        product.idUsuario === userId && product.activo);
            } catch (error) {
                console.error('Error loading user products:', error);
            }
        },

        async startChat() {
            if (!this.currentUser) {
                // Redirect to login if not logged in
                this.router.push('/login');
                return;
            }

            if (!this.userProfile) return;

            try {
                // Ideally, we would create or retrieve a chat directly for these users
                // For now, since the current system is product-based, we might need to 
                // redirect to the user's products page, or implement a direct messaging feature

                // If any products, redirect to first product
                if (this.products.length > 0) {
                    this.router.push(`/producto/${this.products[0].id}`);
                    return;
                }

                // Otherwise show a message that no chats can be started
                alert(this.t('profile.no_products_for_chat'));
            } catch (error) {
                console.error('Error starting chat:', error);
            }
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
