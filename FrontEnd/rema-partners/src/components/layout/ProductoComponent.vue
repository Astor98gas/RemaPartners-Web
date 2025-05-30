<template>
    <div
        class="producto-card bg-white rounded-xl overflow-hidden shadow-md border border-gray-200 flex flex-col transition-all duration-300 hover:shadow-xl hover:border-blue-100 h-full">
        <!-- Contenedor de imagen con posición relativa -->
        <div class="relative h-56 overflow-hidden">
            <!-- Imagen del producto con efecto de zoom suave -->
            <img class="w-full h-full object-cover transition-transform duration-500 hover:scale-110"
                :src="t('link.servidor.low') + producto.imagenes[0]" :alt="producto.titulo" @error="onImageError" />

            <!-- Insignia de stock movida dentro del contenedor de imagen -->
            <div class="absolute top-3 right-3">
                <span v-if="producto.stock > 5"
                    class="bg-green-500 text-white text-xs px-2 py-1 rounded-full font-medium">
                    {{ t('producto.inStock') }}
                </span>
                <span v-else-if="producto.stock > 0"
                    class="bg-amber-500 text-white text-xs px-2 py-1 rounded-full font-medium">
                    {{ t('producto.lowStock') }}
                </span>
                <span v-else class="bg-red-500 text-white text-xs px-2 py-1 rounded-full font-medium">
                    {{ t('producto.outOfStock') }}
                </span>
            </div>

            <!-- Insignia del estado del producto -->
            <div class="absolute top-3 left-3">
                <span class="bg-blue-600 text-white text-xs px-2 py-1 rounded-full font-medium">
                    {{ t(`producto.estados.${producto.estado}`) }}
                </span>
            </div>

            <!-- Insignia de fecha de publicación -->
            <div class="absolute bottom-3 left-3">
                <span class="bg-white/80 text-gray-700 text-xs px-2 py-1 rounded-full font-medium">
                    {{ formatDate(producto.fechaPublicacion) }}
                </span>
            </div>
        </div>

        <!-- Resto del contenido de la tarjeta -->
        <div class="flex-1 p-5">
            <h2 class="text-xl font-bold text-gray-800 mb-2 truncate hover:text-blue-600 transition-colors">{{
                producto.titulo }}</h2>

            <!-- Precio con estilo destacado -->
            <div class="mb-3 flex justify-between items-center">
                <span>
                    <span class="text-2xl font-bold text-blue-600">
                        {{ formatPrice(producto.precioCentimos, producto.moneda) }}
                    </span>
                </span>

                <!-- Insignia destacada si aplica -->
                <span v-if="producto.destacado" class="bg-yellow-400 text-yellow-800 text-xs px-2 py-1 rounded-full">
                    {{ t('producto.destacado') }}
                </span>
            </div>

            <p class="text-gray-600 text-sm mb-4 line-clamp-3">{{ producto.descripcion }}</p>

            <!-- Detalles del producto con iconos -->
            <div class="mt-4 space-y-4">
                <h3 class="text-gray-700 font-semibold flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                        viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    {{ t('producto.details.title') }}
                </h3>

                <div class="grid grid-cols-2 gap-3">
                    <div
                        class="bg-blue-50 p-3 rounded-lg shadow-sm text-center border border-blue-100 hover:bg-blue-100 transition-colors">
                        <p class="text-gray-700 font-medium text-sm">{{ t('producto.brand') }}</p>
                        <p class="text-gray-900 font-semibold">{{ producto.marca }}</p>
                    </div>
                    <div
                        class="bg-blue-50 p-3 rounded-lg shadow-sm text-center border border-blue-100 hover:bg-blue-100 transition-colors">
                        <p class="text-gray-700 font-medium text-sm">{{ t('producto.state') }}</p>
                        <p class="text-gray-900 font-semibold">{{ producto.estado }}</p>
                    </div>


                    <!-- Categoría -->
                    <div
                        class="bg-blue-50 p-3 rounded-lg shadow-sm text-center border border-blue-100 hover:bg-blue-100 transition-colors">
                        <p class="text-gray-700 font-medium text-sm flex items-center justify-center">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1 text-blue-500" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
                            </svg>
                            {{ t('producto.category') }}
                        </p>
                        <p class="text-gray-900 font-semibold truncate" :title="categoriaTitulo">
                            {{ categoriaTitulo || t('common.notAvailable') }}
                        </p>
                    </div>
                    <!-- Stock -->
                    <div
                        class="bg-blue-50 p-3 rounded-lg shadow-sm text-center border border-blue-100 hover:bg-blue-100 transition-colors">
                        <p class="text-gray-700 font-medium text-sm">{{ t('producto.stock') }}</p>
                        <p class="text-gray-900 font-semibold">
                            {{ producto.stock > 0
                                ? t('producto.stock.available').replace('{count}', producto.stock.toString())
                                : t('producto.stock.unavailable') }}
                        </p>
                    </div>
                    <!-- Campos de categoría -->
                    <div v-if="producto.camposCategoria && producto.camposCategoria.length > 0"
                        class="bg-blue-50 p-3 rounded-lg shadow-sm text-center border border-blue-100 hover:bg-blue-100 transition-colors col-span-2">
                        <p class="text-gray-700 font-medium text-sm mb-1">{{ t('producto.category.fields') }}</p>
                        <div class="flex flex-wrap gap-2 justify-center">
                            <span v-for="(campo, index) in producto.camposCategoria.slice(0, 2)" :key="index"
                                class="bg-white px-2 py-1 rounded-md text-xs text-gray-700">
                                <span class="font-medium">{{ campo.nombreCampo }}:</span> {{ campo.datos || '-' }}
                            </span>
                            <span v-if="producto.camposCategoria.length > 2" class="text-xs text-gray-500">
                                +{{ producto.camposCategoria.length - 2 }} {{ t('categoria.form.fieldsPlural') }}
                            </span>
                        </div>
                    </div>
                    <!-- Ubicación -->
                    <div
                        class="bg-blue-50 p-3 rounded-lg shadow-sm text-center border border-blue-100 hover:bg-blue-100 transition-colors col-span-2">
                        <p class="text-gray-700 font-medium text-sm flex items-center justify-center">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1 text-blue-500" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                            </svg>
                            {{ t('producto.direccion') }}
                        </p>
                        <p class="text-gray-900 font-semibold truncate" :title="producto.direccion">
                            {{ producto.direccion || t('common.notAvailable') }}
                        </p>
                    </div>

                </div>
            </div>
        </div>

        <!-- Botones con mejor estilo y animaciones -->
        <div class="flex justify-center p-4 bg-gray-50 border-t border-gray-200">
            <router-link :to="`/producto/${producto.id}`"
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transform hover:scale-105 transition-all duration-300 flex items-center justify-center focus:outline-none focus:ring-2 focus:ring-blue-300 shadow-sm">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
                {{ t('producto.action.details') }}
            </router-link>
        </div>

        <!-- Botón de acciones de administrador -->
        <div v-if="isAdmin" class="flex justify-center p-3 bg-gray-100 border-t border-gray-200">
            <button @click="toggleProductStatus"
                class="px-4 py-2 bg-amber-500 text-white rounded-lg hover:bg-amber-600 transform hover:scale-105 transition-all duration-300 flex items-center justify-center focus:outline-none focus:ring-2 focus:ring-amber-300 shadow-sm w-full">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                </svg>
                {{ producto.activo ? t('producto.action.disable') : t('producto.action.enable') }}
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import type { Producto } from '@/models/producto';
import { useutf8Store } from '@/stores/counter';
import { useUsers } from '@/composables/useUsers';
import { useProducto } from '@/composables/useProducto';
import { useCategoria } from '@/composables/useCategoria';

