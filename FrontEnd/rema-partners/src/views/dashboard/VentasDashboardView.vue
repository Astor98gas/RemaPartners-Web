<template>
    <div class="min-h-screen bg-gray-100 py-6">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <!-- Encabezado del Dashboard -->
            <div class="pb-5 border-b border-gray-200 sm:flex sm:items-center sm:justify-between">
                <h3 class="text-2xl leading-6 font-medium text-gray-900">
                    {{ t('sales.dashboard.title') }}
                </h3>
                <div class="mt-3 sm:mt-0 flex items-center space-x-3">
                    <!-- Selector de año -->
                    <select v-model="selectedYear" @change="loadDashboardData"
                        class="inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
                        <option v-for="year in availableYears" :key="year" :value="year">{{ year }}</option>
                    </select>

                    <!-- Botón para recargar dashboard -->
                    <button @click="refreshDashboard"
                        class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                        </svg>
                        {{ t('sales.dashboard.refresh') }}
                    </button>

                    <!-- Botón para ir a facturas -->
                    <button @click="goToFacturas"
                        class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                        </svg>
                        {{ t('profile.invoices') }}
                    </button>
                </div>
            </div>

            <!-- Contenido principal -->
            <div v-if="loading" class="mt-6 flex justify-center">
                <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>

            <div v-else-if="error" class="mt-6 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg">
                <p>{{ error }}</p>
            </div>

            <div v-else class="mt-6">
                <!-- Tarjetas de resumen -->
                <div class="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
                    <!-- Total de Ventas -->
                    <VentasEstadisticasCard :titulo="t('sales.dashboard.totalSales')"
                        :valor="dashboardData.totalVentas || 0" tipo="ventas" />

                    <!-- Total de Compras -->
                    <VentasEstadisticasCard :titulo="t('sales.dashboard.totalPurchases')"
                        :valor="dashboardData.totalCompras || 0" tipo="compras" />

                    <!-- Importe Total de Ventas -->
                    <VentasEstadisticasCard :titulo="t('sales.stats.table.amount')"
                        :valor="(dashboardData.importeTotalVentas || 0) / 100" tipo="importe" formatoMoneda="true"
                        :moneda="dashboardData.moneda" />

                    <!-- Importe Promedio por Venta -->
                    <VentasEstadisticasCard :titulo="t('sales.dashboard.averageAmount')"
                        :valor="calculateAverageAmount() / 100" tipo="otros" formatoMoneda="true" />
                </div>

                <!-- Gráfico de ventas mensuales -->
                <div class="mt-8 grid grid-cols-1 lg:grid-cols-2 gap-8">
                    <!-- Ventas mensuales -->
                    <VentasChart :titulo="t('sales.dashboard.monthlySales') + ' (' + selectedYear + ')'"
                        :datos="prepareSalesChartData()" tipoGrafico="bar" :altura="320" />

                    <!-- Compras mensuales -->
                    <VentasChart :titulo="t('sales.dashboard.monthlyPurchases') + ' (' + selectedYear + ')'"
                        :datos="preparePurchasesChartData()" tipoGrafico="bar" :altura="320" />
                </div>

                <!-- Tabla de productos más vendidos -->
                <div class="mt-8 bg-white shadow overflow-hidden sm:rounded-md">
                    <div class="px-4 py-5 border-b border-gray-200 sm:px-6">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">
                            {{ t('sales.dashboard.topProducts') }}
                        </h3>
                    </div>
                    <ul class="divide-y divide-gray-200">
                        <li v-for="(producto, index) in dashboardData.productosTopVentas" :key="producto.id"
                            class="px-4 py-4 sm:px-6 hover:bg-gray-50">
                            <div class="flex items-center justify-between">
                                <div class="flex items-center">
                                    <div
                                        class="flex-shrink-0 h-10 w-10 flex items-center justify-center bg-gray-200 rounded-full text-gray-500">
                                        {{ index + 1 }}
                                    </div>
                                    <div class="ml-4">
                                        <div class="text-sm font-medium text-indigo-600">{{ producto.titulo }}</div>
                                        <div class="text-sm text-gray-500">ID: {{ producto.id }}</div>
                                    </div>
                                </div>
                                <div class="flex items-center">
                                    <div class="flex flex-col mr-4 text-right">
                                        <div class="text-sm font-semibold text-gray-900">
                                            {{ producto.cantidadVentas }} {{ t('dashboard.sales') }}
                                        </div>
                                        <div class="text-sm text-gray-600">
                                            {{ formatCurrency(producto.importeTotal / 100, producto.moneda) }}
                                        </div>
                                    </div>
                                    <button @click="goToProductDetail(producto.id)"
                                        class="inline-flex items-center px-3 py-1 border border-transparent text-sm leading-4 font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                        {{ t('sales.dashboard.viewDetails') }}
                                    </button>
                                </div>
                            </div>
                        </li>
                        <li v-if="!dashboardData.productosTopVentas || dashboardData.productosTopVentas.length === 0"
                            class="px-4 py-4 sm:px-6">
                            <div class="text-center text-gray-500">{{ t('sales.dashboard.noSales') }}</div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
