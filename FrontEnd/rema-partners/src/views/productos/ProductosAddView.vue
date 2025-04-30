<template>
    <div class="max-w-[80%] mx-auto py-8 px-4">
        <!-- Header con paso actual -->
        <div class="flex items-center justify-between mb-8">
            <h1 class="text-3xl font-bold text-gray-800">
                {{ t('producto.add.title') }} - {{ t(`producto.add.step${currentStep}`) }}
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

        <!-- Indicador de progreso mejorado -->
        <div class="mb-8">
            <div class="flex items-center justify-between relative">
                <div v-for="step in 4" :key="step"
                    class="z-10 flex items-center justify-center w-12 h-12 rounded-full transition-colors duration-300 shadow-md"
                    :class="[currentStep >= step ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-500']">
                    {{ step }}
                </div>
                <!-- Línea de progreso -->
                <div class="absolute h-2 bg-gray-200 w-full top-1/2 transform -translate-y-1/2 -z-0 rounded-full">
                    <div class="h-full bg-blue-500 transition-all duration-300 rounded-full"
                        :style="{ width: `${(currentStep - 1) * 33.33}%` }"></div>
                </div>
            </div>
        </div>

        <!-- Formulario por pasos -->
        <form @submit.prevent="nextStep" class="bg-white shadow-lg rounded-xl p-8 border border-gray-100">
            <!-- Paso 1: Título -->
            <div v-if="currentStep === 1" class="space-y-4">
                <label for="titulo" class="block text-gray-700 font-semibold mb-2 text-lg">
                    {{ t('producto.titlePlaceholder') }}
                </label>
                <input type="text" id="titulo" v-model="producto.titulo" required
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                    :placeholder="t('producto.titlePlaceholder')" />
            </div>

            <!-- Paso 2: Categoría -->
            <div v-if="currentStep === 2" class="space-y-4">
                <label for="categoria" class="block text-gray-700 font-semibold mb-2 text-lg">
                    {{ t('producto.category') }}
                </label>
                <select id="categoria" v-model="producto.idCategoria" required
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200">
                    <option value="">{{ t('producto.selectCategory') }}</option>
                    <option v-for="categoria in categorias" :key="categoria.id" :value="categoria.id">
                        {{ categoria.titulo }}
                    </option>
                </select>
            </div>

            <!-- Paso 3: Imágenes -->
            <div v-if="currentStep === 3" class="space-y-4">
                <label class="block text-gray-700 font-semibold mb-4 text-lg">
                    {{ t('producto.images') }}
                </label>
                <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
                    <div v-for="index in 8" :key="index"
                        class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center cursor-pointer hover:border-blue-500 transition-colors aspect-square flex flex-col justify-center items-center"
                        @click="triggerFileInput(index - 1)">
                        <div v-if="imagePreview[index - 1]" class="relative w-full h-full">
                            <img :src="imagePreview[index - 1]" class="w-full h-full object-cover rounded" />
                            <button @click.stop="removeImage(index - 1)"
                                class="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1.5 hover:bg-red-600 shadow-md transition-colors">
                                <span class="sr-only">Remove</span>
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>
                        <div v-else class="flex flex-col items-center justify-center h-full">
                            <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor"
                                viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 4v16m8-8H4" />
                            </svg>
                            <span class="mt-2 block text-sm text-gray-600">{{ t('producto.addImage') }}</span>
                        </div>
                    </div>
                </div>
                <input type="file" ref="fileInput" class="hidden" accept="image/*" @change="onFileSelected" />
            </div>

            <!-- Paso 4: Detalles finales -->
            <div v-if="currentStep === 4" class="space-y-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Marca -->
                    <div class="mb-4">
                        <label for="marca" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.brand') }}
                        </label>
                        <input type="text" id="marca" v-model="producto.marca" required
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
                    </div>

                    <!-- Precio -->
                    <div class="mb-4">
                        <label for="precio" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.price') }}
                        </label>
                        <div class="flex">
                            <input type="number" id="precio" v-model="producto.precioCentimos" required min="0" step="1"
                                class="w-3/4 px-4 py-3 border border-gray-300 rounded-l-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
                            <select v-model="producto.moneda"
                                class="w-1/4 px-4 py-3 border border-l-0 border-gray-300 rounded-r-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200">
                                <option v-for="moneda in Object.values(EMoneda)" :key="moneda" :value="moneda">
                                    {{ moneda }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Estado -->
                    <div class="mb-4">
                        <label for="estado" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.estado') }}
                        </label>
                        <select id="estado" v-model="producto.estado"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200">
                            <option v-for="estado in Object.values(EEstado)" :key="estado" :value="estado">
                                {{ t(`producto.estados.${estado}`) }}
                            </option>
                        </select>
                    </div>

                    <!-- Stock -->
                    <div class="mb-4">
                        <label for="stock" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.stock') }}
                        </label>
                        <input type="number" id="stock" v-model="producto.stock" required min="0"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
                    </div>

                    <!-- Dirección con mapa -->
                    <div class="col-span-2 mb-6">
                        <label for="direccion" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.direccion') }}
                        </label>

                        <!-- Campo de búsqueda y botón de ubicación actual -->
                        <div class="flex mb-3 gap-2">
                            <div class="relative flex-1">
                                <input type="text" id="direccion" v-model="direccionBusqueda" ref="direccionInput"
                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                                    :placeholder="t('producto.direccionPlaceholder')" />
                                <button @click="buscarDireccion" type="button"
                                    class="absolute right-1 top-1/2 -translate-y-1/2 p-2 text-gray-500 hover:text-blue-600">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none"
                                        viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                                    </svg>
                                </button>
                            </div>
                            <button @click="usarUbicacionActual" type="button"
                                class="px-3 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 flex items-center justify-center transition-colors">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                                </svg>
                                {{ t('producto.usarUbicacionActual') }}
                            </button>
                        </div>

                        <!-- Contenedor del mapa -->
                        <div id="map-container" ref="mapContainer"
                            class="w-full h-64 rounded-lg border border-gray-300 overflow-hidden shadow-sm relative"
                            style="height: 300px; min-height: 300px; position: relative; z-index: 0;">
                            <div v-if="!map" class="absolute inset-0 flex items-center justify-center bg-gray-50">
                                <svg class="animate-spin h-8 w-8 text-blue-500" xmlns="http://www.w3.org/2000/svg"
                                    fill="none" viewBox="0 0 24 24">
                                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                                        stroke-width="4"></circle>
                                    <path class="opacity-75" fill="currentColor"
                                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                                    </path>
                                </svg>
                            </div>
                        </div>

                        <!-- Dirección seleccionada (valor oculto) -->
                        <input type="hidden" v-model="producto.direccion" />

                        <!-- Mostrar la dirección seleccionada -->

                        <div v-if="producto.direccion"
                            class="mt-3 p-3 bg-blue-50 rounded-lg border border-blue-100 text-blue-800">
                            <div class="flex items-start">
                                <svg xmlns="http://www.w3.org/2000/svg"
                                    class="h-5 w-5 mr-2 mt-0.5 text-blue-600 flex-shrink-0" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                                </svg>
                                <span>{{ producto.direccion }}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Descripción -->
                <div class="mb-4">
                    <label for="descripcion" class="block text-gray-700 font-semibold mb-2">
                        {{ t('producto.description') }}
                    </label>
                    <textarea id="descripcion" v-model="producto.descripcion" required rows="4"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200 resize-none"></textarea>
                </div>

                <!-- Campos dinámicos de la categoría -->
                <div v-if="producto.camposCategoria.length > 0"
                    class="mt-6 p-6 bg-gray-50 rounded-xl border border-gray-200">
                    <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                        </svg>
                        {{ getCategoriaSeleccionada()?.titulo }} - {{ t('producto.category.fields') }}
                    </h3>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div v-for="(campo, index) in producto.camposCategoria" :key="index" class="mb-4">
                            <label :for="`campo-${index}`" class="block text-gray-700 font-medium mb-2">
                                {{ campo.nombreCampo }}
                            </label>
                            <input type="text" :id="`campo-${index}`" v-model="campo.datos"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
                        </div>
                    </div>
                </div>
            </div>

            <!-- Botones de navegación -->
            <div class="flex justify-between space-x-4 pt-6 mt-6 border-t border-gray-100">
                <button type="button" @click="previousStep" v-if="currentStep > 1"
                    class="px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                    </svg>
                    {{ t('common.previous') }}
                </button>
                <div class="flex-1"></div>
                <button type="submit"
                    class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center disabled:bg-blue-300 disabled:cursor-not-allowed"
                    :disabled="loading">
                    <svg v-if="loading && currentStep === 4" class="animate-spin -ml-1 mr-2 h-5 w-5 text-white"
                        xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4">
                        </circle>
                        <path class="opacity-75" fill="currentColor"
                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                        </path>
                    </svg>
                    <span v-if="currentStep < 4">{{ t('common.next') }}</span>
                    <span v-else>{{ t('common.save') }}</span>
                    <svg v-if="currentStep < 4" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 ml-1" fill="none"
                        viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                    </svg>
                </button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, onBeforeUnmount, ref } from 'vue';
