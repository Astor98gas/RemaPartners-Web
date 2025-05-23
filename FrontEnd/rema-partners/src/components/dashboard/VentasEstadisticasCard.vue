<template>
    <div class="bg-white overflow-hidden shadow rounded-lg">
        <div class="px-4 py-5 sm:p-6">
            <div class="flex items-center">
                <div :class="`flex-shrink-0 rounded-md p-3 ${iconBgColor}`">
                    <svg :class="`h-6 w-6 ${iconColor}`" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                        xmlns="http://www.w3.org/2000/svg">
                        <path v-if="tipo === 'ventas'" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z">
                        </path>
                        <path v-else-if="tipo === 'compras'" stroke-linecap="round" stroke-linejoin="round"
                            stroke-width="2"
                            d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z">
                        </path>
                        <path v-else-if="tipo === 'importe'" stroke-linecap="round" stroke-linejoin="round"
                            stroke-width="2"
                            d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z">
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
                            <template v-if="formatoMoneda">{{ formatearMoneda(valor, moneda) }}</template>
                            <template v-else>{{ valor }}</template>
                        </div>
                        <div v-if="cambio !== null"
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
export default {
    name: 'VentasEstadisticasCard',
    props: {
        titulo: {
            type: String,
            required: true
        },
        valor: {
            type: [String, Number],
            required: true
        },
        tipo: {
            type: String,
            default: 'otros',
            validator: function (value) {
                return ['ventas', 'compras', 'importe', 'otros'].includes(value);
            }
        },
        cambio: {
            type: Number,
            default: null
        },
        cambioTexto: {
            type: String,
            default: ''
        },
        formatoMoneda: {
            type: Boolean,
            default: false
        },
        moneda: {
            type: String,
            default: 'EUR',
            required: true
        }
    },
    computed: {
        iconBgColor() {
            const colores = {
                ventas: 'bg-green-100',
                compras: 'bg-blue-100',
                importe: 'bg-purple-100',
                otros: 'bg-indigo-100'
            };
            return colores[this.tipo] || colores.otros;
        },
        iconColor() {
            const colores = {
                ventas: 'text-green-600',
                compras: 'text-blue-600',
                importe: 'text-purple-600',
                otros: 'text-indigo-600'
            };
            return colores[this.tipo] || colores.otros;
        },
        cambioPositivo() {
            return this.cambio !== null && this.cambio >= 0;
        }
    },
    methods: {
        formatearMoneda(valor, moneda) {
            const amount = typeof valor === 'string' ? parseFloat(valor) : valor;
            return new Intl.NumberFormat('es-ES', {
                style: 'currency',
                currency: moneda,
                minimumFractionDigits: 0,
                maximumFractionDigits: 2
            }).format(amount);
        }
    }
};
</script>
