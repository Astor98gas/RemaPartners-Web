<template>
    <div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <!-- Cabecera -->
        <div class="mb-8">
            <h1 class="text-3xl font-bold text-gray-900 relative inline-block">
                {{ t('chat.list.title') }}
                <span
                    class="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-blue-500 to-blue-300 rounded-full"></span>
            </h1>
        </div>

        <!-- Spinner de carga -->
        <div v-if="loading" class="flex flex-col justify-center items-center py-20">
            <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-500"></div>
            <p class="text-gray-500 mt-4 text-lg">{{ t('chat.loading') }}</p>
        </div>

        <!-- Mensaje de error -->
        <div v-else-if="error" class="bg-red-50 border-l-4 border-red-500 text-red-700 p-6 rounded-lg mb-8">
            <div class="flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <p class="font-semibold text-lg">{{ t('common.error') }}</p>
            </div>
            <p class="mt-2">{{ error }}</p>
        </div>

        <!-- Sin chats -->
        <div v-else-if="!chats || chats.length === 0" class="bg-white shadow-lg rounded-xl p-8 text-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-400 mb-4" fill="none"
                viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
            </svg>
            <h2 class="text-xl font-semibold text-gray-700 mb-2">{{ t('chat.list.empty') }}</h2>
            <p class="text-gray-500 mb-6">{{ t('chat.list.emptyDescription') }}</p>
            <router-link to="/"
                class="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                </svg>
                {{ t('chat.list.browseProducts') }}
            </router-link>
        </div>

        <!-- Lista de Chats -->
        <div v-else class="grid grid-cols-1 gap-6">
            <div v-for="chat in chats" :key="chat.id"
                class="bg-white shadow-lg rounded-xl overflow-hidden hover:shadow-xl transition-shadow">
                <div class="p-6 flex flex-col md:flex-row justify-between items-start md:items-center">
                    <!-- Información del producto -->
                    <div class="flex flex-1 items-start space-x-4 mb-4 md:mb-0">
                        <div v-if="getProductImage(chat.idProducto)"
                            class="flex-shrink-0 h-24 w-24 rounded-lg overflow-hidden bg-gray-100">
                            <img :src="getProductImage(chat.idProducto)" :alt="getProductTitle(chat.idProducto)"
                                class="h-full w-full object-cover" @error="onImageError" />
                        </div>
                        <div v-else
                            class="flex-shrink-0 h-16 w-16 rounded-lg overflow-hidden bg-gray-100 flex items-center justify-center">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-gray-400" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                            </svg>
                        </div>
                        <div class="flex-1">
                            <h3 class="text-lg font-semibold text-gray-900">{{ getProductTitle(chat.idProducto) ||
                                t('chat.unknownProduct') }}</h3>
                            <!-- Mostrar con quién se está conversando -->
                            <p class="text-blue-600 text-sm font-medium mt-1">
                                {{ getUserRole(chat) }}
                            </p>
                            <!-- Último mensaje -->
                            <p v-if="getLastMessage(chat)" class="text-gray-500 mt-1 text-sm line-clamp-1">
                                {{ getLastMessage(chat) }}
                            </p>
                            <p v-else class="text-gray-400 mt-1 text-sm italic">
                                {{ t('chat.noMessages') }}
                            </p>
                            <!-- Fecha de última actualización -->
                            <p class="text-xs text-gray-400 mt-1">
                                {{ formatDate(chat.ultimaActualizacion || chat.fechaCreacion) }}
                            </p>
                        </div>
                    </div>

                    <!-- Botones de acción -->
                    <div class="flex space-x-2">
                        <button @click="openChatDetails(chat)"
                            class="flex items-center justify-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                            </svg>
                            {{ t('chat.viewChat') }}
                        </button>
                        <button @click="viewProduct(chat.idProducto)"
                            class="flex items-center justify-center px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                            </svg>
                            {{ t('producto.action.details') }}
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Chat Modal -->
        <div v-if="showChatModal && selectedChat"
            class="fixed inset-0 bg-purple-700/10 flex items-center justify-center z-50 p-4">
            <div class="flex-1 p-4 overflow-hidden">
                <ChatBox :product-id="selectedChat.idProducto" :seller-id="selectedChat.idVendedor"
                    :user-id="currentUser.id" :chat-id="selectedChat.id" @close="showChatModal = false" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { useChat } from '@/composables/useChat';
import { useProducto } from '@/composables/useProducto';
import { useUsers } from '@/composables/useUsers';
import { useutf8Store } from '@/stores/counter';
import ChatBox from '@/components/chat/ChatBox.vue';
import type { ChatEntity } from '@/models/chat';

