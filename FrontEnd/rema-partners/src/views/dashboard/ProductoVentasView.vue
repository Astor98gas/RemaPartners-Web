<template>
    <div class="min-h-screen bg-gray-100 py-6">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <!-- Encabezado -->
            <div class="flex items-center justify-between pb-5 border-b border-gray-200">
                <div>
                    <router-link to="/ventas-dashboard"
                        class="inline-flex items-center text-sm text-blue-600 mb-2 hover:text-blue-800">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                        </svg>
                        {{ t('sales.stats.backToDashboard') }}
                    </router-link>
                    <h3 class="text-2xl leading-6 font-medium text-gray-900">
                        {{ t('sales.stats.title') }}
                    </h3>
                    <p v-if="productoInfo" class="mt-1 text-sm text-gray-500">
                        {{ productoInfo.titulo }}
                    </p>
                </div>
                <!-- Botón para ver el producto -->
                <div class="mt-4">
                    <router-link :to="`/producto/${productoInfo?.id}`"
                        class="inline-flex items-center px-5 py-2 bg-white border border-blue-500 text-blue-600 rounded-md hover:bg-blue-50 transition-colors duration-200 group">
                        <svg xmlns="http://www.w3.org/2000/svg"
                            class="h-5 w-5 mr-2 text-blue-500 group-hover:text-blue-600" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                        </svg>
                        <span class="font-medium">{{ t('producto.action.details') }}</span>
                    </router-link>
                </div>
            </div>

            <!-- Loading state -->
            <div v-if="loading" class="mt-6 flex justify-center">
                <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>

            <!-- Error state -->
            <div v-else-if="error" class="mt-6 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg">
                <p>{{ error }}</p>
            </div>

            <div v-else class="mt-6">
                <!-- Info básica -->
                <div class="bg-white overflow-hidden shadow rounded-lg mb-6">
                    <div class="px-4 py-5 sm:p-6">
                        <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
                            <!-- Imagen -->
                            <div class="lg:col-span-3 flex justify-center lg:justify-start">
                                <img v-if="productoInfo?.imagenes?.length > 0" :src="productoInfo.imagenes[0]"
                                    :alt="productoInfo.titulo"
                                    class="w-40 h-40 object-cover rounded-lg border border-gray-200"
                                    @error="onImageError">
                                <div v-else
                                    class="w-40 h-40 flex items-center justify-center bg-gray-200 rounded-lg text-gray-400">
                                    <!-- SVG placeholder -->
                                </div>
                            </div>

                            <!-- Detalles -->
                            <div class="lg:col-span-6">
                                <h4 class="text-lg font-semibold text-gray-900 mb-2">
                                    {{ productoInfo?.titulo || t('common.notAvailable') }}
                                </h4>
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <div>
                                        <p class="text-sm text-gray-500">{{ t('estadisticas.brand') }}</p>
                                        <p class="font-medium">{{ productoInfo?.marca || '-' }}</p>
                                    </div>
                                    <div>
                                        <p class="text-sm text-gray-500">{{ t('estadisticas.model') }}</p>
                                        <p class="font-medium">{{ productoInfo?.modelo || '-' }}</p>
                                    </div>
                                    <div>
                                        <p class="text-sm text-gray-500">{{ t('estadisticas.condition') }}</p>
                                        <p class="font-medium">{{ productoInfo?.estado || '-' }}</p>
                                    </div>
                                    <div>
                                        <p class="text-sm text-gray-500">{{ t('estadisticas.price') }}</p>
                                        <p class="font-medium">{{ formatPrecio(productoInfo?.precioCentimos,
                                            productoInfo?.moneda) }}</p>
                                    </div>
                                </div>
                            </div>

                            <!-- Estadísticas principales -->
                            <div class="lg:col-span-3 grid grid-cols-1 gap-4">
                                <VentasEstadisticasCard :titulo="t('sales.stats.totalSales')"
                                    :valor="estadisticas?.totalVentas || 0" tipo="ventas" class="w-full" />
                                <VentasEstadisticasCard :titulo="t('sales.stats.totalAmount')"
                                    :valor="(estadisticas?.importeTotal || 0) / 100" tipo="importe" formatoMoneda="true"
                                    class="w-full" />
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Gráfico de ventas -->
                <VentasChart :titulo="t('sales.stats.monthlySales')" :datos="prepareChartData()" tipoGrafico="bar"
                    :altura="300" />

                <!-- Tabla de ventas -->
                <div class="mt-6 bg-white shadow overflow-hidden sm:rounded-md mb-6">
                    <div class="px-4 py-5 border-b border-gray-200 sm:px-6">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">
                            {{ t('sales.stats.saleDetails') }}
                        </h3>
                    </div>
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th scope="col"
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        {{ t('sales.stats.table.month') }}
                                    </th>
                                    <th scope="col"
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        {{ t('sales.stats.table.year') }}
                                    </th>
                                    <th scope="col"
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        {{ t('sales.stats.table.sales') }}
                                    </th>
                                    <th scope="col"
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        {{ t('sales.stats.table.amount') }}
                                    </th>
                                    <th scope="col"
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        {{ t('sales.stats.table.lastUpdate') }}
                                    </th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <tr v-if="!ventasDetalle || ventasDetalle.length === 0">
                                    <td colspan="5" class="px-6 py-4 text-center text-sm text-gray-500">
                                        {{ t('sales.stats.noData') }}
                                    </td>
                                </tr>
                                <tr v-for="(venta, index) in ventasDetalle" :key="index"
                                    class="hover:bg-gray-50 transition-colors">
                                    <td class="px-6 py-4">
                                        <div class="text-sm text-gray-900">{{ getNombreMes(venta.mes) }}</div>
                                    </td>
                                    <td class="px-6 py-4">
                                        <div class="text-sm text-gray-900">{{ venta.año }}</div>
                                    </td>
                                    <td class="px-6 py-4">
                                        <div class="text-sm font-medium text-indigo-600">
                                            {{ venta.cantidad }}
                                        </div>
                                    </td>
                                    <td class="px-6 py-4">
                                        <div class="text-sm font-medium text-purple-600">
                                            {{ formatCurrency(venta.importe / 100) }}
                                        </div>
                                    </td>
                                    <td class="px-6 py-4">
                                        <div class="text-sm text-gray-500">{{ formatDate(venta.ultimaActualizacion) }}
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useutf8Store } from '@/stores/counter';
import ventasDashboardService from '@/services/ventas-dashboard.service';
import VentasEstadisticasCard from '@/components/dashboard/VentasEstadisticasCard.vue';
import VentasChart from '@/components/dashboard/VentasChart.vue';

