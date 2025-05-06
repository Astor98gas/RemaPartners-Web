<template>
    <div class="max-w-7xl mx-auto px-4 py-8">
        <!-- Cabecera con título y filtros -->
        <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between">
            <h1 class="text-3xl font-bold text-gray-800 mb-4 sm:mb-0 relative">
                {{ t('producto.title') }}
                <span class="absolute bottom-0 left-0 w-24 h-1 bg-blue-500 rounded-full"></span>
            </h1>

            <!-- Filtros -->
            <div class="flex space-x-2">
                <select
                    class="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option value="">Todas las categorías</option>
                </select>
            </div>
        </div>

        <!-- Spinner de carga con mejor estilo -->
        <div v-if="loading" class="flex justify-center items-center py-20">
            <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-500"></div>
            <p class="text-gray-500 ml-4 text-lg">{{ t('producto.loading') }}</p>
        </div>

        <!-- Mensaje cuando no hay productos -->
        <div v-else-if="productos.length === 0"
            class="bg-gray-50 rounded-xl p-10 text-center shadow-sm border border-gray-200">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-400 mb-4" fill="none"
                viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p class="text-gray-500 text-xl mb-2">{{ t('producto.empty') }}</p>
            <p class="text-gray-400">{{ t('producto.emptyDescription') }}</p>
        </div>

        <!-- Grid de productos con animaciones -->
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
            <ProductoCard v-for="producto in productos" :key="producto.id" :producto="producto"
                class="transform hover:scale-[1.03] transition-all duration-300" />
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import ProductoCard from '@/components/layout/ProductoComponent.vue';
import type { Producto } from '@/models/producto';
import { useProducto } from '@/composables/useProducto';
import Swal from 'sweetalert2';
import { useutf8Store } from '@/stores/counter';
import { useUsers } from '@/composables/useUsers';


export default defineComponent({
    name: 'HomeView',
    components: {
        ProductoCard
    },
    data() {
        return {
            productos: [] as Producto[],
            loading: true,
            error: null as string | null,
            isAdmin: false
        };
    },
    async mounted() {
        try {
            const usersComposable = useUsers();
            const userData = await usersComposable.isLoggedIn();
            this.isAdmin = userData?.rol?.name === 'ADMIN';
        } catch (error) {
            this.isAdmin = false;
        }
        if (this.isAdmin) {
            await this.fetchProductos();
        } else {
            await this.fetchProductosActivos();
        }
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },

        async fetchProductos() {
            try {
                this.loading = true;
                const productoService = useProducto();
                await productoService.getProductos();
                this.productos = productoService.productos.value;
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
                this.productos = productoService.productos.value;
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