/**
 * Vista de dashboard de ventas.
 * Muestra estadísticas detalladas sobre las ventas y compras del usuario, incluyendo:
 * - Total de ventas realizadas
 * - Total de compras realizadas
 * - Importe total de ventas
 * - Importe promedio por venta
 * - Gráficos de ventas y compras mensuales
 * - Listado de productos más vendidos
 * 
 * Permite filtrar por año y actualizar las estadísticas en tiempo real.
 */
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useutf8Store } from '@/stores/counter';
import { useUsers } from '@/composables/useUsers';
import ventasDashboardService from '@/services/ventas-dashboard.service';
import VentasEstadisticasCard from '@/components/dashboard/VentasEstadisticasCard.vue';
import VentasChart from '@/components/dashboard/VentasChart.vue';

export default {
    name: 'VentasDashboardView',
    components: {
        VentasEstadisticasCard,
        VentasChart
    },
    setup() {
        const router = useRouter();
        const loading = ref(true);
        const error = ref(null);
        const dashboardData = ref({
            totalVentas: 0,
            totalCompras: 0,
            importeTotalVentas: 0,
            importeTotalCompras: 0,
            ventasPorMes: {},
            comprasPorMes: {},
            productosTopVentas: []
        });
        const store = useutf8Store();
        const t = (key) => store.t(key);
        const currentUser = ref(null);

        // Años disponibles para el filtro (últimos 3 años)
        const currentYear = new Date().getFullYear();
        const selectedYear = ref(currentYear);
        const availableYears = ref([currentYear, currentYear - 1, currentYear - 2]);

        // Meses traducidos
        const meses = [
            t('months.january'), t('months.february'), t('months.march'),
            t('months.april'), t('months.may'), t('months.june'),
            t('months.july'), t('months.august'), t('months.september'),
            t('months.october'), t('months.november'), t('months.december')
        ];

        /**
         * Carga los datos del usuario actual.
         * @returns Datos del usuario autenticado o null si hay error
         */
        const loadUserData = async () => {
            try {
                const usersComposable = useUsers();
                const userData = await usersComposable.isLoggedIn();
                currentUser.value = userData;
                return userData;
            } catch (err) {
                console.error('Error loading user data:', err);
                error.value = err.message || 'Error loading user data';
                return null;
            }
        };

        /**
         * Carga los datos del dashboard filtrados por el año seleccionado.
         */
        const loadDashboardData = async () => {
            loading.value = true;
            error.value = null;

            try {
                // Asegurar que tengamos los datos del usuario
                if (!currentUser.value) {
                    const userData = await loadUserData();
                    if (!userData) return;
                }

                // Cargar estadísticas de ventas y compras
                const userId = currentUser.value.id.toString();
                const [ventasResponse, comprasResponse] = await Promise.all([
                    ventasDashboardService.getVentasUsuario(userId, selectedYear.value),
                    ventasDashboardService.getComprasUsuario(userId, selectedYear.value)
                ]);

                // Combinar los datos
                dashboardData.value = {
                    totalVentas: ventasResponse.totalVentas || 0,
                    totalCompras: comprasResponse.totalCompras || 0,
                    importeTotalVentas: ventasResponse.importeTotalVentas || 0,
                    importeTotalCompras: comprasResponse.importeTotalCompras || 0,
                    ventasPorMes: ventasResponse.ventasPorMes || {},
                    comprasPorMes: comprasResponse.comprasPorMes || {},
                    productosTopVentas: ventasResponse.productosTopVentas || []
                };

            } catch (err) {
                console.error('Error al cargar datos del dashboard de ventas:', err);
                error.value = err.response?.data || 'Error al cargar los datos del dashboard de ventas';
            } finally {
                loading.value = false;
            }
        };

        /**
         * Prepara los datos para el gráfico de ventas mensuales.
         * @returns Configuración formateada para el componente de gráfico
         */
        const prepareSalesChartData = () => {
            // Verificar si hay datos para no causar errores
            if (!dashboardData.value || !dashboardData.value.ventasPorMes) {
                console.warn("No hay datos de ventas por mes disponibles");
                return {
                    labels: meses,
                    datasets: [{
                        label: t('dashboard.sales'),
                        data: Array(12).fill(0),
                        backgroundColor: 'rgba(16, 185, 129, 0.6)', // Verde
                        borderColor: 'rgba(16, 185, 129, 1)',
                        borderWidth: 1
                    }],
                    isCurrency: true,
                    currency: 'EUR'
                };
            }

            const ventasPorMes = dashboardData.value.ventasPorMes;

            // Crear arrays con 12 posiciones (una para cada mes)
            const cantidadData = Array(12).fill(0);
            const importeData = Array(12).fill(0);

            // Procesar los datos según su formato
            if (typeof ventasPorMes === 'object' && !Array.isArray(ventasPorMes)) {
                Object.keys(ventasPorMes).forEach(mes => {
                    const mesIndex = parseInt(mes) - 1;
                    if (mesIndex >= 0 && mesIndex < 12) {
                        cantidadData[mesIndex] = ventasPorMes[mes].cantidad || 0;
                        importeData[mesIndex] = (ventasPorMes[mes].importe || 0) / 100;
                    }
                });
            } else if (Array.isArray(ventasPorMes)) {
                ventasPorMes.forEach(venta => {
                    if (venta.mes && venta.mes >= 1 && venta.mes <= 12) {
                        cantidadData[venta.mes - 1] = venta.cantidad || 0;
                        importeData[venta.mes - 1] = venta.importe || 0;
                    }
                });
            }

            return {
                labels: meses,
                datasets: [
                    {
                        label: t('dashboard.sales'),
                        data: cantidadData,
                        backgroundColor: 'rgba(16, 185, 129, 0.6)', // Verde
                        borderColor: 'rgba(16, 185, 129, 1)',
                        borderWidth: 1,
                        yAxisID: 'y'
                    },
                    {
                        label: t('sales.stats.amount'),
                        data: importeData,
                        backgroundColor: 'rgba(79, 70, 229, 0.6)', // Índigo
                        borderColor: 'rgba(79, 70, 229, 1)',
                        borderWidth: 1,
                        yAxisID: 'y1',
                        hidden: true // Por defecto oculto para no saturar la vista
                    }
                ],
                isCurrency: true,
                currency: 'EUR'
            };
        };

        /**
         * Prepara los datos para el gráfico de compras mensuales.
         * @returns Configuración formateada para el componente de gráfico
         */
        const preparePurchasesChartData = () => {
            // Verificar si hay datos para no causar errores
            if (!dashboardData.value || !dashboardData.value.comprasPorMes) {
                console.warn("No hay datos de compras por mes disponibles");
                return {
                    labels: meses,
                    datasets: [{
                        label: t('dashboard.purchases'),
                        data: Array(12).fill(0),
                        backgroundColor: 'rgba(59, 130, 246, 0.6)', // Azul
                        borderColor: 'rgba(59, 130, 246, 1)',
                        borderWidth: 1
                    }],
                    isCurrency: true,
                    currency: 'EUR'
                };
            }

            const comprasPorMes = dashboardData.value.comprasPorMes;

            // Crear arrays con 12 posiciones (una para cada mes)
            const cantidadData = Array(12).fill(0);
            const importeData = Array(12).fill(0);

            // Procesar los datos según su formato
            if (typeof comprasPorMes === 'object' && !Array.isArray(comprasPorMes)) {
                Object.keys(comprasPorMes).forEach(mes => {
                    const mesIndex = parseInt(mes) - 1;
                    if (mesIndex >= 0 && mesIndex < 12) {
                        cantidadData[mesIndex] = comprasPorMes[mes].cantidad || 0;
                        importeData[mesIndex] = (comprasPorMes[mes].importe || 0) / 100;
                    }
                });
            } else if (Array.isArray(comprasPorMes)) {
                comprasPorMes.forEach(compra => {
                    if (compra.mes && compra.mes >= 1 && compra.mes <= 12) {
                        cantidadData[compra.mes - 1] = compra.cantidad || 0;
                        importeData[compra.mes - 1] = compra.importe || 0;
                    }
                });
            }

            return {
                labels: meses,
                datasets: [
                    {
                        label: t('dashboard.purchases'),
                        data: cantidadData,
                        backgroundColor: 'rgba(59, 130, 246, 0.6)', // Azul
                        borderColor: 'rgba(59, 130, 246, 1)',
                        borderWidth: 1,
                        yAxisID: 'y'
                    },
                    {
                        label: t('sales.stats.amount'),
                        data: importeData,
                        backgroundColor: 'rgba(124, 58, 237, 0.6)', // Púrpura
                        borderColor: 'rgba(124, 58, 237, 1)',
                        borderWidth: 1,
                        yAxisID: 'y1',
                        hidden: true // Por defecto oculto para no saturar la vista
                    }
                ],
                isCurrency: true,
                currency: 'EUR'
            };
        };

        /**
         * Calcula el importe promedio por venta.
         * @returns Importe promedio calculado
         */
        const calculateAverageAmount = () => {
            const totalVentas = dashboardData.value.totalVentas || 0;
            const importeTotal = dashboardData.value.importeTotalVentas || 0;

            if (totalVentas === 0) return 0;

            return importeTotal / totalVentas;
        };

        /**
         * Formatea un valor numérico como moneda.
         * @param amount Cantidad a formatear
         * @param moneda Código de moneda (EUR por defecto)
         * @returns Valor formateado como moneda
         */
        const formatCurrency = (amount, moneda) => {
            return new Intl.NumberFormat('es-ES', {
                style: 'currency',
                currency: moneda || 'EUR',
                minimumFractionDigits: 0,
                maximumFractionDigits: 2
            }).format(amount || 0);
        };

        /**
         * Navega a la vista detallada de ventas de un producto específico.
         * @param productId ID del producto a visualizar
         */
        const goToProductDetail = (productId) => {
            router.push({ name: 'producto-ventas', params: { id: productId } });
        };

        /**
         * Actualiza los datos del dashboard.
         */
        const refreshDashboard = () => {
            loadDashboardData();
        };

        /**
         * Navega a la vista de facturas.
         */
        const goToFacturas = () => {
            router.push({ name: 'facturas' });
        };

        // Cargar datos al montar el componente
        onMounted(async () => {
            await loadUserData();
            await loadDashboardData();
        });

        // Recargar cuando cambie el año seleccionado
        watch(selectedYear, () => {
            loadDashboardData();
        });

        return {
            loading,
            error,
            dashboardData,
            selectedYear,
            availableYears,
            loadDashboardData,
            prepareSalesChartData,
            preparePurchasesChartData,
            calculateAverageAmount,
            goToProductDetail,
            refreshDashboard,
            goToFacturas,
            formatCurrency,
            t
        };
    }
};
</script>
