<template>
    <div class="chat-box rounded-lg overflow-hidden border border-gray-300 shadow-lg flex flex-col">
        <!-- Chat header -->
        <div class="bg-blue-600 text-white py-3 px-4 flex justify-between items-center">
            <div class="flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                </svg>
                <div>
                    <h3 class="font-semibold">{{ isSeller ? t('chat.title.asSeller') : t('chat.title') }}</h3>
                    <p v-if="chatPartnerName" class="text-xs text-blue-100">
                        {{ isSeller ?
                            t('chat.withUser.buyer', { userName: chatPartnerName }) :
                            t('chat.withUser.seller', { userName: chatPartnerName })
                        }}
                    </p>
                </div>
            </div>
            <div class="flex items-center space-x-2">
                <!-- Delete Chat Button -->
                <button v-if="productNotFound" @click="confirmDeleteChat"
                    class="text-white bg-red-500 hover:bg-red-600 rounded-lg px-2 py-1 text-xs flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                    {{ t('chat.delete') }}
                </button>

                <!-- Mark as Sold Button (Only for sellers) -->
                <button v-if="isSeller && !productNotFound && currentProduct && currentProduct.stock > 0"
                    @click="confirmMarkAsSold"
                    class="text-white bg-green-500 hover:bg-green-600 rounded-lg px-2 py-1 text-xs flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                    </svg>
                    {{ t('chat.markAsSold') }}
                </button>

                <!-- Close Button -->
                <button @click="$emit('close')" class="text-white hover:text-gray-200 cursor-pointer">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </button>
            </div>
        </div>

        <!-- Product not found warning if applicable -->
        <div v-if="productNotFound" class="bg-yellow-100 text-yellow-800 p-3 text-sm border-b border-yellow-200">
            <div class="flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                </svg>
                {{ t('chat.productNotFound') }}
            </div>
        </div>

        <!-- Chat messages -->
        <div ref="messagesContainer" class="flex-1 p-4 bg-gray-50 overflow-y-auto"
            style="max-height: calc(100% - 120px);">
            <div v-if="isLoading" class="flex justify-center items-center h-full">
                <div class="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
            </div>
            <div v-else-if="errorMessage" class="text-red-500 text-center">
                {{ errorMessage }}
            </div>
            <div v-else-if="!currentChat?.mensajes || currentChat.mensajes.length === 0"
                class="text-gray-500 text-center py-4">
                {{ t('chat.noMessages') }}
            </div>
            <template v-else>
                <div v-for="(message, index) in currentChat.mensajes" :key="index" class="mb-4">
                    <div :class="[
                        'flex max-w-xs rounded-lg p-3 shadow',
                        message.idEmisor === userId ?
                            'ml-auto bg-blue-600 text-white rounded-br-none' :
                            'mr-auto bg-white text-gray-800 rounded-bl-none border border-gray-200'
                    ]">
                        <p>{{ message.mensaje }}</p>
                    </div>
                    <div :class="[
                        'text-xs mt-1',
                        message.idEmisor === userId ? 'text-right text-gray-500' : 'text-left text-gray-500'
                    ]">
                        {{ formatTime(message.fecha || '') }}
                    </div>
                </div>
            </template>
        </div>

        <!-- Message input -->
        <div class="p-3 bg-white border-t border-gray-200">
            <form @submit.prevent="sendMessage" class="flex">
                <input ref="messageInput" v-model="newMessage" type="text"
                    class="flex-1 border border-gray-300 rounded-l-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-300 focus:border-transparent"
                    :placeholder="t('chat.typePlaceholder')" :disabled="isLoading" />
                <button type="button" @click="sendMessage"
                    class="bg-blue-600 hover:bg-blue-700 text-white rounded-r-lg px-4 py-2 flex items-center transition-colors"
                    :disabled="!newMessage.trim() || isLoading"
                    :class="{ 'opacity-50 cursor-not-allowed': !newMessage.trim() || isLoading }">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 rotate-90" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
                    </svg>
                </button>
            </form>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { useChat } from '@/composables/useChat';
