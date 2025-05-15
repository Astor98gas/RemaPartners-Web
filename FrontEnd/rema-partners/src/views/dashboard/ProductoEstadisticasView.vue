<template>
    <div class="min-h-screen bg-gray-100 py-6">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <!-- Encabezado -->
            <div class="flex items-center justify-between pb-5 border-b border-gray-200">
                <div>
                    <router-link to="/dashboard"
                        class="inline-flex items-center text-sm text-blue-600 mb-2 hover:text-blue-800">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                        </svg>
                        {{ t('estadisticas.backToDashboard') }}
                    </router-link>
                    <h3 class="text-2xl leading-6 font-medium text-gray-900">
                        {{ t('estadisticas.title') }}
                    </h3>
                    <p v-if="productoInfo" class="mt-1 text-sm text-gray-500">
                        {{ productoInfo.titulo }}
                    </p>
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
                        <div class="flex flex-col md:flex-row">
                            <!-- Imagen -->
                            <div class="flex-shrink-0 mb-4 md:mb-0 md:mr-6">
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
                            <div class="flex-1">
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
                            <EstadisticasCard :titulo="t('estadisticas.totalVisits')"
                                :valor="estadisticas?.totalVisitas || 0" tipo="visitas" />
                        </div>
                    </div>
                </div>

                <!-- Gráfico de visitas -->
                <VisitasChart :titulo="t('estadisticas.monthlyVisits')" :datos="prepareChartData()" tipoGrafico="bar"
                    :altura="300" />

                <!-- Tabla de visitas -->
                <div class="mt-6 bg-white shadow overflow-hidden sm:rounded-md mb-6">
                    <div class="px-4 py-5 border-b border-gray-200 sm:px-6">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">
                            {{ t('estadisticas.visitDetails') }}
                        </h3>
                    </div>
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th scope="col"
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        {{ t('estadisticas.table.month') }}
                                    </th>
                                    <th scope="col"
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        {{ t('estadisticas.table.year') }}
                                    </th>
                                    <th scope="col"
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        {{ t('estadisticas.table.visits') }}
                                    </th>
                                    <th scope="col"
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        {{ t('estadisticas.table.lastUpdate') }}
                                    </th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <tr v-if="!visitasDetalle || visitasDetalle.length === 0">
                                    <td colspan="4" class="px-6 py-4 text-center text-sm text-gray-500">
                                        {{ t('estadisticas.noData') }}
                                    </td>
                                </tr>
                                <tr v-for="(visita, index) in visitasDetalle" :key="index">
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="text-sm text-gray-900">{{ getNombreMes(visita.mes) }}</span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="text-sm text-gray-900">{{ visita.año }}</span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="text-sm font-medium text-indigo-600">{{ visita.cantidadVisitas
                                            }}</span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {{ formatDate(visita.ultimaActualizacion) }}
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
import { useRoute, useRouter } from 'vue-router';
import { useutf8Store } from '@/stores/counter';
import dashboardService from '@/services/dashboardService';
import EstadisticasCard from '@/components/dashboard/EstadisticasCard.vue';
import VisitasChart from '@/components/dashboard/VisitasChart.vue';

export default {
    name: 'ProductoEstadisticasView',
    components: {
        EstadisticasCard,
        VisitasChart
    },
    setup() {
        const store = useutf8Store();
        const t = (key) => store.t(key);

        const route = useRoute();
        const router = useRouter();
        const productoId = route.params.id;

        const loading = ref(true);
        const error = ref(null);
        const productoInfo = ref(null);
        const estadisticas = ref(null);
        const visitasDetalle = ref([]);

        const meses = [
            'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
            'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
        ];

        // Función para cargar los datos del producto y sus estadísticas
        const loadProductoStats = async () => {
            loading.value = true;
            error.value = null;

            try {
                const response = await dashboardService.getProductoVisitas(productoId);

                if (response) {
                    productoInfo.value = response.producto;
                    estadisticas.value = {
                        totalVisitas: response.totalVisitas
                    };
                    visitasDetalle.value = response.visitasPorMes || [];
                }
            } catch (err) {
                console.error('Error al cargar estadísticas del producto', err);
                error.value = err.response?.data || 'Error al cargar los datos del producto';
            } finally {
                loading.value = false;
            }
        };

        // Preparar datos para el gráfico
        const prepareChartData = () => {
            // Preparar datos para el gráfico
            const visitasData = Array(12).fill(0);
            const años = new Set();

            visitasDetalle.value.forEach(visita => {
                if (visita.mes >= 1 && visita.mes <= 12) {
                    visitasData[visita.mes - 1] = visita.cantidadVisitas;
                    años.add(visita.año);
                }
            });

            const yearLabels = Array.from(años).sort();
            const datasets = [];

            // Si solo hay un año o ninguno, mostrar datos simples
            if (yearLabels.length <= 1) {
                datasets.push({
                    label: yearLabels[0] || 'Visitas',
                    data: visitasData,
                    backgroundColor: 'rgba(79, 70, 229, 0.6)',
                    borderColor: 'rgba(79, 70, 229, 1)',
                    borderWidth: 1,
                });
            } else {
                // Si hay múltiples años, crear un dataset por año
                const colors = [
                    { bg: 'rgba(79, 70, 229, 0.6)', border: 'rgba(79, 70, 229, 1)' },
                    { bg: 'rgba(16, 185, 129, 0.6)', border: 'rgba(16, 185, 129, 1)' },
                    { bg: 'rgba(245, 158, 11, 0.6)', border: 'rgba(245, 158, 11, 1)' },
                ];

                yearLabels.forEach((year, index) => {
                    const yearData = Array(12).fill(0);
                    visitasDetalle.value.forEach(visita => {
                        if (visita.año === year && visita.mes >= 1 && visita.mes <= 12) {
                            yearData[visita.mes - 1] = visita.cantidadVisitas;
                        }
                    });

                    datasets.push({
                        label: `${year}`,
                        data: yearData,
                        backgroundColor: colors[index % colors.length].bg,
                        borderColor: colors[index % colors.length].border,
                        borderWidth: 1,
                    });
                });
            }

            return { datasets };
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

        const formatDate = (dateString) => {
            if (!dateString) return '-';
            const date = new Date(dateString);
            return date.toLocaleString('es-ES');
        };

        const getNombreMes = (numeroMes) => {
            if (numeroMes >= 1 && numeroMes <= 12) {
                return meses[numeroMes - 1];
            }
            return 'Desconocido';
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
            visitasDetalle,
            formatPrecio,
            formatDate,
            getNombreMes,
            prepareChartData
        };
    }
};
</script>
