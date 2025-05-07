<template>
    <div class="max-w-7xl mx-auto px-4 py-8">
        <!-- Cabecera con título y filtros -->
        <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between">
            <h1 class="text-3xl font-bold text-gray-800 mb-4 sm:mb-0 relative">
                {{ t('producto.title') }}
                <span class="absolute bottom-0 left-0 w-24 h-1 bg-blue-500 rounded-full"></span>
            </h1>
        </div>

        <!-- Filtros - Versión mejorada -->
        <div class="mb-8 bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <!-- Cabecera de filtros -->
            <div class="flex justify-between items-center mb-4">
                <h2 class="text-lg font-semibold text-gray-700 flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                        viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
                    </svg>
                    {{ t('producto.filters.title') }}
                </h2>
            </div>

            <!-- Campo de búsqueda por palabra clave (fila completa) -->
            <div class="mb-4">
                <div class="relative">
                    <input type="text" v-model="filtros.keyword" :placeholder="t('producto.filters.searchPlaceholder')"
                        class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 bg-gray-50 focus:bg-white transition-all duration-200"
                        @input="aplicarFiltros" />
                    <div class="absolute left-3 top-3.5 text-gray-400">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                        </svg>
                    </div>
                </div>
            </div>

            <!-- Filtros secundarios en grid -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
                <!-- Filtro por categoría -->
                <div class="flex flex-col">
                    <label class="text-sm text-gray-600 mb-1 font-medium">{{ t('producto.filters.category') }}</label>
                    <select v-model="filtros.categoria"
                        class="px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                        @change="aplicarFiltros">
                        <option value="">{{ t('producto.filters.allCategories') }}</option>
                        <option v-for="categoria in categorias" :key="categoria.id" :value="categoria.id">
                            {{ categoria.titulo }}
                        </option>
                    </select>
                </div>

                <!-- Filtro por estado -->
                <div class="flex flex-col">
                    <label class="text-sm text-gray-600 mb-1 font-medium">{{ t('producto.filters.state') }}</label>
                    <select v-model="filtros.estado"
                        class="px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                        @change="aplicarFiltros">
                        <option value="">{{ t('producto.filters.allStates') }}</option>
                        <option v-for="estado in estadosProducto" :key="estado" :value="estado">
                            {{ t(`producto.estados.${estado}`) }}
                        </option>
                    </select>
                </div>

                <!-- Filtro por ubicación -->
                <div class="flex flex-col">
                    <label class="text-sm text-gray-600 mb-1 font-medium">{{ t('producto.filters.location') }}</label>
                    <select v-model="filtros.ubicacion"
                        class="px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                        @change="aplicarFiltros">
                        <option value="">{{ t('producto.filters.allLocations') }}</option>
                        <option v-for="ubicacion in ubicacionesDisponibles" :key="ubicacion" :value="ubicacion">
                            {{ ubicacion }}
                        </option>
                    </select>
                </div>

                <!-- Filtro por rango de precio -->
                <div class="flex flex-col">
                    <label class="text-sm text-gray-600 mb-1 font-medium">{{ t('producto.filters.priceRange') }}</label>
                    <div class="flex items-center gap-2">
                        <input type="number" v-model.number="filtros.precioMin" :placeholder="t('producto.filters.min')"
                            class="w-1/2 px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                            min="0" @change="aplicarFiltros" />
                        <span class="text-gray-400">-</span>
                        <input type="number" v-model.number="filtros.precioMax" :placeholder="t('producto.filters.max')"
                            class="w-1/2 px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                            min="0" @change="aplicarFiltros" />
                    </div>
                </div>
            </div>

            <!-- Contador de resultados y botón de reinicio -->
            <div class="flex justify-between items-center pt-4 border-t border-gray-100">
                <p class="text-sm text-gray-500">
                    <span class="font-medium text-gray-700">{{ productosVisibles.length }}</span> {{
                        t('producto.resultsFound') }}
                </p>
                <button @click="reiniciarFiltros"
                    class="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 flex items-center transition-colors duration-200"
                    :class="{ 'opacity-50 cursor-not-allowed': !hayFiltrosActivos }" :disabled="!hayFiltrosActivos">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                    </svg>
                    {{ t('producto.resetFilters') }}
                </button>
            </div>
        </div>

        <!-- Spinner de carga con mejor estilo -->
        <div v-if="loading" class="flex justify-center items-center py-20">
            <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-500"></div>
            <p class="text-gray-500 ml-4 text-lg">{{ t('producto.loading') }}</p>
        </div>

        <!-- Mensaje cuando no hay productos -->
        <div v-else-if="productosVisibles.length === 0"
            class="bg-gray-50 rounded-xl p-10 text-center shadow-sm border border-gray-200">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-400 mb-4" fill="none"
                viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p class="text-gray-500 text-xl mb-2">{{ t('producto.emptyFilters') }}</p>
            <p class="text-gray-400">{{ t('producto.emptyFiltersDescription') }}</p>
        </div>

        <!-- Grid de productos con animaciones -->
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
            <ProductoCard v-for="producto in productosVisibles" :key="producto.id" :producto="producto"
                :categorias="categorias" class="transform hover:scale-[1.03] transition-all duration-300" />
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import ProductoCard from '@/components/layout/ProductoComponent.vue';
import type { Producto } from '@/models/producto';
import { useProducto } from '@/composables/useProducto';
import { useCategoria } from '@/composables/useCategoria';
import Swal from 'sweetalert2';
import { useutf8Store } from '@/stores/counter';
import { useUsers } from '@/composables/useUsers';
import { EEstado } from '@/models/enums/EEstado';

