<template>
    <div class="categoria-list-view max-w-[80%] mx-auto mt-6">
        <h1 class="text-3xl font-bold text-gray-800 mb-6 text-center">Lista de Categorías</h1>
        <div class="mb-4 flex justify-end">
        </div>
        <div v-for="(categoria) in categorias" :key="categoria.id" class="mb-4">
            <CategoriaComponent :categoriaId="categoria.id" :categoriaTitulo="categoria.titulo"
                :categoriaDescripcion="categoria.descripcion" :campos="categoria.campos"
                @delete="removeCategoria(categoria.id)" />
        </div>
    </div>
</template>

<script lang="ts">
import CategoriaComponent from '@/components/layout/CategoriaComponent.vue';
import { useCategoria } from '@/composables/useCategoria';

export default {
    name: 'CategoriaListView',
    components: {
        CategoriaComponent
    },
    setup() {
        const {
            categorias,
            getCategorias,
            deleteCategoria,
            loading,
            error
        } = useCategoria();

        // Fetch categories on component mount
        getCategorias();


        const removeCategoria = async (id: string) => {
            try {
                await deleteCategoria(id);
            } catch (err) {
                console.error('Error al eliminar categoría:', err);
            }
        };

        return {
            categorias,
            loading,
            error,
            removeCategoria
        };
    }
};
</script>