export default {
    name: 'ProductoVentasView',
    components: {
        VentasEstadisticasCard,
        VentasChart
    },
    setup() {
        const store = useutf8Store();
        const t = (key) => store.t(key);

        const route = useRoute();
        const productoId = route.params.id;

        const loading = ref(true);
        const error = ref(null);
        const productoInfo = ref(null);
        const estadisticas = ref(null);
        const ventasDetalle = ref([]);

        // Meses traducidos
        const mesesTraducidos = [
            t('months.january'), t('months.february'), t('months.march'),
            t('months.april'), t('months.may'), t('months.june'),
            t('months.july'), t('months.august'), t('months.september'),
            t('months.october'), t('months.november'), t('months.december')
        ];

        const getNombreMes = (numeroMes) => {
            if (numeroMes >= 1 && numeroMes <= 12) {
                return mesesTraducidos[numeroMes - 1];
            }
            return t('common.unknown');
        };

        // Función para cargar los datos del producto y sus estadísticas de ventas
        const loadProductoStats = async () => {
            loading.value = true;
            error.value = null;

            try {
                const response = await ventasDashboardService.getProductoVentas(productoId);

                if (response) {
                    productoInfo.value = response.producto;
                    estadisticas.value = {
                        totalVentas: response.totalVentas,
                        importeTotal: response.importeTotal
                    };
                    ventasDetalle.value = response.ventasPorMes || [];
                }
            } catch (err) {
                console.error('Error al cargar estadísticas de ventas del producto', err);
                error.value = err.response?.data || 'Error al cargar los datos de ventas del producto';
            } finally {
                loading.value = false;
            }
        };

        // Preparar datos para el gráfico
        const prepareChartData = () => {
            // Preparar datos para el gráfico
            const ventasCantidadData = Array(12).fill(0);
            const ventasImporteData = Array(12).fill(0);
            const años = new Set();

            ventasDetalle.value.forEach(venta => {
                if (venta.mes >= 1 && venta.mes <= 12) {
                    ventasCantidadData[venta.mes - 1] = venta.cantidad;
                    ventasImporteData[venta.mes - 1] = (venta.importe / 100);
                    años.add(venta.año);
                }
            });

            const yearLabels = Array.from(años).sort();
            const datasets = [];

            // Si solo hay un año o ninguno, mostrar datos simples
            if (yearLabels.length <= 1) {
                datasets.push({
                    label: t('sales.dashboard.monthlySales'),
                    data: ventasCantidadData,
                    backgroundColor: 'rgba(16, 185, 129, 0.6)', // Verde
                    borderColor: 'rgba(16, 185, 129, 1)',
                    borderWidth: 1,
                    yAxisID: 'y'
                });
                datasets.push({
                    label: t('sales.stats.amount'),
                    data: ventasImporteData,
                    backgroundColor: 'rgba(124, 58, 237, 0.6)', // Púrpura
                    borderColor: 'rgba(124, 58, 237, 1)',
                    borderWidth: 1,
                    yAxisID: 'y1',
                    hidden: true // Oculto por defecto
                });
            } else {
                // Si hay múltiples años, crear un dataset por año
                const colors = [
                    { bg: 'rgba(16, 185, 129, 0.6)', border: 'rgba(16, 185, 129, 1)' }, // Verde
                    { bg: 'rgba(59, 130, 246, 0.6)', border: 'rgba(59, 130, 246, 1)' }, // Azul
                    { bg: 'rgba(245, 158, 11, 0.6)', border: 'rgba(245, 158, 11, 1)' }, // Amarillo
                ];

                yearLabels.forEach((year, index) => {
                    const yearData = Array(12).fill(0);
                    ventasDetalle.value.forEach(venta => {
                        if (venta.año === year && venta.mes >= 1 && venta.mes <= 12) {
                            yearData[venta.mes - 1] = venta.cantidad;
                        }
                    });

                    datasets.push({
                        label: `${t('sales.stats.sales')} ${year}`,
                        data: yearData,
                        backgroundColor: colors[index % colors.length].bg,
                        borderColor: colors[index % colors.length].border,
                        borderWidth: 1,
                    });
                });
            }

            return {
                labels: mesesTraducidos,
                datasets: datasets,
                isCurrency: true,
                currency: productoInfo.value?.moneda || 'EUR'
            };
        };

        // Funciones de utilidad
        const formatPrecio = (precioCentimos, moneda) => {
            if (!precioCentimos) return '-';
            const precio = precioCentimos / 100;
            return new Intl.NumberFormat('es-ES', {
                style: 'currency',
                currency: moneda || 'EUR'
            }).format(precio);
        };

        const formatCurrency = (amount) => {
            return new Intl.NumberFormat('es-ES', {
                style: 'currency',
                currency: productoInfo.value?.moneda || 'EUR',
                minimumFractionDigits: 0,
                maximumFractionDigits: 2
            }).format(amount || 0);
        };

        const formatDate = (dateString) => {
            if (!dateString) return '-';
            const date = new Date(dateString);
            return date.toLocaleString('es-ES');
        };

        const onImageError = (e) => {
            e.target.src = '/path-to-default-image.png'; // Imagen por defecto si falla la carga
        };

        // Cargar datos al montar el componente
        onMounted(() => {
            if (!productoId) {
                error.value = 'ID de producto no proporcionado';
                loading.value = false;
                return;
            }

            loadProductoStats();
        });

        return {
            t,
            loading,
            error,
            productoInfo,
            estadisticas,
            ventasDetalle,
            formatPrecio,
            formatCurrency,
            formatDate,
            getNombreMes,
            prepareChartData,
            onImageError
        };
    }
};
</script>
