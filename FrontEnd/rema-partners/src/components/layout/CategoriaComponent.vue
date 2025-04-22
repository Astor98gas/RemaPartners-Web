<template>
    <div class="categoria-component">
        <h2>Categoria Component</h2>
        <p>Categoria ID: {{ categoriaId }}</p>
        <p>Categoria Titulo: {{ categoriaTitulo }}</p>
        <p>Categoria Descripción: {{ categoriaDescripcion }}</p>
        <div v-for="(campo) in campos">
            <p>{{ campo }}</p>
        </div>
    </div>
</template>

<script lang="ts">

export default {
    name: 'CategoriaComponent',
    props: {
        categoriaId: {
            type: String,
        },
        categoriaTitulo: {
            type: String,
        },
        categoriaDescripcion: {
            type: String,
        },
        campos: {
            type: Array,
        }
    }
}
</script>