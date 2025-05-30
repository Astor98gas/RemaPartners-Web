<template>
    <div class="bg-white shadow overflow-hidden rounded-lg p-6">
        <h2 class="text-xl font-semibold border-b pb-2 mb-4">{{ t('profile.invoices') }}</h2>

        <!-- Tabs for switching between buyer and seller invoices -->
        <div class="mb-6">
            <div class="border-b border-gray-200">
                <nav class="flex -mb-px space-x-4">
                    <button @click="currentTab = 'buyer'" :class="[
                        'py-2 px-4 text-sm font-medium transition-colors',
                        currentTab === 'buyer' ?
                            'border-b-2 border-blue-500 text-blue-600' :
                            'text-gray-500 hover:text-gray-700 hover:border-gray-300'
                    ]">
                        {{ t('invoice.as_buyer') }}
                    </button>
                    <button @click="currentTab = 'seller'" :class="[
                        'py-2 px-4 text-sm font-medium transition-colors',
                        currentTab === 'seller' ?
                            'border-b-2 border-blue-500 text-blue-600' :
                            'text-gray-500 hover:text-gray-700 hover:border-gray-300'
                    ]">
                        {{ t('invoice.as_seller') }}
                    </button>
                </nav>
            </div>
        </div>

        <!-- Loading section -->
        <div v-if="loading" class="flex justify-center items-center h-36">
            <div class="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500"></div>
        </div>

        <!-- Error section -->
        <div v-else-if="error" class="text-red-500 text-center p-6 bg-red-50 rounded-md">
            <p>{{ error }}</p>
        </div>

        <!-- No invoices section -->
        <div v-else-if="!facturas.length" class="text-gray-500 text-center p-6 bg-gray-50 rounded-md">
            <p>{{ t('invoice.no_invoices') }}</p>
        </div>

        <!-- Invoices table -->
        <div v-else class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                    <tr>
                        <th scope="col"
                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {{ t('invoice.product') }}
                        </th>
                        <th scope="col"
                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {{ t('invoice.quantity') }}
                        </th>
                        <th scope="col"
                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {{ t('invoice.amount') }}
                        </th>
                        <th scope="col"
                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {{ t('invoice.date') }}
                        </th>
                        <th scope="col"
                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {{ t('invoice.status') }}
                        </th>
                        <th scope="col"
                            class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {{ t('invoice.actions') }}
                        </th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="factura in facturas" :key="factura.id">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            {{ factura.tituloProducto || 'N/A' }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            {{ factura.cantidad }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {{ formatCurrency(calculateTotal(factura)) }} {{ factura.moneda }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            {{ formatDate(factura.fechaEmision) }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <span :class="[
                                'px-2 inline-flex text-xs leading-5 font-semibold rounded-full',
                                statusClasses[factura.estado] || 'bg-gray-100 text-gray-800'
                            ]">
                                {{ t(`invoice.status.${factura.estado.toLowerCase()}`) }}
                            </span>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                            <button @click="viewInvoiceDetails(factura.id)" class="text-blue-600 hover:text-blue-900">
                                {{ t('invoice.view_details') }}
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Invoice details modal -->
        <div v-if="showDetailsModal && selectedInvoice"
            class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
            <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
                <div class="p-6">
                    <div class="flex justify-between items-center mb-6">
                        <h3 class="text-lg font-medium text-gray-900">{{ t('invoice.details') }}</h3>
                        <button @click="showDetailsModal = false" class="text-gray-400 hover:text-gray-500">
                            <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>

                    <div class="border-t border-gray-200 pt-4">
                        <dl class="grid grid-cols-1 gap-x-4 gap-y-6 sm:grid-cols-2">
                            <div>
                                <dt class="text-sm font-medium text-gray-500">{{ t('invoice.product') }}</dt>
                                <dd class="mt-1 text-sm text-gray-900">{{ selectedInvoice.tituloProducto || 'N/A' }}
                                </dd>
                            </div>
                            <div>
                                <dt class="text-sm font-medium text-gray-500">{{ t('invoice.invoice_id') }}</dt>
                                <dd class="mt-1 text-sm text-gray-900">{{ selectedInvoice.id }}</dd>
                            </div>
                            <div>
                                <dt class="text-sm font-medium text-gray-500">{{ t('invoice.date') }}</dt>
                                <dd class="mt-1 text-sm text-gray-900">{{ formatDate(selectedInvoice.fechaEmision) }}
                                </dd>
                            </div>
                            <div>
                                <dt class="text-sm font-medium text-gray-500">{{ t('invoice.status') }}</dt>
                                <dd class="mt-1 text-sm text-gray-900">
                                    <span :class="[
                                        'px-2 inline-flex text-xs leading-5 font-semibold rounded-full',
                                        statusClasses[selectedInvoice.estado] || 'bg-gray-100 text-gray-800'
                                    ]">
                                        {{ t(`invoice.status.${selectedInvoice.estado.toLowerCase()}`) }}
                                    </span>
                                </dd>
                            </div>
                            <div class="sm:col-span-2">
                                <dt class="text-sm font-medium text-gray-500">{{ t('invoice.details') }}</dt>
                                <dd class="mt-1 text-sm text-gray-900">
                                    <div class="border border-gray-300 rounded-md">
                                        <table class="min-w-full divide-y divide-gray-300">
                                            <thead>
                                                <tr>
                                                    <th scope="col"
                                                        class="px-3 py-2 text-left text-xs font-medium text-gray-500">
                                                        {{ t('invoice.product') }}
                                                    </th>
                                                    <th scope="col"
                                                        class="px-3 py-2 text-left text-xs font-medium text-gray-500">
                                                        {{ t('invoice.quantity') }}
                                                    </th>
                                                    <th scope="col"
                                                        class="px-3 py-2 text-left text-xs font-medium text-gray-500">
                                                        {{ t('invoice.unit_price') }}
                                                    </th>
                                                    <th scope="col"
                                                        class="px-3 py-2 text-right text-xs font-medium text-gray-500">
                                                        {{ t('invoice.subtotal') }}
                                                    </th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td class="px-3 py-2 text-xs text-gray-900">
                                                        {{ selectedInvoice.tituloProducto || 'N/A' }}
                                                    </td>
                                                    <td class="px-3 py-2 text-xs text-gray-900">
                                                        {{ selectedInvoice.cantidad }}
                                                    </td>
                                                    <td class="px-3 py-2 text-xs text-gray-900">
                                                        {{ formatCurrency(selectedInvoice.precioCentimos / 100)
                                                        }} {{ selectedInvoice.moneda }}
                                                    </td>
                                                    <td class="px-3 py-2 text-xs text-gray-900 text-right">
                                                        {{ formatCurrency((selectedInvoice.precioCentimos *
                                                            selectedInvoice.cantidad) / 100) }} {{ selectedInvoice.moneda }}
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <th scope="row" colspan="3"
                                                        class="px-3 py-2 text-xs font-medium text-gray-900 text-right">
                                                        {{ t('invoice.iva') }} ({{ selectedInvoice.iva }}%)
                                                    </th>
                                                    <td class="px-3 py-2 text-xs text-gray-900 text-right">
                                                        {{ formatCurrency(calculateIVA(selectedInvoice)) }} {{
                                                            selectedInvoice.moneda }}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th scope="row" colspan="3"
                                                        class="px-3 py-2 text-sm font-medium text-gray-900 text-right">
                                                        {{ t('invoice.total') }}
                                                    </th>
                                                    <td class="px-3 py-2 text-sm font-medium text-gray-900 text-right">
                                                        {{ formatCurrency(calculateTotal(selectedInvoice)) }} {{
                                                            selectedInvoice.moneda }}
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </div>
                                </dd>
                            </div>
                            <div v-if="selectedInvoice.notasAdicionales" class="sm:col-span-2">
                                <dt class="text-sm font-medium text-gray-500">{{ t('invoice.notes') }}</dt>
                                <dd class="mt-1 text-sm text-gray-900">{{ selectedInvoice.notasAdicionales }}</dd>
                            </div>
                        </dl>
                    </div>

                    <div class="mt-6 flex justify-end">
                        <button @click="showDetailsModal = false"
                            class="bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700">
                            {{ t('common.close') }}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { useFactura } from '@/composables/useFactura';
import { useutf8Store } from '@/stores/counter';
import type { FacturaEntity } from '@/models/factura';

/**
 * Componente para mostrar y gestionar las facturas del usuario.
 * Permite alternar entre facturas como comprador y como vendedor, ver detalles y calcular totales.
 * 
 * @component
 * @example
 * <FacturasPanel :userId="userId" />
 */
export default defineComponent({
    name: 'FacturasPanel',
    props: {
        /**
         * ID del usuario cuyas facturas se mostrarán.
         * @type {string}
         * @required
         */
        userId: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            currentTab: 'buyer',
            facturas: [] as FacturaEntity[],
            loading: false,
            error: null as string | null,
            showDetailsModal: false,
            selectedInvoice: null as FacturaEntity | null,
            facturaComposable: useFactura(),
            statusClasses: {
                'PENDIENTE': 'bg-yellow-100 text-yellow-800',
                'PAGADA': 'bg-green-100 text-green-800',
                'CANCELADA': 'bg-red-100 text-red-800',
                'EMITIDA': 'bg-blue-100 text-blue-800'
            }
        };
    },
    watch: {
        currentTab: {
            handler() {
                this.loadInvoices();
            },
            immediate: true
        },
        userId: {
            handler(newVal) {
                console.log(`userId changed to: ${newVal}`);
                if (newVal) {
                    this.loadInvoices();
                }
            },
            immediate: true
        }
    },
    methods: {
        /**
         * Carga las facturas del usuario según la pestaña seleccionada (comprador o vendedor).
         */
        async loadInvoices() {
            if (!this.userId) {
                console.error("No user ID provided to FacturasPanel");
                this.error = "No se ha proporcionado un ID de usuario";
                return;
            }

            console.log(`Loading invoices for user ID: ${this.userId}, tab: ${this.currentTab}`);
            this.loading = true;
            this.error = null;
            this.facturas = [];

            try {
                if (this.currentTab === 'buyer') {
                    console.log(`Fetching invoices as buyer for user ID: ${this.userId}`);
                    const response = await this.facturaComposable.getFacturasByBuyerId(this.userId);
                    console.log("API Response (buyer):", response);
                    this.facturas = response || [];
                } else {
                    console.log(`Fetching invoices as seller for user ID: ${this.userId}`);
                    const response = await this.facturaComposable.getFacturasBySellerId(this.userId);
                    console.log("API Response (seller):", response);
                    this.facturas = response || [];
                }
            } catch (err: any) {
                console.error("Error loading invoices:", err);
                this.error = err.message || this.t('invoice.loading_error');
            } finally {
                this.loading = false;
                console.log(`Loaded ${this.facturas.length} invoices`);
            }
        },
        /**
         * Muestra los detalles de una factura seleccionada en un modal.
         * @param {string|undefined} invoiceId - ID de la factura a mostrar.
         */
        async viewInvoiceDetails(invoiceId: string | undefined) {
            if (!invoiceId) return;

            try {
                // Opción 1: Usar la factura de la lista, pero mapear los campos si es necesario
                const invoice = this.facturas.find(f => f.id === invoiceId);
                if (invoice) {
                    console.log("Selected invoice:", invoice); // Depuración
                    // Mapear los campos para asegurar consistencia
                    this.selectedInvoice = {
                        ...invoice,
                        // Usar solo propiedades que existen en el tipo
                        tituloProducto: invoice.tituloProducto,
                        estado: invoice.estado,
                        precioCentimos: invoice.precioCentimos,
                        fechaEmision: invoice.fechaEmision || new Date().toISOString()
                    };
                    this.showDetailsModal = true;
                } else {
                    console.error("Invoice not found with ID:", invoiceId);
                }
            } catch (err) {
                console.error("Error displaying invoice details:", err);
            }
        },
        /**
         * Calcula el importe del IVA para una factura.
         * @param {FacturaEntity} factura - Factura a calcular.
         * @returns {number} Importe del IVA.
         */
        calculateIVA(factura: FacturaEntity) {
            // Usar un valor de IVA predeterminado si no viene en la respuesta de la API
            const ivaRate = factura.iva || 21; // 21% es el IVA común en España
            const subtotal = (factura.precioCentimos * factura.cantidad) / 100;
            return subtotal * (ivaRate / 100);
        },
        /**
         * Calcula el total (subtotal + IVA) de una factura.
         * @param {FacturaEntity} factura - Factura a calcular.
         * @returns {number} Total de la factura.
         */
        calculateTotal(factura: FacturaEntity) {
            const subtotal = (factura.precioCentimos * factura.cantidad) / 100;
            const ivaRate = factura.iva || 21; // 21% es el IVA común en España
            const iva = subtotal * (ivaRate / 100);
            return subtotal + iva;
        },
        /**
         * Formatea un número como moneda con dos decimales.
         * @param {number} amount - Cantidad a formatear.
         * @returns {string} Cantidad formateada.
         */
        formatCurrency(amount: number) {
            return amount.toFixed(2);
        },
        /**
         * Formatea una fecha a formato local según el idioma del usuario.
         * @param {string} [dateStr] - Fecha en formato ISO.
         * @returns {string} Fecha formateada.
         */
        formatDate(dateStr?: string) {
            if (!dateStr) return 'N/A';

            const store = useutf8Store();
            const userLanguage = store.currentLanguage;

            try {
                const date = new Date(dateStr);
                return new Intl.DateTimeFormat(userLanguage || 'es', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric'
                }).format(date);
            } catch (error) {
                console.error('Error formatting date:', error);
                return dateStr;
            }
        },
        /**
         * Traduce una clave usando el store de internacionalización.
         * @param {string} key - Clave de traducción.
         * @param {Record<string, any>} [params] - Parámetros para interpolación.
         * @returns {string} Traducción.
         */
        t(key: string, params?: Record<string, any>) {
            const store = useutf8Store();
            const translation = store.t(key);

            if (params) {
                return Object.entries(params).reduce((str, [key, value]) => {
                    return str.replace(`{${key}}`, String(value));
                }, translation);
            }

            return translation;
        }
    }
});
</script>

<style scoped>
/* Scoped styles if needed */
</style>
