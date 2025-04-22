<template>
    <div class="categoria-list-view">
        <h1>Lista de Categorías</h1>
        <div v-for="(categoria) in categorias" :key="categoria.id">
            <CategoriaComponent :categoriaId="categoria.id" :categoriaTitulo="categoria.titulo"
                :categoriaDescripcion="categoria.descripcion" :campos="categoria.campos" />
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
                { id: '1', titulo: 'Categoria 1', descripcion: 'Descripción de Categoria 1', campos: ['Campo 1', 'Campo 2'] },
                { id: '2', titulo: 'Categoria 2', descripcion: 'Descripción de Categoria 2', campos: ['Campo A', 'Campo B'] },
                { id: '3', titulo: 'Categoria 3', descripcion: 'Descripción de Categoria 3', campos: ['Campo X', 'Campo Y'] }
            ]
        }
    }


}

</script>