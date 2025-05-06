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
            <div v-if="loading" class="flex justify-center items-center h-full">
                <div class="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
            </div>
            <div v-else-if="error" class="text-red-500 text-center">
                {{ error }}
            </div>
            <div v-else-if="!chat?.mensajes || chat.mensajes.length === 0" class="text-gray-500 text-center py-4">
                {{ t('chat.noMessages') }}
            </div>
            <template v-else>
                <div v-for="(message, index) in chat.mensajes" :key="index" class="mb-4">
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
                        {{ formatTime(message.fecha) }}
                    </div>
                </div>
            </template>
        </div>

        <!-- Message input -->
        <div class="p-3 bg-white border-t border-gray-200">
            <form @submit.prevent="sendMessage" class="flex">
                <input v-model="newMessage" type="text"
                    class="flex-1 border border-gray-300 rounded-l-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-300 focus:border-transparent"
                    :placeholder="t('chat.typePlaceholder')" :disabled="loading" />
                <button type="button" @click="sendMessage"
                    class="bg-blue-600 hover:bg-blue-700 text-white rounded-r-lg px-4 py-2 flex items-center transition-colors"
                    :disabled="!newMessage.trim() || loading"
                    :class="{ 'opacity-50 cursor-not-allowed': !newMessage.trim() || loading }">
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
import { defineComponent, ref, onMounted, onUnmounted, nextTick, watch, computed } from 'vue';
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
    setup(props) {
        const newMessage = ref('');
        const messagesContainer = ref<HTMLElement | null>(null);
        const chatComposable = useChat();
        const loading = ref(false);
        const error = ref<string | null>(null);
        const chat = ref<ChatEntity | null>(null);
        const refreshInterval = ref<number | null>(null);

        // Scroll to the bottom of the messages container
        const scrollToBottom = async () => {
            await nextTick();
            if (messagesContainer.value) {
                messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
            }
        };

        // Initialize chat
        const initializeChat = async () => {
            try {
                loading.value = true;
                error.value = null;

                console.log("Inicializando chat con props:", {
                    chatId: props.chatId,
                    productId: props.productId,
                    userId: props.userId,
                    sellerId: props.sellerId
                });

                // Si tenemos un ID de chat, cargar directamente por ID
                if (props.chatId) {
                    console.log("Cargando chat por ID:", props.chatId);
                    const response = await chatComposable.getChatById(props.chatId);
                    console.log("Chat cargado correctamente:", response);
                    chat.value = response;
                } else {
                    // Si no, obtener o crear chat usando los IDs de participantes
                    console.log("Obteniendo chat por participantes");
                    const response = await chatComposable.getChatByParticipants(
                        props.productId,
                        props.userId,
                        props.sellerId
                    );
                    chat.value = response;
                }

                console.log("Estado final del chat:", chat.value);
                await scrollToBottom();

                // Configurar intervalo de actualización automática
                setupAutoRefresh();
            } catch (err: any) {
                console.error('Error initializing chat:', err);
                error.value = err.message || 'Error loading chat messages';
            } finally {
                loading.value = false;
            }
        };

        // Configurar actualización automática de mensajes
        const setupAutoRefresh = () => {
            // Limpiar intervalo anterior si existe
            if (refreshInterval.value) {
                clearInterval(refreshInterval.value);
            }

            // Actualizar mensajes cada 10 segundos
            refreshInterval.value = window.setInterval(async () => {
                if (chat.value?.id) {
                    try {
                        const response = await chatComposable.getChatById(chat.value.id);

                        // Solo actualizar si hay cambios en la cantidad de mensajes
                        if (response && response.mensajes &&
                            (!chat.value.mensajes || response.mensajes.length > chat.value.mensajes.length)) {
                            chat.value = response;
                            await scrollToBottom();
                        }
                    } catch (err) {
                        console.error('Error al actualizar mensajes:', err);
                    }
                }
            }, 10000);
        };

        // Send a new message
        const sendMessage = async () => {
            if (!newMessage.value.trim() || !chat.value?.id) {
                console.log("No se puede enviar: mensaje vacío o chat nulo", {
                    messageEmpty: !newMessage.value.trim(),
                    chatId: chat.value?.id
                });
                return;
            }

            try {
                console.log("Intentando enviar mensaje:", {
                    chatId: chat.value.id,
                    userId: props.userId,
                    message: newMessage.value.trim()
                });

                loading.value = true;
                const updatedChat = await chatComposable.addMessage(
                    chat.value.id,
                    props.userId,
                    newMessage.value.trim()
                );

                console.log("Respuesta del servidor:", updatedChat);

                // Actualizar el chat con los datos más recientes del servidor
                chat.value = updatedChat;

                // Limpiar el campo de mensaje
                newMessage.value = '';

                // Desplazarse al final para mostrar el mensaje más reciente
                await scrollToBottom();
            } catch (err: any) {
                console.error('Error enviando mensaje:', err);
                error.value = err.message || 'Error sending message';
            } finally {
                loading.value = false;
            }
        };

        // Format time for display
        const formatTime = (dateStr?: string): string => {
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
        };

        // Handle automatic scrolling when new messages arrive
        watch(() => chatComposable.currentChat.value?.mensajes?.length, (newVal, oldVal) => {
            if (newVal && oldVal && newVal > oldVal) {
                scrollToBottom();
            }
        });

        onMounted(() => {
            initializeChat();
        });

        // Limpieza cuando el componente se desmonta
        onUnmounted(() => {
            // Detener el intervalo de actualización automática
            if (refreshInterval.value) {
                clearInterval(refreshInterval.value);
                refreshInterval.value = null;
            }
        });

        return {
            newMessage,
            messagesContainer,
            loading: computed(() => loading.value || chatComposable.loading.value),
            error: computed(() => error.value || chatComposable.error.value),
            chat: computed(() => chat.value || chatComposable.currentChat.value),
            sendMessage,
            formatTime
        };
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
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