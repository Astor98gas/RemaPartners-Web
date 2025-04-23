<template>
    <div class="categoria-list-view max-w-[80%] mx-auto mt-6">
        <h1 class="text-3xl font-bold text-gray-800 mb-6 text-center">Lista de Categorías</h1>
        <div class="mb-4 flex justify-end">
            <button @click="addCategoria"
                class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:outline-none">
                Añadir Categoría
            </button>
        </div>
        <div v-for="(categoria) in categorias" :key="categoria.id" class="mb-4">
            <CategoriaComponent :categoriaId="categoria.id" :categoriaTitulo="categoria.titulo"
                :categoriaDescripcion="categoria.descripcion" :campos="categoria.campos" @delete="deleteCategoria" />
        </div>
    </div>
</template>

<script lang="ts">
import CategoriaComponent from '@/components/layout/CategoriaComponent.vue'

export default {
    name: 'CategoriaListView',
    components: {
        CategoriaComponent
    },
    data() {
        return {
            categorias: [
                {
                    id: '1',
                    titulo: 'Categoria 1 Lorem ipsum dolor sit amet consectetur adipiscing elit',
                    descripcion: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.',
                    campos: ['Campo 1sdffsdsdafsadfsadfsad fsafsfsadfsdafasdfsfsfsadffsdfA', 'Campo 1B', 'Campo 1C', 'Campo 1D', 'Campo 1E', 'Campo 1F', 'Campo 1G', 'Campo 1H', 'Campo 1I', 'Campo 1J']
                },
                {
                    id: '2',
                    titulo: 'Categoria 2',
                    descripcion: 'Descripción de Categoria 2',
                    campos: ['Campo 2A', 'Campo 2B', 'Campo 2C', 'Campo 2D', 'Campo 2E', 'Campo 2F', 'Campo 2G', 'Campo 2H', 'Campo 2I', 'Campo 2J']
                },
                {
                    id: '3',
                    titulo: 'Categoria 3',
                    descripcion: 'Descripción de Categoria 3',
                    campos: ['Campo 3A', 'Campo 3B', 'Campo 3C', 'Campo 3D', 'Campo 3E', 'Campo 3F', 'Campo 3G', 'Campo 3H', 'Campo 3I', 'Campo 3J']
                }
            ]
        }
    },
    methods: {
        deleteCategoria(id: string) {
            this.categorias = this.categorias.filter(categoria => categoria.id !== id);
        },
        addCategoria() {
            const newId = (this.categorias.length + 1).toString();
            this.categorias.push({
                id: newId,
                titulo: `Categoria ${newId}`,
                descripcion: `Descripción de Categoria ${newId}`,
                campos: [
                    `Campo ${newId}A`, `Campo ${newId}B`, `Campo ${newId}C`, `Campo ${newId}D`,
                    `Campo ${newId}E`, `Campo ${newId}F`, `Campo ${newId}G`, `Campo ${newId}H`,
                    `Campo ${newId}I`, `Campo ${newId}J`
                ]
            });
        }
    }
}
</script>