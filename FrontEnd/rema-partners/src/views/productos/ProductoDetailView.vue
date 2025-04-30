<template>
    <div class="max-w-[90%] mx-auto py-8 px-4">
        <!-- Header con botón de regreso -->
        <div class="flex items-center justify-between mb-8">
            <h1 class="text-3xl font-bold text-gray-800 relative">
                {{ product ? product.titulo : t('producto.details.loading') }}
                <span class="absolute bottom-0 left-0 w-24 h-1 bg-blue-500 rounded-full"></span>
            </h1>
            <router-link to="/"
                class="flex items-center px-4 py-2 text-gray-600 hover:text-gray-800 hover:bg-gray-100 hover:shadow-md rounded-lg transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                </svg>
                {{ t('producto.add.back') }}
            </router-link>
        </div>

        <!-- Spinner de carga mientras se carga la información -->
        <div v-if="loading" class="flex justify-center items-center py-20">
            <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-500"></div>
            <p class="text-gray-500 ml-4 text-lg">{{ t('producto.loading') }}</p>
        </div>

        <!-- Error message -->
        <div v-else-if="error" class="bg-red-50 border border-red-200 text-red-700 p-6 rounded-xl mb-8">
            <p class="font-semibold text-lg">{{ t('common.error') }}</p>
            <p>{{ error }}</p>
        </div>

        <!-- Contenido del Producto -->
        <div v-else-if="product" class="bg-white shadow-lg rounded-xl overflow-hidden">
            <!-- Galería de imágenes -->
            <div class="relative">
                <div class="h-96 overflow-hidden bg-gray-100">
                    <img :src="currentImage" :alt="product.titulo" class="w-full h-full object-contain"
                        @error="onImageError" />
                </div>

                <!-- Miniaturas de imágenes -->
                <div class="p-4 bg-gray-50 flex gap-2 overflow-x-auto" v-if="product.imagenes.length > 1">
                    <button v-for="(imagen, index) in product.imagenes" :key="index" @click="selectImage(imagen)"
                        class="w-20 h-20 rounded-md overflow-hidden border-2 transition-all duration-300"
                        :class="currentImage === imagen ? 'border-blue-500 shadow-md scale-105' : 'border-transparent'">
                        <img :src="imagen" :alt="`${product.titulo} - ${index + 1}`"
                            class="w-full h-full object-cover" />
                    </button>
                </div>

                <!-- Badge de stock -->
                <div class="absolute top-3 right-3">
                    <span v-if="product.stock > 5" class="bg-green-500 text-white px-3 py-1 rounded-full font-medium">
                        {{ t('producto.inStock') }}
                    </span>
                    <span v-else-if="product.stock > 0"
                        class="bg-amber-500 text-white px-3 py-1 rounded-full font-medium">
                        {{ t('producto.lowStock') }}
                    </span>
                    <span v-else class="bg-red-500 text-white px-3 py-1 rounded-full font-medium">
                        {{ t('producto.outOfStock') }}
                    </span>
                </div>

                <!-- Estado del producto -->
                <div class="absolute top-3 left-3">
                    <span class="bg-blue-600 text-white px-3 py-1 rounded-full font-medium">
                        {{ t(`producto.estados.${product.estado}`) }}
                    </span>
                </div>
            </div>

            <!-- Información detallada -->
            <div class="p-8">
                <div class="flex flex-col md:flex-row md:justify-between md:items-center mb-6">
                    <h1 class="text-3xl font-bold text-gray-800 mb-4 md:mb-0">{{ product.titulo }}</h1>
                    <div class="text-3xl font-bold text-blue-600">
                        {{ (product.precioCentimos / 100).toFixed(2) }}
                        <span class="text-gray-600 text-lg ml-1">{{ product.moneda }}</span>
                    </div>
                </div>

                <!-- Marca y acciones -->
                <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-8">
                    <div class="mb-4 md:mb-0">
                        <span class="text-gray-500">{{ t('producto.brand') }}:</span>
                        <span class="ml-2 text-gray-900 font-semibold">{{ product.marca }}</span>
                    </div>
                    <div class="flex gap-3">
                        <button v-if="isAdmin" @click="toggleProductStatus"
                            class="px-4 py-2 bg-amber-500 text-white rounded-lg hover:bg-amber-600 transition-all duration-300 flex items-center justify-center focus:outline-none focus:ring-2 focus:ring-amber-300">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                            </svg>
                            {{ product.activo ? t('producto.action.disable') : t('producto.action.enable') }}
                        </button>
                        <button
                            class="px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-all duration-300 flex items-center shadow-md"
                            :disabled="product.stock <= 0"
                            :class="{ 'opacity-50 cursor-not-allowed': product.stock <= 0 }">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                            </svg>
                            {{ t('producto.action.buy') }}
                        </button>
                    </div>
                </div>

                <!-- Descripción -->
                <div class="mb-8">
                    <h2 class="text-xl font-semibold mb-4 text-gray-800 flex items-center">
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
                    <h2 class="text-xl font-semibold mb-6 text-gray-800 flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        {{ t('producto.details.title') }}
                    </h2>

                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <!-- Stock -->
                        <div class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.stock') }}</p>
                            <p class="text-gray-900 font-semibold">
                                {{ product.stock > 0
                                    ? t('producto.stock.available').replace('{count}', product.stock.toString())
                                    : t('producto.stock.unavailable') }}
                            </p>
                        </div>

                        <!-- Estado -->
                        <div class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.estado') }}</p>
                            <p class="text-gray-900 font-semibold">{{ t(`producto.estados.${product.estado}`) }}</p>
                        </div>

                        <!-- Ubicación -->
                        <div
                            class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100 col-span-1 md:col-span-2 lg:col-span-3">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.direccion') }}</p>
                            <p class="text-gray-900 font-semibold mb-2">{{ product.direccion || t('common.notAvailable')
                            }}</p>

                            <div v-if="product.direccion"
                                class="w-full h-64 rounded-lg border border-gray-300 overflow-hidden shadow-sm mt-2"
                                ref="mapContainer">
                            </div>
                        </div>

                        <!-- Activo -->
                        <div class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100" v-if="isAdmin">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.activo') }}</p>
                            <p class="text-gray-900 font-semibold">
                                <span :class="product.activo ? 'text-green-600' : 'text-red-600'">
                                    {{ product.activo ? t('common.yes') : t('common.no') }}
                                </span>
                            </p>
                        </div>

                        <!-- Destacado -->
                        <div class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100" v-if="isAdmin">
                            <p class="text-gray-500 text-sm mb-1">{{ t('producto.destacado') }}</p>
                            <p class="text-gray-900 font-semibold">
                                <span :class="product.destacado ? 'text-green-600' : 'text-red-600'">
                                    {{ product.destacado ? t('common.yes') : t('common.no') }}
                                </span>
                            </p>
                        </div>

                        <!-- Fechas -->
                        <div class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100" v-if="isAdmin">
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
                    </div>
                </div>

                <!-- Campos específicos de la categoría -->
                <div class="mb-8" v-if="product.camposCategoria && product.camposCategoria.length > 0">
                    <h2 class="text-xl font-semibold mb-6 text-gray-800 flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4" />
                        </svg>
                        {{ t('producto.category.fields') }}
                    </h2>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div v-for="(campo, index) in product.camposCategoria" :key="index"
                            class="bg-blue-50 p-4 rounded-lg shadow-sm border border-blue-100">
                            <p class="text-gray-500 text-sm mb-1">{{ campo.nombreCampo }}</p>
                            <p class="text-gray-900 font-semibold">{{ campo.datos || t('common.notAvailable') }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { useProducto } from '@/composables/useProducto';
import { useUsers } from '@/composables/useUsers';
import { useutf8Store } from '@/stores/counter';
import Swal from 'sweetalert2';
import 'leaflet/dist/leaflet.css';
import * as L from 'leaflet';

// Leaflet marker images
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';

export default defineComponent({
    name: 'ProductoDetailView',
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
            product: null as any,
            loading: true,
            error: null as string | null,
            isAdmin: false,
            currentImage: '',
        };
    },
    async mounted() {
        const productoId = this.$route.params.id as string;
        if (!productoId) {
            this.error = 'No product ID provided';
            this.loading = false;
            return;
        }

        try {
            // Check if user is admin
            const usersComposable = useUsers();
            const userData = await usersComposable.isLoggedIn();
            this.isAdmin = userData?.rol?.name === 'ADMIN';

            // Get product details
            const productoService = useProducto();
            await productoService.getProductoById(productoId);
            this.product = productoService.currentProducto.value;

            if (this.product && this.product.imagenes && this.product.imagenes.length > 0) {
                this.currentImage = this.product.imagenes[0];
            }

            this.error = null;

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
    onBeforeUnmount() {
        if (this.map) {
            (this.map as L.Map).remove();
            this.map = null;
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
        }
    }
});
</script>