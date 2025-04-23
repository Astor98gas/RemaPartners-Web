<template>
    <div
        class="categoria-component max-w-4xl mx-auto bg-white shadow-lg rounded-lg overflow-hidden border border-gray-200 flex flex-col md:flex-row">
        <div class="flex-1 px-6 py-4">
            <h2 class="text-2xl font-bold text-gray-800 mb-2">{{ categoriaTitulo }}</h2>
            <p class="text-gray-500 text-xs mb-2"><strong>Id:</strong> {{ categoriaId }}</p>
            <p class="text-gray-600 text-sm mb-4">{{ categoriaDescripcion }}</p>
            <div class="mt-4">
                <h3 class="text-gray-700 font-semibold mb-2">Campos:</h3>
                <!-- Diseño de cuadrícula para las cartas -->
                <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
                    <div v-for="(campo, index) in campos" :key="index"
                        class="campo-card bg-gray-100 p-4 rounded-lg shadow-md text-center">
                        <p class="text-gray-700 font-medium">{{ campo }}</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="flex items-center justify-center p-4 bg-gray-100">
            <button @click="$emit('delete', categoriaId)"
                class="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600 focus:outline-none">
                Eliminar
            </button>
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