export default defineComponent({
    name: 'ProductoCard',
    props: {
        producto: {
            type: Object as () => Producto,
            required: true
        },
        categorias: {
            type: Array as () => Array<{ id: string; titulo: string }>,
            default: () => []
        }
    },
    data() {
        return {
            isAdmin: false
        };
    },
    computed: {
        categoriaTitulo(): string {
            const categoria = this.categorias.find(cat => cat.id === this.producto.idCategoria);
            return categoria ? categoria.titulo : '';
        }
    },
    async mounted() {
        try {
            const usersComposable = useUsers();
            const userData = await usersComposable.isLoggedIn();
            this.isAdmin = userData?.rol?.name === 'ADMIN';
        } catch (error) {
            this.isAdmin = false;
        }
    },
    methods: {
        /**
         * Traduce una clave utilizando el store de internacionalización.
         * @param {string} key - Clave de traducción.
         * @returns {string} Traducción correspondiente.
         */
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },
        /**
         * Maneja el error de carga de la imagen del producto.
         * Si la imagen falla, se reemplaza por el logo por defecto.
         * @param {Event} event - Evento de error de imagen.
         */
        onImageError(event: Event) {
            (event.target as HTMLImageElement).src = new URL('@/assets/logoCuadrado.jpeg', import.meta.url).href;
        },
        /**
         * Alterna el estado activo/inactivo del producto.
         * Llama al servicio correspondiente y emite eventos de cambio de estado.
         */
        toggleProductStatus() {
            const productoService = useProducto();
            productoService.toggleStatus(this.producto.id)
                .then(() => {
                    this.producto.activo = !this.producto.activo;
                    this.$emit('status-changed', this.producto);
                })
                .catch((error) => {
                    console.error('Error al cambiar el estado del producto:', error);
                });
            this.$emit('toggle-status', this.producto.id);
        },
        /**
         * Formatea una fecha en formato legible según el idioma del documento.
         * @param {string} dateStr - Fecha en formato string.
         * @returns {string} Fecha formateada o texto de no disponible.
         */
        formatDate(dateStr: string): string {
            if (!dateStr) return this.t('common.notAvailable');

            try {
                const date = new Date(dateStr);
                return new Intl.DateTimeFormat(document.documentElement.lang || 'es', {
                    year: 'numeric',
                    month: 'short',
                    day: 'numeric'
                }).format(date);
            } catch (error) {
                console.error('Error al formatear la fecha:', error);
                return dateStr;
            }
        },
        /**
         * Formatea el precio con separadores de miles y decimales según la moneda.
         * @param {number} precioCentimos - Precio en centimos.
         * @param {string} moneda - Código de la moneda.
         * @returns {string} Precio formateado.
         */
        formatPrice(precioCentimos: number, moneda: string = 'EUR'): string {
            const precio = precioCentimos / 100;
            const locale = document.documentElement.lang === 'en' ? 'en-US' : 'es-ES';

            return new Intl.NumberFormat(locale, {
                style: 'currency',
                currency: moneda,
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            }).format(precio);
        },
    }
});
</script>

<style scoped>
.line-clamp-3 {
    display: -webkit-box;
    -webkit-line-clamp: 3;
    line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.producto-card {
    max-width: 100%;
    position: relative;
}
</style>
