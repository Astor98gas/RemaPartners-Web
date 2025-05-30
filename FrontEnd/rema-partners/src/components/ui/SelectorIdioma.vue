<template>
    <li class="relative">
        <button @click="isOpen = !isOpen" class="flex items-center space-x-2 px-3 py-2 rounded-md hover:bg-gray-100">
            <img :src="getFlag(utf8.currentLanguage)" class="w-6 h-6" :alt="utf8.currentLanguage">
        </button>

        <div v-if="isOpen" class="absolute mt-2 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5">
            <div class="py-1">
                <button v-for="lang in ['en', 'es', 'cat']" :key="lang" @click="selectLanguage(lang)"
                    class="flex items-center w-full px-4 py-2 text-sm hover:bg-gray-100">
                    <img :src="getFlag(lang)" class="w-6 h-6 mr-3" :alt="lang">
                    {{ lang.toUpperCase() }}
                </button>
            </div>
        </div>
    </li>
</template>

<script lang="ts">
import { useutf8Store } from '@/stores/counter'

/**
 * Selector de idioma con banderas.
 * 
 * Permite al usuario seleccionar entre varios idiomas (es, en, cat).
 * Muestra la bandera correspondiente al idioma actual y un menú desplegable para cambiarlo.
 * 
 * @component
 * @data {Object} utf8 - Store de traducciones y estado del idioma.
 * @data {Boolean} isOpen - Indica si el menú desplegable está abierto.
 * 
 * @method getFlag(lang) - Devuelve la ruta de la bandera para el idioma dado.
 * @method selectLanguage(lang) - Cambia el idioma actual y cierra el menú.
 * @method closeDropdown(e) - Cierra el menú si se hace clic fuera del componente.
 */
export default {
    name: 'SelectorIdioma',
    data() {
        return {
            utf8: useutf8Store(),
            isOpen: false
        }
    },
    methods: {
        getFlag(lang: string) {
            return new URL(`../../assets/icons/flags/${lang}.png`, import.meta.url).href
        },
        selectLanguage(lang: string) {
            this.utf8.setLanguage(lang)
            this.isOpen = false
        },
        closeDropdown(e: MouseEvent) {
            if (!this.$el.contains(e.target as Node)) {
                this.isOpen = false
            }
        }
    },
    mounted() {
        // Cerrar el menú cuando se hace clic fuera del componente
        document.addEventListener('click', this.closeDropdown)
    },
    beforeUnmount() {
        document.removeEventListener('click', this.closeDropdown)
    }
}
</script>