<template>
    <div v-if="fields.length > 0" class="mt-6 p-6 bg-gray-50 rounded-xl border border-gray-200">
        <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
            {{ categoryTitle }} - {{ t('producto.category.fields') }}
        </h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div v-for="(field, index) in fields" :key="index" class="mb-4">
                <label :for="`campo-${index}`" class="block text-gray-700 font-medium mb-2">
                    {{ field.nombreCampo }}
                </label>
                <input type="text" :id="`campo-${index}`" v-model="field.datos" @input="updateField(index, field.datos)"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, type PropType, watch } from 'vue';
import { useutf8Store } from '@/stores/counter';
import type { CamposCategoria } from '@/models/camposCategoria';

/**
 * Componente CategoryFields
 * 
 * Muestra y permite editar los campos personalizados de una categoría de producto.
 * 
 * Props:
 * - fields: Array de objetos CamposCategoria que representan los campos a mostrar y editar.
 * - categoryTitle: Título de la categoría actual.
 * 
 * Emits:
 * - update:fields: Emite el array actualizado de campos cuando se modifica algún valor.
 */
export default defineComponent({
    name: 'CategoryFields',
    props: {
        /**
         * Lista de campos personalizados de la categoría.
         * @type {CamposCategoria[]}
         */
        fields: {
            type: Array as PropType<CamposCategoria[]>,
            required: true
        },
        /**
         * Título de la categoría.
         * @type {string}
         */
        categoryTitle: {
            type: String,
            default: ''
        }
    },
    emits: ['update:fields'],
    setup(props, { emit }) {
        /**
         * Traduce una clave utilizando el store de traducciones.
         * @param {string} key Clave de traducción.
         * @returns {string} Traducción correspondiente.
         */
        const t = (key: string): string => {
            const store = useutf8Store();
            return store.t(key);
        };

        /**
         * Actualiza el valor de un campo específico y emite el array actualizado.
         * @param {number} index Índice del campo a actualizar.
         * @param {string} value Nuevo valor del campo.
         */
        const updateField = (index: number, value: string) => {
            const updatedFields = [...props.fields];
            updatedFields[index].datos = value;
            emit('update:fields', updatedFields);
        };

        return {
            t,
            updateField
        };
    }
});
</script>