export default defineComponent({
    name: 'HomeView',
    components: {
        ProductoCard
    },
    data() {
        return {
            productos: [] as Producto[],
            productosOriginales: [] as Producto[],
            categorias: [] as any[],
            loading: true,
            error: null as string | null,
            isAdmin: false,
            estadosProducto: Object.values(EEstado),
            ubicacionesDisponibles: [] as string[],
            filtros: {
                keyword: '',
                categoria: '',
                estado: '',
                ubicacion: '',
                precioMin: null as number | null,
                precioMax: null as number | null
            }
        };
    },
    computed: {
        productosVisibles(): Producto[] {
            return this.productos;
        },
        hayFiltrosActivos(): boolean {
            return this.filtros.keyword !== '' ||
                this.filtros.categoria !== '' ||
                this.filtros.estado !== '' ||
                this.filtros.ubicacion !== '' ||
                this.filtros.precioMin !== null ||
                this.filtros.precioMax !== null;
        }
    },
    async mounted() {
        try {
            // Cargar categorías
            await this.cargarCategorias();

            // Verificar si el usuario es administrador
            const usersComposable = useUsers();
            const userData = await usersComposable.isLoggedIn();
            this.isAdmin = userData?.rol?.name === 'ADMIN';

            // Cargar productos según permisos
            if (this.isAdmin) {
                await this.fetchProductos();
            } else {
                await this.fetchProductosActivos();
            }

            // Extraer ubicaciones únicas de los productos
            this.extraerUbicacionesUnicas();
        } catch (error) {
            this.isAdmin = false;
            await this.fetchProductosActivos();
        }
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },

        async cargarCategorias() {
            try {
                const categoriaService = useCategoria();
                await categoriaService.getCategorias();
                this.categorias = categoriaService.categorias.value;
            } catch (err) {
                console.error('Error al cargar categorías:', err);
            }
        },

        async fetchProductos() {
            try {
                this.loading = true;
                const productoService = useProducto();
                await productoService.getProductos();
                this.productosOriginales = [...productoService.productos.value];
                this.productos = [...this.productosOriginales];
                this.error = null;
            } catch (err: any) {
                console.error('Error al cargar productos:', err);
                this.error = err.response?.data?.message || 'Error al cargar los productos';
                Swal.fire({
                    icon: 'error',
                    title: this.t('producto.list.error'),
                    text: this.error || '',
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },

        async fetchProductosActivos() {
            try {
                this.loading = true;
                const productoService = useProducto();
                await productoService.getProductosActivos();
                this.productosOriginales = [...productoService.productos.value];
                this.productos = [...this.productosOriginales];
                this.error = null;
            } catch (err: any) {
                console.error('Error al cargar productos activos:', err);
                this.error = err.response?.data?.message || 'Error al cargar los productos activos';
                Swal.fire({
                    icon: 'error',
                    title: this.t('producto.list.error'),
                    text: this.error || '',
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },

        extraerUbicacionesUnicas() {
            // Extraer ubicaciones únicas para el filtro
            const ubicaciones = new Set<string>();
            this.productosOriginales.forEach(producto => {
                if (producto.direccion && producto.direccion.trim() !== '') {
                    // Extraer ciudad o primera parte de la dirección
                    const partesDireccion = producto.direccion.split(',');
                    if (partesDireccion.length > 0) {
                        ubicaciones.add(partesDireccion[0].trim());
                    }
                }
            });
            this.ubicacionesDisponibles = Array.from(ubicaciones).sort();
        },

        aplicarFiltros() {
            this.loading = true;

            setTimeout(() => {
                let productosFiltrados = [...this.productosOriginales];

                // Filtrar por palabra clave (busca en título, descripción, marca y campos de categoría)
                if (this.filtros.keyword) {
                    const keyword = this.filtros.keyword.toLowerCase();
                    productosFiltrados = productosFiltrados.filter(producto =>
                        producto.titulo.toLowerCase().includes(keyword) ||
                        producto.descripcion.toLowerCase().includes(keyword) ||
                        producto.marca.toLowerCase().includes(keyword) ||
                        (producto.camposCategoria && producto.camposCategoria.some(campo =>
                            campo.datos && campo.datos.toLowerCase().includes(keyword)
                        ))
                    );
                }

                // Filtrar por categoría
                if (this.filtros.categoria) {
                    productosFiltrados = productosFiltrados.filter(producto =>
                        producto.idCategoria === this.filtros.categoria
                    );
                }

                // Filtrar por estado
                if (this.filtros.estado) {
                    productosFiltrados = productosFiltrados.filter(producto =>
                        producto.estado === this.filtros.estado
                    );
                }

                // Filtrar por ubicación
                if (this.filtros.ubicacion) {
                    productosFiltrados = productosFiltrados.filter(producto =>
                        producto.direccion && producto.direccion.startsWith(this.filtros.ubicacion)
                    );
                }

                // Filtrar por rango de precio
                if (this.filtros.precioMin !== null) {
                    productosFiltrados = productosFiltrados.filter(producto =>
                        producto.precioCentimos >= this.filtros.precioMin! * 100
                    );
                }

                if (this.filtros.precioMax !== null) {
                    productosFiltrados = productosFiltrados.filter(producto =>
                        producto.precioCentimos <= this.filtros.precioMax! * 100
                    );
                }

                this.productos = productosFiltrados;
                this.loading = false;
            }, 300); // Pequeño delay para mostrar el spinner
        },

        reiniciarFiltros() {
            this.filtros = {
                keyword: '',
                categoria: '',
                estado: '',
                ubicacion: '',
                precioMin: null,
                precioMax: null
            };
            this.productos = [...this.productosOriginales];
        }
    }
});
</script>

<style scoped>
.grid {
    animation: fade-in 0.5s ease-in-out;
}

@keyframes fade-in {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>