import { EEstado } from '@/models/enums/EEstado';
import { EMoneda } from '@/models/enums/EMoneda';
import type { ProductoModify } from '@/models/producto';
import { useutf8Store } from '@/stores/counter';
import { useCategoria } from '@/composables/useCategoria';
import type { Categoria } from '@/models/categoria';
import type { CamposCategoria } from '@/models/camposCategoria';
import { useProducto } from '@/composables/useProducto';
import Swal from 'sweetalert2';
import { OpenStreetMapProvider } from 'leaflet-geosearch';
import { GeoSearchControl } from 'leaflet-geosearch';
// Import the correct types
// Removed unused import for SearchResult
import * as L from 'leaflet';
import type { SearchResult } from 'leaflet-geosearch/dist/providers/provider.js';

export default defineComponent({
    name: 'ProductosAddView',
    setup() {
        const categoriaService = useCategoria();
        const productoService = useProducto(); // Añadir el servicio de producto
        return {
            categoriaService,
            productoService
        };
    },
    data() {
        return {
            currentStep: 1,
            producto: {
                idUsuario: '',
                idCategoria: '',
                imagenes: [] as string[],
                marca: '',
                titulo: '',
                descripcion: '',
                estado: EEstado.NUEVO,
                precioCentimos: 0,
                moneda: EMoneda.EUR,
                stock: 0,
                fechaCreacion: new Date().toISOString(),
                fechaModificacion: new Date().toISOString(),
                fechaPublicacion: '',
                fechaBaja: '',
                direccion: '',
                activo: true,
                destacado: false,
                camposCategoria: [] as CamposCategoria[],
            } as ProductoModify,
            imagePreview: [] as string[],
            categorias: [] as Categoria[],
            currentImageIndex: 0,
            EEstado,
            EMoneda,
            loading: false,
            map: null as L.Map | null,
            marker: null as L.Marker | null,
            direccionBusqueda: '',
            coordenadas: {
                lat: 40.416775, // Coordenadas por defecto (Madrid)
                lng: -3.703790
            },
            mapInitialized: false,
        };
    },
    watch: {
        // Añadir este watcher para inicializar el mapa cuando llegamos al paso 4
        currentStep(newValue) {
            if (newValue === 4) {
                this.$nextTick(() => {
                    setTimeout(() => {
                        this.initMap();
                    }, 500);
                });
            }
        }
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },
        nextStep() {
            if (this.validateCurrentStep()) {
                if (this.currentStep === 2) {
                    this.initCamposCategoria();
                }
                if (this.currentStep === 4) {
                    this.saveProduct();
                } else {
                    this.currentStep++;
                }
            }
        },
        previousStep() {
            if (this.currentStep > 1) {
                this.currentStep--;
            }
        },
        validateCurrentStep(): boolean {
            switch (this.currentStep) {
                case 1:
                    if (!this.producto.titulo) {
                        Swal.fire({
                            icon: 'warning',
                            title: this.t('producto.validation.error'),
                            text: this.t('producto.validation.titleRequired'),
                            confirmButtonText: this.t('common.ok')
                        });
                        return false;
                    }
                    return true;

                case 2:
                    if (!this.producto.idCategoria) {
                        Swal.fire({
                            icon: 'warning',
                            title: this.t('producto.validation.error'),
                            text: this.t('producto.validation.categoryRequired'),
                            confirmButtonText: this.t('common.ok')
                        });
                        return false;
                    }
                    return true;

                case 3:
                    if (this.producto.imagenes.length === 0) {
                        Swal.fire({
                            icon: 'warning',
                            title: this.t('producto.validation.error'),
                            text: this.t('producto.validation.imageRequired'),
                            confirmButtonText: this.t('common.ok')
                        });
                        return false;
                    }
                    return true;

                case 4:
                    let errorMessage = '';

                    if (!this.producto.marca) {
                        errorMessage = this.t('producto.validation.brandRequired');
                    } else if (!this.producto.descripcion) {
                        errorMessage = this.t('producto.validation.descriptionRequired');
                    } else if (this.producto.precioCentimos <= 0) {
                        errorMessage = this.t('producto.validation.priceRequired');
                    } else if (this.producto.stock < 0) {
                        errorMessage = this.t('producto.validation.stockInvalid');
                    } else if (this.producto.camposCategoria &&
                        !this.producto.camposCategoria.every(campo => typeof campo.datos === 'string' && campo.datos.trim() !== '')) {
                        errorMessage = this.t('producto.validation.categoryFieldsRequired');
                    }

                    if (errorMessage) {
                        Swal.fire({
                            icon: 'warning',
                            title: this.t('producto.validation.error'),
                            text: errorMessage,
                            confirmButtonText: this.t('common.ok')
                        });
                        return false;
                    }

                    return true;

                default:
                    return false;
            }
        },
        triggerFileInput(index: number) {
            this.currentImageIndex = index;
            (this.$refs.fileInput as HTMLInputElement).click();
        },
        async onFileSelected(event: Event) {
            const file = (event.target as HTMLInputElement).files?.[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    if (e.target?.result) {
                        this.imagePreview[this.currentImageIndex] = e.target.result as string;
                        this.producto.imagenes[this.currentImageIndex] = e.target.result as string;
                    }
                };
                reader.readAsDataURL(file);
            }
        },
        removeImage(index: number) {
            this.imagePreview.splice(index, 1);
            this.producto.imagenes.splice(index, 1);
        },
        async saveProduct() {
            try {
                this.loading = true;

                this.producto.idUsuario = localStorage.getItem('userId') || '';

                await this.productoService.createProducto(this.producto);

                Swal.fire({
                    icon: 'success',
                    title: this.t('producto.add.success'),
                    text: this.t('producto.add.successMessage'),
                    confirmButtonText: this.t('common.ok')
                }).then(() => {
                    this.$router.push('/');
                });

            } catch (error: any) {
                console.error('Error al guardar el producto:', error);

                Swal.fire({
                    icon: 'error',
                    title: this.t('producto.add.error'),
                    text: error.response?.data?.message || this.t('producto.add.errorMessage'),
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },
        getCategoriaSeleccionada(): Categoria | undefined {
            return this.categorias.find(cat => cat.id === this.producto.idCategoria);
        },
        initCamposCategoria() {
            const categoriaSeleccionada = this.getCategoriaSeleccionada();
            if (categoriaSeleccionada) {
                this.producto.camposCategoria = categoriaSeleccionada.campos.map(campo => ({
                    nombreCampo: campo,
                    datos: ''
                }));
            }
        },
        initMap() {
            // Asegurarse de que el contenedor existe
            const container = document.getElementById('map-container');
            if (!container) {
                console.error('Contenedor del mapa no encontrado');
                return;
            }

            try {
                // Eliminar el mapa existente si hay uno
                if (this.map) {
                    this.map.remove();
                    this.map = null;
                }

                // Establecer primero la propiedad L.Icon.Default.imagePath
                L.Icon.Default.mergeOptions({
                    iconRetinaUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png',
                    iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
                    shadowUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
                });

                // Inicializar el mapa con un zoom más amplio para mostrar la ciudad
                this.map = L.map('map-container', {
                    center: [this.coordenadas.lat, this.coordenadas.lng],
                    zoom: 10,
                    zoomControl: true
                });
                console.log('Mapa creado:', this.map);

                // Añadir la capa de OpenStreetMap
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                }).addTo(this.map as L.Map);

                // Añadir marcador inicial si hay una dirección guardada
                if (this.producto.direccion) {
                    this.marker = L.marker([this.coordenadas.lat, this.coordenadas.lng])
                        .addTo(this.map as L.Map)
                        .bindPopup(this.producto.direccion)
                        .openPopup();
                }

                // Manejar el clic en el mapa
                this.map.on('click', this.handleMapClick);

                // Forzar una actualización del tamaño del mapa
                setTimeout(() => {
                    if (this.map) {
                        this.map.invalidateSize();
                        console.log('Tamaño del mapa actualizado');
                    }
                }, 300);

            } catch (error) {
                console.error('Error al inicializar el mapa:', error);
            }
        },
        handleMapClick(e: L.LeafletMouseEvent) {
            const { lat, lng } = e.latlng;
            this.actualizarMarcador(lat, lng);
            this.obtenerDireccionDesdeLatLng(lat, lng);
        },

        handleLocationFound(event: { location: SearchResult }) {
            this.direccionBusqueda = event.location.label || '';
            // Rest of your handler
        },

        actualizarMarcador(lat: number, lng: number) {
            // Actualizar coordenadas
            this.coordenadas = { lat, lng };

            // Si ya existe un marcador, eliminarlo
            if (this.marker) {
                this.map?.removeLayer(this.marker as L.Marker);
            }

            // Crear un nuevo marcador
            if (this.map) {
                this.marker = L.marker([lat, lng]).addTo(this.map as L.Map);
                if (this.producto.direccion) {
                    this.marker.bindPopup(this.producto.direccion).openPopup();
                }
            }
            this.mapInitialized = true;
        },

        async obtenerDireccionDesdeLatLng(lat: number, lng: number) {
            try {
                const response = await fetch(
                    `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&zoom=10&addressdetails=1`
                );
                const data = await response.json();

                if (data && data.address) {
                    // Construimos solo la dirección de la ciudad
                    const address = data.address;
                    const cityParts = [];
                    if (address.city) cityParts.push(address.city);
                    if (address.town) cityParts.push(address.town);
                    if (address.village) cityParts.push(address.village);
                    if (address.municipality) cityParts.push(address.municipality);

                    const cityName = cityParts.length > 0 ? cityParts[0] :
                        address.county || data.display_name.split(',')[0];

                    this.producto.direccion = cityName;
                    this.direccionBusqueda = cityName;

                    if (this.marker) {
                        this.marker.bindPopup(cityName).openPopup();
                    }
                }
            } catch (error) {
                console.error('Error al obtener dirección:', error);
            }
        },

        async buscarDireccion() {
            if (!this.direccionBusqueda.trim()) return;

            try {
                const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(this.direccionBusqueda)}&limit=1`);
                const data = await response.json();

                if (data && data.length > 0) {
                    const { lat, lon, display_name } = data[0];
                    this.actualizarMarcador(parseFloat(lat), parseFloat(lon));
                    this.map?.setView([parseFloat(lat), parseFloat(lon)], 13);
                    this.producto.direccion = display_name;
                }
            } catch (error) {
                console.error('Error al buscar dirección:', error);
            }
        },

        usarUbicacionActual() {
            if (!navigator.geolocation) {
                Swal.fire({
                    icon: 'error',
                    title: this.t('producto.ubicacion.errorTitle'),
                    text: this.t('producto.ubicacion.noSupported'),
                    confirmButtonText: this.t('common.ok')
                });
                return;
            }

            Swal.fire({
                title: this.t('producto.ubicacion.obteniendo'),
                text: this.t('producto.ubicacion.espere'),
                allowOutsideClick: false,
                didOpen: () => {
                    Swal.showLoading();

                    navigator.geolocation.getCurrentPosition(
                        async (position) => {
                            const { latitude, longitude } = position.coords;

                            // Primero centramos el mapa
                            this.actualizarMarcador(latitude, longitude);
                            this.map?.setView([latitude, longitude], 13);

                            // Luego obtenemos solo la ciudad
                            try {
                                const response = await fetch(
                                    `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}&zoom=10&addressdetails=1`
                                );
                                const data = await response.json();

                                if (data && data.address) {
                                    // Construimos solo la dirección de la ciudad
                                    const address = data.address;
                                    const cityParts = [];
                                    if (address.city) cityParts.push(address.city);
                                    if (address.town) cityParts.push(address.town);
                                    if (address.village) cityParts.push(address.village);
                                    if (address.municipality) cityParts.push(address.municipality);

                                    const cityName = cityParts.length > 0 ? cityParts[0] :
                                        address.county || data.display_name.split(',')[0];

                                    this.producto.direccion = cityName;
                                    this.direccionBusqueda = cityName;

                                    if (this.marker) {
                                        this.marker.bindPopup(cityName).openPopup();
                                    }
                                }
                            } catch (error) {
                                console.error('Error al obtener dirección:', error);
                            }

                            Swal.close();
                        },
                        (error) => {
                            console.error('Error al obtener ubicación:', error);
                            Swal.fire({
                                icon: 'error',
                                title: this.t('producto.ubicacion.errorTitle'),
                                text: this.t('producto.ubicacion.errorMessage'),
                                confirmButtonText: this.t('common.ok')
                            });
                        },
                        {
                            enableHighAccuracy: true,
                            timeout: 10000,
                            maximumAge: 0
                        }
                    );
                }
            });
        }
    },
    async mounted() {
        try {
            // Cargar categorías como ya hacías
            this.categoriaService.getCategorias().then(() => {
                this.categorias = this.categoriaService.categorias.value;
            }).catch(error => {
                console.error('Error al cargar categorías:', error);
            });
        } catch (error) {
            console.error('Error en mounted:', error);
        }
    },
    beforeUnmount() {
        if (this.map) {
            this.map.off();
            this.map.remove();
            this.map = null;
        }
    }
});
</script>
