<template>
    <div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <!-- Header con botón de regreso -->
        <div class="flex items-center justify-between mb-8">
            <h1 class="text-3xl font-bold text-gray-900 relative inline-block">
                {{ product ? product.titulo : t('producto.details.loading') }}
                <span
                    class="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r from-blue-500 to-blue-300 rounded-full"></span>
            </h1>
            <router-link to="/"
                class="flex items-center px-4 py-2 text-gray-700 hover:text-orange-600 hover:bg-gray-50 rounded-3xl transition-all duration-200 hover:shadow-md">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                </svg>
                {{ t('producto.add.back') }}
            </router-link>
        </div>

        <!-- Spinner de carga mientras se carga la información -->
        <div v-if="loading" class="flex flex-col justify-center items-center py-20">
            <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-500"></div>
            <p class="text-gray-500 mt-4 text-lg">{{ t('producto.loading') }}</p>
        </div>

        <!-- Error message -->
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

        <!-- Contenido del Producto -->
        <div v-else-if="product" class="bg-white shadow-xl rounded-xl overflow-hidden">
            <!-- Galería de imágenes -->
            <div class="relative">
                <!-- Imagen principal con zoom al hover -->
                <div class="h-96 overflow-hidden bg-gray-50 flex items-center justify-center relative group">
                    <img :src="currentImage" :alt="product.titulo"
                        class="max-w-full max-h-full object-contain transition-transform duration-300 group-hover:scale-105 cursor-zoom-in"
                        @error="onImageError" @click="showLightbox" />

                    <!-- Botones de navegación for móviles -->
                    <button v-if="product.imagenes.length > 1" @click="prevImage"
                        class="absolute left-4 top-1/2 -translate-y-1/2 bg-white/80 rounded-full p-2 shadow-md md:hidden z-10">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-800" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                        </svg>
                    </button>
                    <button v-if="product.imagenes.length > 1" @click="nextImage"
                        class="absolute right-4 top-1/2 -translate-y-1/2 bg-white/80 rounded-full p-2 shadow-md md:hidden z-10">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-800" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                        </svg>
                    </button>
                </div>

                <!-- Miniaturas de imágenes -->
                <div class="p-4 bg-gray-50 flex gap-3 overflow-x-auto scrollbar-hide"
                    v-if="product.imagenes.length > 1">
                    <button v-for="(imagen, index) in product.imagenes" :key="index" @click="selectImage(imagen)"
                        class="flex-shrink-0 w-24 h-24 rounded-lg overflow-hidden border-2 transition-all duration-200 hover:shadow-md cursor-pointer"
                        :class="currentImage === imagen ? 'border-blue-500 shadow-md' : 'border-transparent hover:border-gray-300'">
                        <img :src="imagen" :alt="`${product.titulo} - ${index + 1}`"
                            class="w-full h-full object-cover hover:opacity-90" />
                    </button>
                </div>

                <!-- Badge de stock -->
                <div class="absolute top-3 right-3 flex flex-col gap-2">
                    <span v-if="product.stock > 5"
                        class="bg-green-500 text-white px-3 py-1 rounded-full font-medium shadow-md flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                        </svg>
                        {{ t('producto.inStock') }}
                    </span>
                    <span v-else-if="product.stock > 0"
                        class="bg-amber-500 text-white px-3 py-1 rounded-full font-medium shadow-md flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                        </svg>
                        {{ t('producto.lowStock') }}
                    </span>
                    <span v-else
                        class="bg-red-500 text-white px-3 py-1 rounded-full font-medium shadow-md flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M6 18L18 6M6 6l12 12" />
                        </svg>
                        {{ t('producto.outOfStock') }}
                    </span>
                </div>

                <!-- Estado del producto -->
                <div class="absolute top-3 left-3">
                    <span class="bg-blue-600 text-white px-3 py-1 rounded-full font-medium shadow-md flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                        </svg>
                        {{ t(`producto.estados.${product.estado}`) }}
                    </span>
                </div>
            </div>

            <!-- Información detallada -->
            <div class="p-6 sm:p-8">
                <div class="flex flex-col md:flex-row md:justify-between md:items-center mb-6 gap-4">
                    <h1 class="text-3xl font-bold text-gray-900">{{ product.titulo }}</h1>
                    <div class="text-3xl font-bold text-blue-600 bg-blue-50 px-4 py-2 rounded-lg">
                        {{ (product.precioCentimos / 100).toFixed(2) }}
                        <span class="text-gray-600 text-lg ml-1">{{ product.moneda }}</span>
                    </div>
                </div>

                <!-- Marca y acciones -->
                <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
                    <div class="bg-gray-50 px-4 py-2 rounded-lg">
                        <span class="text-gray-500">{{ t('producto.brand') }}:</span>
                        <span class="ml-2 text-gray-900 font-semibold">{{ product.marca }}</span>
                    </div>
                    <div class="flex gap-3 flex-wrap">
                        <!-- Edit button - replaced with EditButton component -->
                        <RouterLink v-if="isAdmin" :to="`/producto/edit/${product.id}`">
                            <EditButton />
                        </RouterLink>

                        <!-- Delete button - replaced with DeleteButton component -->
                        <div v-if="isAdmin" @click="confirmDeleteProduct">
                            <DeleteButton />
                        </div>

                        <!-- Toggle status button - keep unchanged -->
                        <button v-if="isAdmin" @click="toggleProductStatus"
                            class="px-4 py-2 bg-amber-500 hover:bg-amber-600 text-white rounded-lg transition-all duration-200 flex items-center justify-center shadow-md hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-amber-300 focus:ring-offset-2">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                            </svg>
                            {{ product.activo ? t('producto.action.disable') : t('producto.action.enable') }}
                        </button>

                        <!-- Buy button - modified to check for product ownership -->
                        <button @click="startChat"
                            class="px-6 py-3 bg-gradient-to-r from-green-600 to-green-500 text-white rounded-lg hover:from-green-700 hover:to-green-600 transition-all duration-200 flex items-center shadow-md hover:shadow-lg"
                            :disabled="product.stock <= 0 || isOwner"
                            :class="{ 'opacity-50 cursor-not-allowed grayscale': product.stock <= 0 || isOwner }">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                            </svg>
                            {{ isOwner ? t('producto.action.ownProduct') : t('producto.action.buy') }}
                        </button>
                    </div>
                </div>

                <!-- Descripción -->
                <div class="mb-8 bg-gray-50 p-6 rounded-xl">
                    <h2 class="text-xl font-semibold mb-4 text-gray-900 flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z" />
                        </svg>
                        {{ t('producto.description') }}
                    </h2>
                    <p class="text-gray-700 leading-relaxed whitespace-pre-line">{{ product.descripcion }}</p>
                </div>

                <!-- Información del producto -->
                <div class="mb-8">
                    <h2 class="text-xl font-semibold mb-6 text-gray-900 flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        {{ t('producto.details.title') }}
                    </h2>

                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <!-- Stock -->
                        <div
                            class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100 hover:shadow-md transition-shadow">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.stock') }}</p>
                            <p class="text-gray-900 font-semibold">
                                {{ product.stock > 0
                                    ? t('producto.stock.available').replace('{count}', product.stock.toString())
                                    : t('producto.stock.unavailable') }}
                            </p>
                        </div>

                        <!-- Estado -->
                        <div
                            class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100 hover:shadow-md transition-shadow">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.estado') }}</p>
                            <p class="text-gray-900 font-semibold">{{ t(`producto.estados.${product.estado}`) }}</p>
                        </div>

                        <!-- Fechas -->
                        <div
                            class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100 hover:shadow-md transition-shadow">
                            <p class="text-gray-500 text-sm mb-1">{{ t('common.dates') }}</p>
                            <p class="text-gray-700 text-sm">
                                <span class="font-medium">{{ t('common.createdOn') }}:</span>
                                {{ formatDate(product.fechaCreacion) }}
                            </p>
                            <p class="text-gray-700 text-sm" v-if="product.fechaModificacion">
                                <span class="font-medium">{{ t('common.modifiedOn') }}:</span>
                                {{ formatDate(product.fechaModificacion) }}
                            </p>
                            <p class="text-gray-700 text-sm" v-if="product.fechaPublicacion">
                                <span class="font-medium">{{ t('common.publishedOn') }}:</span>
                                {{ formatDate(product.fechaPublicacion) }}
                            </p>
                        </div>

                        <!-- Ubicación -->
                        <div
                            class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100 hover:shadow-md transition-shadow col-span-1 md:col-span-2 lg:col-span-3">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.direccion') }}</p>
                            <p class="text-gray-900 font-semibold mb-2">{{ product.direccion || t('common.notAvailable')
                            }}</p>

                            <div v-if="product.direccion"
                                class="w-full h-64 rounded-lg border border-gray-300 overflow-hidden shadow-sm mt-2 hover:shadow-md transition-shadow"
                                ref="mapContainer">
                            </div>
                        </div>

                        <!-- Activo -->
                        <div class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100 hover:shadow-md transition-shadow"
                            v-if="isAdmin">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.activo') }}</p>
                            <p class="text-gray-900 font-semibold">
                                <span :class="product.activo ? 'text-green-600' : 'text-red-600'">
                                    {{ product.activo ? t('common.yes') : t('common.no') }}
                                </span>
                            </p>
                        </div>

                        <!-- Destacado -->
                        <div class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100 hover:shadow-md transition-shadow"
                            v-if="isAdmin">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.destacado') }}</p>
                            <p class="text-gray-900 font-semibold">
                                <span :class="product.destacado ? 'text-green-600' : 'text-red-600'">
                                    {{ product.destacado ? t('common.yes') : t('common.no') }}
                                </span>
                            </p>
                        </div>

                    </div>
                </div>

                <!-- Campos específicos de la categoría -->
                <div class="mb-8" v-if="product.camposCategoria && product.camposCategoria.length > 0">
                    <h2 class="text-xl font-semibold mb-6 text-gray-900 flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4" />
                        </svg>
                        {{ t('producto.category.fields') }}
                    </h2>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div v-for="(campo, index) in product.camposCategoria" :key="index"
                            class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100 hover:shadow-md transition-shadow">
                            <p class="text-gray-500 text-sm mb-1">{{ campo.nombreCampo }}</p>
                            <p class="text-gray-900 font-semibold">{{ campo.datos || t('common.notAvailable') }}</p>
                        </div>
                    </div>
                </div>

                <!-- Productos similares -->
                <div class="mb-8" v-if="productosSimilares.length > 0">
                    <h2 class="text-xl font-semibold mb-6 text-gray-900 flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h18v18H3V3z" />
                        </svg>
                        {{ t('producto.similarProducts') }}
                    </h2>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <ProductoComponent v-for="productoSim in productosSimilares" :key="productoSim.id"
                            :producto="productoSim" />
                    </div>
                </div>
            </div>
        </div>

        <!-- Chat Modal -->
        <div v-if="showChat && product"
            class="fixed inset-0 bg-purple-800/10 flex items-center justify-center z-50 p-4">

            <!-- Login required message -->
            <div v-if="!currentUser" class="p-8 text-center">
                <div class="bg-white rounded-xl shadow-2xl w-full max-w-2xl">
                    <div class="relative">
                        <!-- Close button -->
                        <button @click="showChat = false"
                            class="absolute top-4 right-4 text-gray-500 hover:text-gray-700">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-blue-500 mb-4" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                        </svg>
                        <h3 class="text-xl font-semibold mb-2">{{ t('chat.loginRequired') }}</h3>
                        <p class="text-gray-600 mb-6">{{ t('auth.login_required') }}</p>
                        <div class="flex justify-center">
                            <router-link to="/login"
                                class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                                {{ t('login.login') }}
                            </router-link>
                        </div>

                    </div>

                </div>
            </div>
            <!-- Chat component -->
            <div v-else class="p-4 w-full">
                <ChatBox :product-id="product.id" :seller-id="product.idUsuario" :user-id="currentUser.id"
                    :chat-id="activeChatId" @close="showChat = false" />
            </div>
        </div>

        <!-- Offer Modal -->
        <OfferModal v-if="showOfferModal && product" :product-title="product.titulo" :product-image="currentImage"
            :original-price="product.precioCentimos" :currency="product.moneda" @close="showOfferModal = false"
            @offer-submitted="handleOfferSubmitted" />

        <vue-easy-lightbox :visible="lightboxVisible" :imgs="product?.imagenes || []" :index="currentImageIndex"
            @hide="lightboxVisible = false" :scroll-disabled="true" />
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue';
import { useProducto } from '@/composables/useProducto';
import { useUsers } from '@/composables/useUsers';
import { useChat } from '@/composables/useChat';
import { useutf8Store } from '@/stores/counter';
import EditButton from '@/components/ui/EditButton.vue';
import DeleteButton from '@/components/ui/DeleteButton.vue';
import ChatBox from '@/components/chat/ChatBox.vue';
import OfferModal from '@/components/features/productos/OfferModal.vue';
import Swal from 'sweetalert2';
import 'leaflet/dist/leaflet.css';
import * as L from 'leaflet';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import type { Producto } from '@/models/producto';
import ProductoComponent from '@/components/layout/ProductoComponent.vue';
import VueEasyLightbox from 'vue-easy-lightbox';

