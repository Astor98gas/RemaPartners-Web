<template>
    <div class="categoria-list-view max-w-[80%] mx-auto mt-6">
        <h1 class="text-3xl font-bold text-gray-800 mb-6 text-center">{{ t('categoria.list.title') }}</h1>
        <div v-if="loading" class="text-center py-4">
            <p class="text-gray-600">{{ t('categoria.list.loading') }}</p>
        </div>
        <div v-else-if="error" class="text-center py-4 text-red-500">
            <p>{{ t('categoria.list.error') }} {{ error }}</p>
        </div>
        <div v-else-if="categorias.length === 0" class="text-center py-4">
            <p class="text-gray-600">{{ t('categoria.list.empty') }}</p>
        </div>
        <div v-else>
            <div v-for="categoria in categorias" :key="categoria.id" class="mb-4">
                <CategoriaComponent :categoriaId="categoria.id" :categoriaTitulo="categoria.titulo"
                    :categoriaDescripcion="categoria.descripcion" :campos="categoria.campos"
                    @delete="removeCategoria(categoria.id)" @edit="editCategoria(categoria.id)" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import CategoriaComponent from '@/components/layout/CategoriaComponent.vue';
import { useCategoria } from '@/composables/useCategoria';
import { useToast } from 'vue-toastification';
import Swal from 'sweetalert2';
import { useutf8Store } from '@/stores/counter';

export default defineComponent({
    name: 'CategoriaListView',
    components: {
        CategoriaComponent
    },
    data() {
        return {
            categorias: [] as Array<{ id: string; titulo: string; descripcion: string; campos: any }>,
            loading: true,
            error: null as string | null
        };
    },
    async mounted() {
        await this.fetchCategorias();
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },

        async fetchCategorias() {
            try {
                this.loading = true;
                const categoriaService = useCategoria();
                await categoriaService.getCategorias();
                this.categorias = categoriaService.categorias.value;
                this.error = null;
            } catch (err) {
                console.error('Error al cargar categorías:', err);
                this.error = 'Error al cargar las categorías';
                const toast = useToast();
                toast.error(this.t('categoria.list.error'));
            } finally {
                this.loading = false;
            }
        },

        editCategoria(id: string) {
            this.$router.push(`/admin/categoria/edit/${id}`);
        },

        async removeCategoria(id: string) {
            try {
                const toast = useToast();
                const categoriaService = useCategoria();

                // Usar SweetAlert2 para la confirmación
                const result = await Swal.fire({
                    title: this.t('categoria.list.delete.title'),
                    text: this.t('categoria.list.delete.text'),
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: this.t('categoria.list.delete.confirm'),
                    cancelButtonText: this.t('categoria.list.delete.cancel')
                });

                if (result.isConfirmed) {
                    await categoriaService.deleteCategoria(id);
                    toast.success(this.t('categoria.list.delete.success'));
                    // Recargar la lista de categorías
                    await this.fetchCategorias();
                }
            } catch (err) {
                console.error('Error al eliminar categoría:', err);
                const toast = useToast();
                toast.error(this.t('categoria.list.delete.error'));
            }
        }
    }
});
</script>