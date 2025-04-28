<template>
    <div class="max-w-7xl mx-auto px-4 py-8">
        <h1 class="text-3xl font-bold text-gray-800 mb-8 text-center">Productos Disponibles</h1>

        <!-- Estados -->
        <div v-if="loading" class="text-center py-12">
            <p class="text-gray-500 animate-pulse">Cargando productos...</p>
        </div>

        <div v-else-if="productos.length === 0" class="text-center py-12">
            <p class="text-gray-500">No hay productos disponibles.</p>
        </div>

        <!-- Grid de productos -->
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            <ProductoCard v-for="producto in productos" :key="producto.id" :producto="producto"
                class="hover:scale-[1.02] transition-transform" />
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

export default defineComponent({
    name: 'HomeView',
    components: {
        ProductoCard
    },
    data() {
        return {
            productos: [] as Producto[],
            loading: true,
            error: null as string | null
        };
    },
    async mounted() {
        await this.fetchProductos();
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
    }
});
</script>
