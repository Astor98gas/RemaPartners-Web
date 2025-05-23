<template>
    <div class="bg-white overflow-hidden shadow rounded-lg">
        <div class="px-4 py-5 sm:p-6">
            <div class="flex items-center">
                <div :class="`flex-shrink-0 rounded-md p-3 ${iconBgColor}`">
                    <svg :class="`h-6 w-6 ${iconColor}`" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                        xmlns="http://www.w3.org/2000/svg">
                        <path v-if="tipo === 'productos'" stroke-linecap="round" stroke-linejoin="round"
                            stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10"></path>
                        <path v-else-if="tipo === 'visitas'" stroke-linecap="round" stroke-linejoin="round"
                            stroke-width="2"
                            d="M15 12a3 3 0 11-6 0 3 3 0 016 0z M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z">
                        </path>
                        <path v-else-if="tipo === 'promedio'" stroke-linecap="round" stroke-linejoin="round"
                            stroke-width="2"
                            d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z">
                        </path>
                        <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path>
                    </svg>
                </div>
                <div class="ml-5 w-0 flex-1">
                    <dt class="text-sm font-medium text-gray-500 truncate">
                        {{ titulo }}
                    </dt>
                    <dd class="flex items-baseline">
                        <div class="text-2xl font-semibold text-gray-900">
                            {{ valor }}
                        </div>
                        <div v-if="cambio"
                            :class="`ml-2 flex items-center text-sm font-semibold ${cambioPositivo ? 'text-green-600' : 'text-red-600'}`">
                            <svg v-if="cambioPositivo" class="self-center flex-shrink-0 h-5 w-5 text-green-500"
                                fill="currentColor" viewBox="0 0 20 20" aria-hidden="true">
                                <path fill-rule="evenodd"
                                    d="M5.293 9.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 7.414V15a1 1 0 11-2 0V7.414L6.707 9.707a1 1 0 01-1.414 0z"
                                    clip-rule="evenodd" />
                            </svg>
                            <svg v-else class="self-center flex-shrink-0 h-5 w-5 text-red-500" fill="currentColor"
                                viewBox="0 0 20 20" aria-hidden="true">
                                <path fill-rule="evenodd"
                                    d="M14.707 10.293a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 111.414-1.414L9 12.586V5a1 1 0 012 0v7.586l2.293-2.293a1 1 0 011.414 0z"
                                    clip-rule="evenodd" />
                            </svg>
                            <span class="sr-only">{{ cambioPositivo ? 'Aumentó' : 'Disminuyó' }} by</span>
                            {{ cambioTexto }}
                        </div>
                    </dd>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
/**
 * Componente EstadisticasCard
 * Tarjeta visual para mostrar estadísticas con icono, valor y cambio porcentual.
 *
 * @component
 * @prop {String} titulo - Título de la estadística.
 * @prop {String|Number} valor - Valor principal a mostrar.
 * @prop {String} [tipo='otros'] - Tipo de estadística ('productos', 'visitas', 'promedio', 'otros').
 * @prop {Number|null} [cambio=null] - Valor numérico del cambio respecto a un periodo anterior.
 * @prop {String} [cambioTexto=''] - Texto descriptivo del cambio.
 */
export default {
    name: 'EstadisticasCard',
    props: {
        /**
         * Título de la estadística.
         */
        titulo: {
            type: String,
            required: true
        },
        /**
         * Valor principal a mostrar.
         */
        valor: {
            type: [String, Number],
            required: true
        },
        /**
         * Tipo de estadística para icono y color.
         * Puede ser 'productos', 'visitas', 'promedio' u 'otros'.
         */
        tipo: {
            type: String,
            default: 'otros',
            validator: function (value) {
                return ['productos', 'visitas', 'promedio', 'otros'].includes(value);
            }
        },
        /**
         * Valor numérico del cambio respecto a un periodo anterior.
         */
        cambio: {
            type: Number,
            default: null
        },
        /**
         * Texto descriptivo del cambio.
         */
        cambioTexto: {
            type: String,
            default: ''
        }
    },
    computed: {
        /**
         * Color de fondo del icono según el tipo.
         * @returns {String}
         */
        iconBgColor() {
            const colores = {
                productos: 'bg-indigo-100',
                visitas: 'bg-green-100',
                promedio: 'bg-blue-100',
                otros: 'bg-purple-100'
            };
            return colores[this.tipo] || colores.otros;
        },
        /**
         * Color del icono según el tipo.
         * @returns {String}
         */
        iconColor() {
            const colores = {
                productos: 'text-indigo-600',
                visitas: 'text-green-600',
                promedio: 'text-blue-600',
                otros: 'text-purple-600'
            };
            return colores[this.tipo] || colores.otros;
        },
        /**
         * Indica si el cambio es positivo o negativo.
         * @returns {Boolean}
         */
        cambioPositivo() {
            return this.cambio !== null && this.cambio >= 0;
        }
    }
};
</script>
