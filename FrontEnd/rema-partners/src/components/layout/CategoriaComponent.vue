<template>
    <div
        class="categoria-component max-w-[90%] mx-auto bg-white shadow-lg rounded-xl overflow-hidden border border-gray-200 flex flex-col m-10">
        <div class="flex-1 px-6 py-4">
            <h2 class="text-2xl font-bold text-gray-800 mb-2">{{ categoriaTitulo }}</h2>
            <p class="text-gray-500 text-xs mb-2"><strong>Id:</strong> {{ categoriaId }}</p>
            <p class="text-gray-600 text-sm mb-4">{{ categoriaDescripcion }}</p>
            <div class="mt-4">
                <h3 class="text-gray-700 font-semibold mb-2">Campos:</h3>
                <!-- Diseño de cuadrícula para las cartas -->
                <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                    <div v-for="(campo, index) in campos" :key="index"
                        class="campo-card bg-gray-100 p-4 rounded-lg shadow-md text-center">
                        <p class="text-gray-700 font-medium">{{ campo }}</p>
                    </div>
                </div>
            </div>
        </div>
        <!-- Contenedor de botones en la parte inferior -->
        <div class="flex items-center justify-evenly p-4 bg-gray-100">
            <DeleteButton @click="$emit('delete', categoriaId)" class="ml-4" />
            <EditButton @click="$emit('edit', categoriaId)" class="ml-4" />
        </div>
    </div>
</template>

<script lang="ts">
import DeleteButton from '../ui/DeleteButton.vue';
import EditButton from '../ui/EditButton.vue';

/**
 * Componente para mostrar la información de una categoría.
 * 
 * Props:
 * - categoriaId: Identificador único de la categoría.
 * - categoriaTitulo: Título de la categoría.
 * - categoriaDescripcion: Descripción de la categoría.
 * - campos: Lista de campos asociados a la categoría.
 * 
 * Emits:
 * - delete: Se emite cuando se solicita eliminar la categoría.
 * - edit: Se emite cuando se solicita editar la categoría.
 */
export default {
    name: 'CategoriaComponent',
    components: {
        DeleteButton,
        EditButton
    },
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