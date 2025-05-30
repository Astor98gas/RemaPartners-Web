<template>
    <div class="chat-box rounded-lg overflow-hidden border border-gray-300 shadow-lg flex flex-col">
        <!-- Cabecera del chat -->
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
                <!-- Botón eliminar chat -->
                <button v-if="productNotFound" @click="confirmDeleteChat"
                    class="text-white bg-red-500 hover:bg-red-600 rounded-lg px-2 py-1 text-xs flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                    {{ t('chat.delete') }}
                </button>

                <!-- Botón marcar como vendido (Solo para vendedores) -->
                <button v-if="isSeller && !productNotFound && currentProduct && currentProduct.stock > 0"
                    @click="confirmMarkAsSold"
                    class="text-white bg-green-500 hover:bg-green-600 rounded-lg px-2 py-1 text-xs flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                    </svg>
                    {{ t('chat.markAsSold') }}
                </button>

                <!-- Botón cerrar -->
                <button @click="$emit('close')" class="text-white hover:text-gray-200 cursor-pointer">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </button>
            </div>
        </div>

        <!-- Advertencia de producto no encontrado si es aplicable -->
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

        <!-- Mensajes del chat -->
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

        <!-- Campo de entrada de mensaje -->
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
import { useFactura } from '@/composables/useFactura';
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
        const facturaComposable = useFactura();

        return {
            newMessage: '',
            messagesContainer: null as HTMLElement | null,
            chatComposable,
            productoComposable,
            facturaComposable,
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
        /**
         * Desplaza el contenedor de mensajes hasta el fondo para mostrar el último mensaje.
         * @returns {Promise<void>}
         */
        async scrollToBottom() {
            await this.$nextTick();
            if (this.$refs.messagesContainer) {
                const container = this.$refs.messagesContainer as HTMLDivElement;
                setTimeout(() => {
                    container.scrollTop = container.scrollHeight;
                }, 50);
            }
        },
        /**
         * Inicializa el chat cargando los mensajes y la información del producto.
         * También obtiene el nombre del usuario con el que se está chateando.
         * @returns {Promise<void>}
         */
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

                // Cargar la información del producto
                await this.loadProductInfo();

                this.setupAutoRefresh();

                // Enfocar el campo de entrada después de inicializar el chat
                this.focusInput();
            } catch (err) {
                console.error('Error inicializando chat:', err);
                this.error = (err instanceof Error ? err.message : 'Error cargando mensajes del chat');
            } finally {
                this.loading = false;
                this.scrollToBottom();
            }
        },
        /**
         * Carga la información del producto asociado al chat actual.
         * Establece una bandera si el producto no existe o no está activo.
         * @returns {Promise<void>}
         */
        async loadProductInfo() {
            try {
                if (!this.currentChat) return;

                await this.productoComposable.getProductoById(this.currentChat.idProducto);
                this.currentProduct = this.productoComposable.currentProducto;

                // Establecer bandera si el producto no existe o ya no está activo
                this.productNotFound = !this.currentProduct || !this.currentProduct.activo;
            } catch (err) {
                console.error('Error cargando información del producto:', err);
                this.productNotFound = true;
            }
        },
        /**
         * Enfoca el campo de entrada de mensaje.
         */
        focusInput() {
            setTimeout(() => {
                if (this.$refs.messageInput) {
                    (this.$refs.messageInput as HTMLInputElement).focus();
                }
            }, 100);
        },
        /**
         * Configura la actualización automática de los mensajes del chat cada 10 segundos.
         */
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
                        console.error('Error actualizando mensajes:', err);
                    } finally {
                        this.loading = false;
                        await this.scrollToBottom();
                        await this.focusInput();
                    }
                }
            }, 10000);
        },
        /**
         * Envía un nuevo mensaje en el chat actual.
         * @returns {Promise<void>}
         */
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

                // Enfocar de nuevo en el campo de entrada después de enviar
                this.focusInput();
            } catch (err) {
                console.error('Error enviando mensaje:', err);
                this.error = (err instanceof Error ? err.message : 'Error enviando mensaje');
            } finally {
                this.loading = false;
                await this.scrollToBottom();
            }
        },
        /**
         * Muestra un cuadro de confirmación para eliminar el chat.
         */
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
        /**
         * Elimina el chat actual de la base de datos.
         * @returns {Promise<void>}
         */
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

                // Cerrar la caja de chat
                this.$emit('close');
            } catch (err) {
                console.error('Error eliminando chat:', err);
                Swal.fire({
                    icon: 'error',
                    title: this.t('chat.delete.error'),
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },
        /**
         * Muestra un cuadro de diálogo para confirmar la venta del producto y seleccionar la cantidad.
         */
        confirmMarkAsSold() {
            if (!this.currentChat?.idProducto || !this.isSeller || !this.currentProduct) return;

            const availableStock = this.currentProduct.stock;

            if (availableStock <= 0) {
                Swal.fire({
                    icon: 'error',
                    title: this.t('common.error'),
                    text: this.t('producto.stock.unavailable'),
                    confirmButtonText: this.t('common.ok')
                });
                return;
            }

            // Crear opciones de selección para la cantidad
            const inputOptions: Record<string, string> = {};
            for (let i = 1; i <= availableStock; i++) {
                inputOptions[i.toString()] = i.toString();
            }

            Swal.fire({
                title: this.t('chat.markAsSold'),
                text: this.t('chat.markAsSold.confirm'),
                input: 'select',
                inputOptions: inputOptions,
                inputLabel: this.t('chat.markAsSold.quantity'),
                inputValue: '1', // Valor por defecto
                showCancelButton: true,
                confirmButtonText: this.t('common.yes'),
                cancelButtonText: this.t('common.cancel'),
                reverseButtons: true,
                inputValidator: (value) => {
                    if (!value) {
                        return 'Necesitas seleccionar una cantidad!';
                    }
                }
            }).then(async (result) => {
                if (result.isConfirmed && result.value) {
                    const quantity = parseInt(result.value);
                    if (!isNaN(quantity) && quantity > 0) {
                        await this.markProductAsSold(quantity);
                    }
                }
            });
        },
        /**
         * Marca el producto como vendido y crea una factura por la venta.
         * También envía un mensaje de sistema notificando la venta.
         * @param {number} quantity - Cantidad de productos vendidos.
         * @returns {Promise<void>}
         */
        async markProductAsSold(quantity = 1) {
            if (!this.currentChat?.idProducto || !this.isSeller) return;

            try {
                this.loading = true;
                await this.productoComposable.markAsSold(this.currentChat.idProducto, quantity);

                // Actualizar información del producto
                await this.loadProductInfo();

                // Crear una factura para esta venta
                if (this.currentChat.id && this.currentProduct) {
                    try {
                        // Crear factura usando el endpoint dedicado
                        await this.facturaComposable.createFromSale(
                            this.currentChat.idProducto,
                            this.currentChat.idComprador,
                            this.currentChat.idVendedor,
                            quantity,
                            this.currentChat.id
                        );

                        console.log('Factura creada exitosamente para la venta');
                    } catch (invoiceErr) {
                        console.error('Error creando factura:', invoiceErr);
                        // No queremos que falle todo el proceso de venta si la creación de factura falla
                        // Solo registrar el error y continuar
                    }
                }

                // Notificar al comprador que el producto ha sido vendido enviando un mensaje del sistema
                if (this.currentProduct) {
                    const message = `[${this.t('chat.markAsSold')}] ${quantity} - ${this.currentProduct.titulo}`;
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
                console.error('Error marcando producto como vendido:', err);
                Swal.fire({
                    icon: 'error',
                    title: this.t('chat.markAsSold.error'),
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },
        /**
         * Da formato a una fecha en función del idioma del usuario.
         * @param {string} dateStr - Fecha en formato string.
         * @returns {string}
         */
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
                console.error('Error formateando fecha:', error);
                return dateStr;
            }
        },
        /**
         * Traduce una clave utilizando el sistema de traducción de la aplicación.
         * @param {string} key - Clave de traducción.
         * @param {Record<string, any>} [params] - Parámetros para interpolar en la traducción.
         * @returns {string}
         */
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