<template>
    <div class="bg-white shadow-md rounded-lg p-4">
        <div class="flex items-center mb-4">
            <h1 class="text-2xl font-bold text-gray-800">{{ t('producto.add.title') }}</h1>
            <router-link to="/productos"
                class="ml-auto bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-200">
                {{ t('producto.add.back') }}
            </router-link>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { EEstado } from '@/models/enums/EEstado';
import { EMoneda } from '@/models/enums/EMoneda';
import type { ProductoModify } from '@/models/producto';
import { useutf8Store } from '@/stores/counter';


export default defineComponent({
    name: 'ProductosAddView',
    data() {
        return {
            producto: {
                id: '',
                marca: '',
                titulo: '',
                descripcion: '',
                estado: '',
                precioCentimos: 0,
                moneda: '',
                stock: 0,
                fechaCreacion: '',
                fechaModificacion: '',
                fechaPublicacion: '',
                fechaBaja: '',
                direccion: '',
                activo: false,
                destacado: false,
            },
        };
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },
    }
});

</script>