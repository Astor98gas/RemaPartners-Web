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
                <h3 class="font-semibold">{{ t('chat.title') }}</h3>
            </div>
            <button @click="$emit('close')" class="text-white hover:text-gray-200">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
            </button>
        </div>

        <!-- Chat messages -->
        <div ref="messagesContainer" class="flex-1 p-4 bg-gray-50 overflow-y-auto max-h-96">
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
                <input v-model="newMessage" type="text"
                    class="flex-1 border border-gray-300 rounded-l-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-300 focus:border-transparent"
                    :placeholder="t('chat.typePlaceholder')" :disabled="isLoading" />
                <button type="button" @click="sendMessage"
                    class="bg-blue-600 hover:bg-blue-700 text-white rounded-r-lg px-4 py-2 flex items-center transition-colors"
                    :disabled="!newMessage.trim() || isLoading"
                    :class="{ 'opacity-50 cursor-not-allowed': !newMessage.trim() || isLoading }">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
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
import { useutf8Store } from '@/stores/counter';
import type { ChatEntity } from '@/models/chat';

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
        return {
            newMessage: '',
            messagesContainer: null,
            chatComposable: useChat(),
            loading: false,
            error: null as string | null,
            chat: null,
            refreshInterval: null as number | null
        };
    },
    computed: {
        isLoading(): boolean {
            return this.loading || this.chatComposable.loading;
        },
        errorMessage(): string | null {
            return this.error || this.chatComposable.error;
        },
        currentChat(): ChatEntity | null {
            return this.chat || this.chatComposable.currentChat;
        }
    },
    watch: {
        ['chatComposable.currentChat.value.mensajes.length'](newVal, oldVal) {
            if (newVal && oldVal && newVal > oldVal) {
                this.scrollToBottom();
            }
        }
    },
    methods: {
        async scrollToBottom() {
            await this.$nextTick();
            if (this.$refs.messagesContainer) {
                (this.$refs.messagesContainer as HTMLDivElement).scrollTop = (this.$refs.messagesContainer as HTMLDivElement).scrollHeight;
            }
        },
        async initializeChat() {
            try {
                this.loading = true;
                this.error = null;

                console.log("Inicializando chat con props:", {
                    chatId: this.chatId,
                    productId: this.productId,
                    userId: this.userId,
                    sellerId: this.sellerId
                });

                // Si tenemos un ID de chat, cargar directamente por ID
                if (this.chatId) {
                    console.log("Cargando chat por ID:", this.chatId);
                    const response = await this.chatComposable.getChatById(this.chatId);
                    console.log("Chat cargado correctamente:", response);
                    this.chat = response;
                } else {
                    // Si no, obtener o crear chat usando los IDs de participantes
                    console.log("Obteniendo chat por participantes");
                    const response = await this.chatComposable.getChatByParticipants(
                        this.productId,
                        this.userId,
                        this.sellerId
                    );
                    this.chat = response;
                }

                console.log("Estado final del chat:", this.chat);
                await this.scrollToBottom();

                // Configurar intervalo de actualización automática
                this.setupAutoRefresh();
            } catch (err) {
                console.error('Error initializing chat:', err);
                this.error = (err instanceof Error ? err.message : 'Error loading chat messages');
            } finally {
                this.loading = false;
            }
        },
        setupAutoRefresh() {
            // Limpiar intervalo anterior si existe
            if (this.refreshInterval) {
                clearInterval(this.refreshInterval);
            }

            // Actualizar mensajes cada 10 segundos
            this.refreshInterval = window.setInterval(async () => {
                if (this.currentChat?.id) {
                    try {
                        const response = await this.chatComposable.getChatById(this.currentChat.id);

                        // Solo actualizar si hay cambios en la cantidad de mensajes
                        if (response && response.mensajes &&
                            (!this.currentChat.mensajes || response.mensajes.length > this.currentChat.mensajes.length)) {
                            this.chat = response;
                            await this.scrollToBottom();
                        }
                    } catch (err) {
                        console.error('Error al actualizar mensajes:', err);
                    }
                }
            }, 10000);
        },
        async sendMessage() {
            if (!this.newMessage.trim() || !this.currentChat?.id) {
                console.log("No se puede enviar: mensaje vacío o chat nulo", {
                    messageEmpty: !this.newMessage.trim(),
                    chatId: this.currentChat?.id
                });
                return;
            }

            try {
                console.log("Intentando enviar mensaje:", {
                    chatId: this.currentChat.id,
                    userId: this.userId,
                    message: this.newMessage.trim()
                });

                this.loading = true;
                const updatedChat = await this.chatComposable.addMessage(
                    this.currentChat.id,
                    this.userId,
                    this.newMessage.trim()
                );

                console.log("Respuesta del servidor:", updatedChat);

                // Actualizar el chat con los datos más recientes del servidor
                this.chat = updatedChat;

                // Limpiar el campo de mensaje
                this.newMessage = '';

                // Desplazarse al final para mostrar el mensaje más reciente
                await this.scrollToBottom();
            } catch (err) {
                console.error('Error enviando mensaje:', err);
                this.error = (err instanceof Error ? err.message : 'Error sending message');
            } finally {
                this.loading = false;
            }
        },
        formatTime(dateStr: string) {
            if (!dateStr) return '';

            try {
                const store = useutf8Store();
                const userLanguage = store.currentLanguage; // Obtener el idioma del usuario desde el store

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
        t(key: string) {
            const store = useutf8Store();
            return store.t(key);
        }
    },
    mounted() {
        this.initializeChat();
    },
    beforeUnmount() {
        // Detener el intervalo de actualización automática
        if (this.refreshInterval) {
            clearInterval(this.refreshInterval);
            this.refreshInterval = null;
        }
    }
});
</script>

<style scoped>
.chat-box {
    height: 500px;
    width: 100%;
    max-width: 600px;
    margin: 0 auto;
}
</style>