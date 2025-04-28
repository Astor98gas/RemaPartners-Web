<template>
    <div
        class="max-w-[90%] w-[90%] mx-auto bg-white shadow-lg rounded-3xl overflow-hidden border border-gray-200 flex flex-col m-4 transition-all hover:shadow-xl">
        <!-- Imagen del producto -->
        <img class="w-full h-48 object-cover" :src="producto.imagenes[0]" :alt="producto.titulo" />

        <!-- Contenido de la tarjeta -->
        <div class="flex-1 p-6">
            <h2 class="text-xl font-bold text-gray-800 mb-2 truncate">{{ producto.titulo }}</h2>
            <p class="text-gray-500 text-sm mb-2">
                <span class="font-semibold">Precio:</span> {{ (producto.precioCentimos / 100).toFixed(2) }} {{
                    producto.moneda }}
            </p>
            <p class="text-gray-600 text-sm mb-4 line-clamp-3">{{ producto.descripcion }}</p>

            <div class="mt-4 space-y-3">
                <h3 class="text-gray-700 font-semibold">Detalles:</h3>
                <div class="grid grid-cols-2 gap-3">
                    <div class="bg-gray-50 p-3 rounded-lg shadow-sm text-center border border-gray-100">
                        <p class="text-gray-700 font-medium text-sm">Marca</p>
                        <p class="text-gray-500 text-sm">{{ producto.marca }}</p>
                    </div>
                    <div class="bg-gray-50 p-3 rounded-lg shadow-sm text-center border border-gray-100">
                        <p class="text-gray-700 font-medium text-sm">Estado</p>
                        <p class="text-gray-500 text-sm">{{ producto.estado }}</p>
                    </div>
                    <div class="bg-gray-50 p-3 rounded-lg shadow-sm text-center border border-gray-100">
                        <p class="text-gray-700 font-medium text-sm">Stock</p>
                        <p class="text-gray-500 text-sm">{{ producto.stock > 0 ? `${producto.stock} disponibles` :
                            'Sin stock' }}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Botones -->
        <div class="flex justify-between p-4 bg-gray-50 border-t border-gray-200">
            <button
                class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-300">
                Ver Detalles
            </button>
            <button
                class="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors focus:outline-none focus:ring-2 focus:ring-green-300">
                Comprar
            </button>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import type { Producto } from '@/models/producto';

export default defineComponent({
    name: 'ProductoCard',
    props: {
        producto: {
            type: Object as () => Producto,
            required: true
        }
    }
});
</script>