import { useProducto } from '@/composables/useProducto';
import { useutf8Store } from '@/stores/counter';
import type { ChatEntity } from '@/models/chat';
import type { Producto } from '@/models/producto';
import Swal from 'sweetalert2';

export default defineComponent({
    name: 'ChatBox',
    props: {
        productId: {
            type: String,
            required: true
        },
        sellerId: {
            type: String,
            required: true
        },
        userId: {
            type: String,
            required: true
        },
        chatId: {
            type: String,
            default: null
        }
    },
    emits: ['close'],
    data() {
        const chatComposable = useChat();
        const productoComposable = useProducto();

        return {
            newMessage: '',
            messagesContainer: null as HTMLElement | null,
            chatComposable,
            productoComposable,
            loading: false,
            error: null as string | null,
            chat: null as ChatEntity | null,
            refreshInterval: null as number | null,
            chatPartnerName: null as string | null,
            currentProduct: null as Producto | null,
            productNotFound: false
        };
    },
    computed: {
        isLoading(): boolean {
            return this.loading || this.chatComposable.loading || this.productoComposable.loading;
        },
        errorMessage(): string | null {
            return this.error || this.chatComposable.error;
        },
        currentChat(): ChatEntity | null {
            return this.chat || this.chatComposable.currentChat;
        },
        isSeller(): boolean {
            return this.userId === this.sellerId;
        }
    },
    methods: {
        async scrollToBottom() {
            await this.$nextTick();
            if (this.$refs.messagesContainer) {
                const container = this.$refs.messagesContainer as HTMLDivElement;
                setTimeout(() => {
                    container.scrollTop = container.scrollHeight;
                }, 50);
            }
        },
        async initializeChat() {
            try {
                this.loading = true;
                this.error = null;

                if (this.chatId) {
                    const response = await this.chatComposable.getChatById(this.chatId);
                    this.chat = response;
                } else {
                    const response = await this.chatComposable.getChatByParticipants(
                        this.productId,
                        this.userId,
                        this.sellerId
                    );
                    this.chat = response;
                }

                await this.$nextTick();

                if (this.chat?.id) {
                    const partnerId = this.isSeller ?
                        this.chat.idComprador :
                        this.chat.idVendedor;

                    const userName = await this.chatComposable.getUserNameById(partnerId);
                    this.chatPartnerName = userName;
                }

                // Load the product information 
                await this.loadProductInfo();

                this.setupAutoRefresh();

                // Focus the input field after chat is initialized
                this.focusInput();
            } catch (err) {
                console.error('Error initializing chat:', err);
                this.error = (err instanceof Error ? err.message : 'Error loading chat messages');
            } finally {
                this.loading = false;
                this.scrollToBottom();
            }
        },
        async loadProductInfo() {
            try {
                if (!this.currentChat) return;

                await this.productoComposable.getProductoById(this.currentChat.idProducto);
                this.currentProduct = this.productoComposable.currentProducto;

                // Set flag if product doesn't exist or is no longer active
                this.productNotFound = !this.currentProduct || !this.currentProduct.activo;
            } catch (err) {
                console.error('Error loading product info:', err);
                this.productNotFound = true;
            }
        },
        focusInput() {
            setTimeout(() => {
                if (this.$refs.messageInput) {
                    (this.$refs.messageInput as HTMLInputElement).focus();
                }
            }, 100);
        },
        setupAutoRefresh() {
            if (this.refreshInterval) {
                clearInterval(this.refreshInterval);
            }

            this.refreshInterval = window.setInterval(async () => {
                if (this.currentChat?.id && !this.newMessage.trim()) {
                    try {
                        const response = await this.chatComposable.getChatById(this.currentChat.id);

                        if (response && response.mensajes &&
                            (!this.currentChat.mensajes || response.mensajes.length > this.currentChat.mensajes.length)) {
                            this.chat = response;
                            this.focusInput();
                        }
                    } catch (err) {
                        console.error('Error al actualizar mensajes:', err);
                    } finally {
                        this.loading = false;
                        await this.scrollToBottom();
                        await this.focusInput();
                    }
                }
            }, 10000);
        },
        async sendMessage() {
            if (!this.newMessage.trim() || !this.currentChat?.id) {
                return;
            }

            try {
                this.loading = true;
                const updatedChat = await this.chatComposable.addMessage(
                    this.currentChat.id,
                    this.userId,
                    this.newMessage.trim()
                );

                this.chat = updatedChat;
                this.newMessage = '';

                // Focus back on input after sending
                this.focusInput();
            } catch (err) {
                console.error('Error enviando mensaje:', err);
                this.error = (err instanceof Error ? err.message : 'Error sending message');
            } finally {
                this.loading = false;
                await this.scrollToBottom();
            }
        },
        confirmDeleteChat() {
            if (!this.currentChat?.id) return;

            Swal.fire({
                title: this.t('chat.delete'),
                text: this.t('chat.delete.confirm'),
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: this.t('common.yes'),
                cancelButtonText: this.t('common.cancel'),
                reverseButtons: true
            }).then(async (result) => {
                if (result.isConfirmed) {
                    await this.deleteChat();
                }
            });
        },
        async deleteChat() {
            if (!this.currentChat?.id) return;

            try {
                this.loading = true;
                await this.chatComposable.deleteChat(this.currentChat.id);

                Swal.fire({
                    icon: 'success',
                    title: this.t('chat.delete.success'),
                    confirmButtonText: this.t('common.ok')
                });

                // Close the chat box
                this.$emit('close');
            } catch (err) {
                console.error('Error deleting chat:', err);
                Swal.fire({
                    icon: 'error',
                    title: this.t('chat.delete.error'),
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },
        confirmMarkAsSold() {
            if (!this.currentChat?.idProducto || !this.isSeller) return;

            Swal.fire({
                title: this.t('chat.markAsSold'),
                text: this.t('chat.markAsSold.confirm'),
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: this.t('common.yes'),
                cancelButtonText: this.t('common.cancel'),
                reverseButtons: true
            }).then(async (result) => {
                if (result.isConfirmed) {
                    await this.markProductAsSold();
                }
            });
        },
        async markProductAsSold() {
            if (!this.currentChat?.idProducto || !this.isSeller) return;

            try {
                this.loading = true;
                await this.productoComposable.markAsSold(this.currentChat.idProducto);

                // Refresh product info
                await this.loadProductInfo();

                // Notify the buyer that the product has been sold by sending a system message
                if (this.currentProduct) {
                    const message = `[${this.t('chat.markAsSold')}] ${this.currentProduct.titulo}`;
                    await this.chatComposable.addMessage(
                        this.currentChat.id!,
                        this.userId,
                        message
                    );
                }

                Swal.fire({
                    icon: 'success',
                    title: this.t('chat.markAsSold.success'),
                    confirmButtonText: this.t('common.ok')
                });
            } catch (err) {
                console.error('Error marking product as sold:', err);
                Swal.fire({
                    icon: 'error',
                    title: this.t('chat.markAsSold.error'),
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },
        formatTime(dateStr: string) {
            if (!dateStr) return '';

            try {
                const store = useutf8Store();
                const userLanguage = store.currentLanguage;

                const date = new Date(dateStr);
                return new Intl.DateTimeFormat(userLanguage || 'es', {
                    hour: 'numeric',
                    minute: 'numeric',
                    year: 'numeric',
                    month: 'short',
                    day: 'numeric'
                }).format(date);
            } catch (error) {
                console.error('Error formatting date:', error);
                return dateStr;
            }
        },
        t(key: string, params?: Record<string, any>) {
            const store = useutf8Store();
            const translation = store.t(key);

            if (params) {
                return Object.entries(params).reduce((str, [key, value]) => {
                    return str.replace(`{${key}}`, String(value));
                }, translation);
            }

            return translation;
        }
    },
    mounted() {
        this.initializeChat();
        this.$nextTick(() => {
            this.scrollToBottom();
        });
    },
    beforeUnmount() {
        if (this.refreshInterval) {
            clearInterval(this.refreshInterval);
            this.refreshInterval = null;
        }
    }
});
</script>

<style scoped>
.chat-box {
    height: 650px;
    width: 100%;
    max-width: 800px;
    margin: 0 auto;
}
</style>