export default defineComponent({
    name: 'ChatsListView',
    components: {
        ChatBox
    },
    data() {
        return {
            loading: true,
            error: null as string | null,
            chats: [] as ChatEntity[],
            currentUser: null as any,
            productCache: {} as Record<string, any>,
            userCache: {} as Record<string, string>, // Nuevo: caché para nombres de usuarios
            showChatModal: false,
            selectedChat: null as ChatEntity | null
        };
    },
    async mounted() {
        try {
            // Obtener usuario actual
            const usersComposable = useUsers();
            this.currentUser = await usersComposable.isLoggedIn();

            if (!this.currentUser) {
                this.error = this.t('auth.login_required');
                this.loading = false;
                return;
            }

            await this.loadChats();
        } catch (err: any) {
            console.error('Error al cargar la vista de chats:', err);
            this.error = err.response?.data?.message || this.t('chat.list.loadError');
        } finally {
            this.loading = false;
        }
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },

        async loadChats() {
            try {
                this.loading = true;
                this.error = null;

                const chatComposable = useChat();

                // Cargar todos los chats relevantes para el usuario actual
                // independientemente de si es comprador o vendedor
                const userId = this.currentUser.id;

                // Obtener chats como comprador
                await chatComposable.getChatsByBuyerId(userId);
                const buyerChats = [...chatComposable.chats.value];

                // Obtener chats como vendedor
                await chatComposable.getChatsBySellerId(userId);
                const sellerChats = [...chatComposable.chats.value];

                // Combinar ambos conjuntos de chats y eliminar duplicados
                const allChats = [...buyerChats, ...sellerChats];
                this.chats = allChats.filter((chat, index, self) =>
                    index === self.findIndex((c) => c.id === chat.id)
                );

                // Precarga información de productos para mostrar
                await this.preloadProductInfo();

                // Precarga información de usuarios para mostrar
                await this.preloadUserInfo();
            } catch (err: any) {
                console.error('Error cargando chats:', err);
                this.error = err.message || this.t('chat.list.loadError');
            } finally {
                this.loading = false;
            }
        },

        async preloadProductInfo() {
            const productoService = useProducto();

            // Obtener los IDs de producto únicos
            const productIds = Array.from(new Set(this.chats.map(chat => chat.idProducto)));

            // Cargar información básica de los productos
            for (const productId of productIds) {
                try {
                    await productoService.getProductoById(productId);
                    this.productCache[productId] = productoService.currentProducto.value;
                } catch (error) {
                    console.error(`Error al cargar producto ${productId}:`, error);
                    // No almacenamos nada en el cache si hay error
                }
            }
        },

        async preloadUserInfo() {
            const chatComposable = useChat();

            // Construir lista de IDs de usuario a cargar (tanto vendedores como compradores)
            const userIds = new Set<string>();

            for (const chat of this.chats) {
                // Determinar cuál es el usuario con el que se está conversando
                const partnerId = chat.idComprador === this.currentUser.id ?
                    chat.idVendedor : chat.idComprador;
                userIds.add(partnerId);
            }

            // Cargar los nombres de usuarios
            for (const userId of userIds) {
                try {
                    const userName = await chatComposable.getUserNameById(userId);
                    if (userName) {
                        this.userCache[userId] = userName;
                    }
                } catch (error) {
                    console.error(`Error al cargar nombre de usuario ${userId}:`, error);
                }
            }
        },

        getProductTitle(productId: string): string {
            return this.productCache[productId]?.titulo || '';
        },

        getProductImage(productId: string): string {
            if (this.productCache[productId]?.imagenes && this.productCache[productId].imagenes.length > 0) {
                return this.productCache[productId].imagenes[0];
            }
            return '';
        },

        getUserName(chat: ChatEntity): string {
            // Determinar cuál es el usuario con el que se está conversando
            const partnerId = chat.idComprador === this.currentUser.id ?
                chat.idVendedor : chat.idComprador;

            return this.userCache[partnerId] || this.t('common.notAvailable');
        },

        getUserRole(chat: ChatEntity): string {
            // Determinar si el usuario con quien estamos hablando es un comprador o un vendedor
            const isSeller = this.currentUser.id === chat.idVendedor;
            const userName = this.getUserName(chat);

            if (isSeller) {
                return this.t('chat.withUser.buyer').replace('{userName}', userName);
            } else {
                return this.t('chat.withUser.seller').replace('{userName}', userName);
            }
        },

        getLastMessage(chat: ChatEntity): string {
            if (chat.mensajes && chat.mensajes.length > 0) {
                return chat.mensajes[chat.mensajes.length - 1].mensaje;
            }
            return '';
        },

        formatDate(dateStr: string): string {
            if (!dateStr) return this.t('common.notAvailable');

            try {
                const date = new Date(dateStr);
                return new Intl.DateTimeFormat(document.documentElement.lang || 'es', {
                    year: 'numeric',
                    month: 'short',
                    day: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit'
                }).format(date);
            } catch (error) {
                console.error('Error formatting date:', error);
                return dateStr;
            }
        },

        onImageError(event: Event) {
            (event.target as HTMLImageElement).src = new URL('@/assets/logoCuadrado.jpeg', import.meta.url).href;
        },

        openChatDetails(chat: ChatEntity) {
            this.selectedChat = chat;
            this.showChatModal = true;
        },

        viewProduct(productId: string) {
            this.$router.push(`/producto/${productId}`);
        }
    }
});
</script>

<style scoped>
.line-clamp-1 {
    overflow: hidden;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 1;
    line-clamp: 1;
}
</style>