export default defineComponent({
    name: 'ProductoDetailView',
    components: {
        EditButton,
        DeleteButton,
        ChatBox,
        OfferModal,
        ProductoComponent,
        VueEasyLightbox
    },
    setup() {
        const mapContainer = ref(null);
        const map = ref<L.Map | null>(null);

        return {
            mapContainer,
            map
        };
    },
    data() {
        return {
            product: null as Producto | null,
            loading: true,
            error: null as string | null,
            isAdmin: false,
            currentImage: '',
            currentImageIndex: 0,
            showChat: false,
            showOfferModal: false,
            activeChatId: undefined as string | undefined,
            currentUser: null as { id: string } | null,
            isLoggedIn: false,
            isOwner: false,
            productosSimilares: [] as Producto[],
            lightboxVisible: false,
        };
    },
    computed: {
        isOwner(): boolean {
            return !!this.currentUser && !!this.product && this.currentUser.id === this.product.idUsuario;
        }
    },
    async mounted() {
        const productoId = this.$route.params.id as string;
        await this.loadProductData(productoId);
    },
    onBeforeUnmount() {
        if (this.map) {
            (this.map as L.Map).remove();
            this.map = null;
        }
    },
    watch: {
        '$route.params.id': {
            immediate: false,
            handler(newId, oldId) {
                if (newId && newId !== oldId) {
                    // Reinicia el estado
                    this.loading = true;
                    this.error = null;
                    this.product = null;
                    this.productosSimilares = [];

                    // Recarga los datos del producto
                    this.loadProductData(newId);
                }
            }
        }
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },
        onImageError(event: Event) {
            (event.target as HTMLImageElement).src = new URL('@/assets/logoCuadrado.jpeg', import.meta.url).href;
        },
        selectImage(image: string) {
            this.currentImage = image;
            if (this.product?.imagenes) {
                this.currentImageIndex = this.product.imagenes.indexOf(image);
            }
        },
        nextImage() {
            if (this.product && this.product.imagenes.length > 0) {
                this.currentImageIndex = (this.currentImageIndex + 1) % this.product.imagenes.length;
                this.currentImage = this.product.imagenes[this.currentImageIndex];
            }
        },
        prevImage() {
            if (this.product && this.product.imagenes.length > 0) {
                this.currentImageIndex = (this.currentImageIndex - 1 + this.product.imagenes.length) % this.product.imagenes.length;
                this.currentImage = this.product.imagenes[this.currentImageIndex];
            }
        },
        formatDate(dateStr: string): string {
            if (!dateStr) return this.t('common.notAvailable');

            try {
                const date = new Date(dateStr);
                return new Intl.DateTimeFormat(document.documentElement.lang || 'es', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                }).format(date);
            } catch (error) {
                console.error('Error formatting date:', error);
                return dateStr;
            }
        },
        async toggleProductStatus() {
            if (!this.product) return;

            try {
                const productoService = useProducto();
                await productoService.toggleStatus(this.product.id);
                this.product.activo = !this.product.activo;

                Swal.fire({
                    icon: 'success',
                    title: this.t('common.success'),
                    text: this.product.activo
                        ? this.t('producto.status.activated')
                        : this.t('producto.status.deactivated'),
                    confirmButtonText: this.t('common.ok')
                });

            } catch (error) {
                console.error('Error toggling product status:', error);
                Swal.fire({
                    icon: 'error',
                    title: this.t('common.error'),
                    text: this.t('producto.status.toggleError'),
                    confirmButtonText: this.t('common.ok')
                });
            }
        },
        async confirmDeleteProduct() {
            if (!this.product) return;

            try {
                const result = await Swal.fire({
                    title: this.t('producto.delete.confirmTitle'),
                    text: this.t('producto.delete.confirmText'),
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    cancelButtonColor: '#3085d6',
                    confirmButtonText: this.t('producto.delete.confirmButton'),
                    cancelButtonText: this.t('common.cancel')
                });

                if (result.isConfirmed) {
                    await this.deleteProduct();
                }
            } catch (error) {
                console.error('Error in delete confirmation:', error);
            }
        },
        async deleteProduct() {
            if (!this.product) return;

            try {
                const productoService = useProducto();
                await productoService.deleteProducto(this.product.id);

                Swal.fire({
                    icon: 'success',
                    title: this.t('producto.delete.success'),
                    text: this.t('producto.delete.successMessage'),
                    confirmButtonText: this.t('common.ok')
                }).then(() => {
                    // Navigate to home page after successful deletion
                    this.$router.push('/');
                });
            } catch (error) {
                console.error('Error deleting product:', error);
                Swal.fire({
                    icon: 'error',
                    title: this.t('common.error'),
                    text: this.t('producto.delete.errorMessage'),
                    confirmButtonText: this.t('common.ok')
                });
            }
        },
        async initMap() {
            if (!this.product?.direccion || !this.mapContainer) return;

            // Configure Leaflet default icons
            delete (L.Icon.Default.prototype as any)._getIconUrl;
            L.Icon.Default.mergeOptions({
                iconRetinaUrl: markerIcon2x,
                iconUrl: markerIcon,
                shadowUrl: markerShadow,
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });

            // Initialize map
            if (this.map) {
                (this.map as L.Map).remove();
                this.map = null;
            }

            this.map = L.map(this.mapContainer, {
                zoomControl: true,
                zoom: 12,
                center: [40.416775, -3.703790] // Default coordinates (Madrid)
            });

            // Add tile layer
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; OpenStreetMap contributors'
            }).addTo(this.map as L.Map);

            // Geocode the address and center map
            await this.geocodeAddress();
        },
        async geocodeAddress() {
            if (!this.product?.direccion) return;

            try {
                const response = await fetch(
                    `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(this.product.direccion)}&limit=1`
                );
                const data = await response.json();

                if (data && data.length > 0) {
                    const { lat, lon } = data[0];
                    const latitude = parseFloat(lat);
                    const longitude = parseFloat(lon);

                    if (this.map) {
                        (this.map as L.Map).setView([latitude, longitude], 13);
                        // Add marker
                        L.marker([latitude, longitude])
                            .addTo(this.map as L.Map)
                            .bindPopup(this.product.direccion)
                            .openPopup();
                    }
                }
            } catch (error) {
                console.error('Error geocoding address:', error);
            }
        },
        startChat() {
            // Show offer modal first instead of directly opening chat
            if (!this.currentUser) {
                // If user is not logged in, show the chat modal which will display login message
                this.showChat = true;
                return;
            }

            // If user is the owner of the product, don't allow to make an offer
            if (this.isOwner) {
                Swal.fire({
                    icon: 'info',
                    title: this.t('producto.action.ownProduct'),
                    text: this.t('producto.action.cantBuyOwn'),
                    confirmButtonText: this.t('common.ok')
                });
                return;
            }

            // Show offer modal first
            this.showOfferModal = true;
        },
        /**
         * Maneja el proceso cuando un usuario envía una oferta por un producto.
         * Este método:
         * - Cierra el modal de oferta
         * - Crea o recupera un chat entre el comprador y el vendedor
         * - Envía un mensaje automático con la oferta propuesta
         * - Muestra el chat para continuar la conversación
         * 
         * @param offerDetails - Objeto con los detalles de la oferta: amount (cantidad), currency (moneda) y formattedOffer (oferta formateada)
         */
        async handleOfferSubmitted(offerDetails: { amount: number, currency: string, formattedOffer: string }) {
            // Comentado: console.log de detalles de oferta
            // console.log('Offer submitted:', offerDetails);
            this.showOfferModal = false;

            if (!this.currentUser || !this.product) return;

            try {
                // Create a chat or get existing chat
                const chatService = useChat();
                const chat = await chatService.getChatByParticipants(
                    this.product.id,
                    this.currentUser.id,
                    this.product.idUsuario
                );

                if (chat && chat.id) {
                    // Set the active chat ID
                    this.activeChatId = chat.id;

                    // Prepare the offer message
                    const offerMessage = this.t('producto.offerMessage')
                        .replace('{productTitle}', this.product.titulo)
                        .replace('{offerAmount}', offerDetails.formattedOffer);

                    // Send the offer message
                    await chatService.addMessage(
                        chat.id,
                        this.currentUser.id,
                        offerMessage
                    );

                    // Show the chat after sending the offer
                    this.showChat = true;
                } else {
                    // Error handling if chat creation failed
                    Swal.fire({
                        icon: 'error',
                        title: this.t('chat.error'),
                        text: this.t('chat.createError'),
                        confirmButtonText: this.t('common.ok')
                    });
                }
            } catch (error) {
                console.error('Error creating chat with offer:', error);
                Swal.fire({
                    icon: 'error',
                    title: this.t('common.error'),
                    text: this.t('chat.createError'),
                    confirmButtonText: this.t('common.ok')
                });
            }
        },
        async getProductosByIdCategoria(idCategoria: string) {
            if (!idCategoria || !this.product) return;

            try {
                const productoService = useProducto();
                // Using regular getProductos method since we don't see a specific idCategoria method
                await productoService.getProductos();

                // Filter products to only include those:
                // 1. From the same category
                // 2. That are not the current product
                // 3. That are active (optional, but recommended)
                // 4. Limit to a reasonable number (e.g., 3-6)
                this.productosSimilares = productoService.productos.value
                    .filter(producto =>
                        producto.idCategoria === idCategoria &&
                        producto.id !== this.product?.id &&
                        producto.activo === true
                    )
                    .slice(0, 3); // Limit to 3 similar products
            } catch (error) {
                console.error('Error loading similar products:', error);
                this.productosSimilares = [];
            }
        },
        async loadProductData(productoId: string) {
            if (!productoId) {
                this.error = 'No product ID provided';
                this.loading = false;
                return;
            }

            try {
                // Check if user is admin and get current user
                const usersComposable = useUsers();
                const userData = await usersComposable.isLoggedIn();
                this.isAdmin = userData?.rol?.name === 'ADMIN';
                this.currentUser = userData;
                this.isLoggedIn = !!userData;

                // Get product details
                const productoService = useProducto();
                await productoService.getProductoById(productoId);
                this.product = productoService.currentProducto.value;

                if (this.product && this.product.imagenes && this.product.imagenes.length > 0) {
                    this.currentImage = this.product.imagenes[0];
                    this.currentImageIndex = 0;
                }

                if (!this.isAdmin) {
                    if (userData?.id == this.product?.idUsuario) {
                        this.isAdmin = true;
                    } else {
                        this.isAdmin = false;
                    }
                }

                this.isOwner = userData?.id === this.product?.idUsuario;

                this.error = null;

                if (this.product) {
                    await this.getProductosByIdCategoria(this.product.idCategoria);
                }

                // Initialize map after product loads and only if there's a location
                if (this.product && this.product.direccion) {
                    this.$nextTick(() => {
                        setTimeout(() => this.initMap(), 300);
                    });
                }
            } catch (err: any) {
                console.error('Error al cargar el producto:', err);
                this.error = err.response?.data?.message || 'Error al cargar los datos del producto';

                Swal.fire({
                    icon: 'error',
                    title: this.t('common.error'),
                    text: this.error || '',
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },
        showLightbox() {
            if (this.product && this.product.imagenes && this.product.imagenes.length > 0) {
                this.lightboxVisible = true;
            }
        },
    }
});
</script>

<style scoped>
.scrollbar-hide::-webkit-scrollbar {
    display: none;
}

.scrollbar-hide {
    -ms-overflow-style: none;
    scrollbar-width: none;
}

.group:hover .group-hover\:scale-105 {
    transform: scale(1.05);
}

.hover\:shadow-lg:hover {
    box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}
</style>