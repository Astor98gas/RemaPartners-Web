<template>
    <div class="min-h-screen bg-gray-100 py-6">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <!-- Encabezado del Dashboard -->
            <div class="pb-5 border-b border-gray-200 sm:flex sm:items-center sm:justify-between">
                <h3 class="text-2xl leading-6 font-medium text-gray-900">
                    {{ t('dashboard.title') }}
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
                        {{ t('dashboard.refresh') }}
                    </button>

                    <!-- Botón para ir al perfil -->
                    <button @click="goToProfile"
                        class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                        </svg>
                        {{ t('dashboard.profile') }}
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
                <div class="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
                    <!-- Total de Productos -->
                    <EstadisticasCard :titulo="t('dashboard.totalProducts')" :valor="dashboardData.totalProductos || 0"
                        tipo="productos" />

                    <!-- Total de Visitas -->
                    <EstadisticasCard :titulo="t('dashboard.totalVisits')" :valor="dashboardData.totalVisitas || 0"
                        tipo="visitas" />

                    <!-- Promedio de Visitas Mensual -->
                    <EstadisticasCard :titulo="t('dashboard.monthlyAverage')" :valor="calculateMonthlyAverage()"
                        tipo="promedio" />
                </div>

                <!-- Gráfico de visitas mensuales -->
                <div class="mt-8">
                    <VisitasChart :titulo="t('dashboard.monthlyVisits') + ' (' + selectedYear + ')'"
                        :datos="prepareChartData()" tipoGrafico="bar" :altura="300" />
                </div>

                <!-- Tabla de productos más visitados -->
                <div class="mt-8 bg-white shadow overflow-hidden sm:rounded-md">
                    <div class="px-4 py-5 border-b border-gray-200 sm:px-6">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">
                            {{ t('dashboard.topProducts') }}
                        </h3>
                    </div>
                    <ul class="divide-y divide-gray-200">
                        <li v-for="(producto, index) in dashboardData.productosTopVisitas" :key="producto.id"
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
                                    <div class="text-sm font-semibold text-gray-900">{{ producto.visitas }} visitas
                                    </div>
                                    <button @click="goToProductDetail(producto.id)"
                                        class="ml-4 inline-flex items-center px-3 py-1 border border-transparent text-sm leading-4 font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                        {{ t('dashboard.viewProduct') }}
                                    </button>
                                </div>
                            </div>
                        </li>
                        <li v-if="!dashboardData.productosTopVisitas || dashboardData.productosTopVisitas.length === 0"
                            class="px-4 py-4 sm:px-6">
                            <div class="text-center text-gray-500">{{ t('dashboard.noProducts') }}</div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
/**
 * Vista principal del dashboard de vendedor.
 * Muestra estadísticas sobre los productos del vendedor, incluyendo:
 * - Total de productos publicados
 * - Total de visitas recibidas
 * - Promedio mensual de visitas
 * - Gráfico de visitas mensuales
 * - Productos más visitados
 * 
 * Permite filtrar por año y actualizar las estadísticas en tiempo real.
 */
import { ref, onMounted, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import dashboardService from '@/services/dashboard.service';
import EstadisticasCard from '@/components/dashboard/EstadisticasCard.vue';
import VisitasChart from '@/components/dashboard/VisitasChart.vue';
import { useutf8Store } from '@/stores/counter';

export default {
    name: 'DashboardView',
    components: {
        EstadisticasCard,
        VisitasChart
    },
    setup() {
        const router = useRouter();
        const loading = ref(true);
        const error = ref(null);
        const dashboardData = ref({
            totalProductos: 0,
            totalVisitas: 0,
            visitasPorMes: {},
            productosTopVisitas: []
        });
        const store = useutf8Store();
        const t = (key) => store.t(key);

        const selectedYear = ref(new Date().getFullYear());
        const availableYears = ref([]);

        availableYears.value.push(new Date().getFullYear());

        // Replace hardcoded months with translations
        const meses = [
            t('months.january'), t('months.february'), t('months.march'),
            t('months.april'), t('months.may'), t('months.june'),
            t('months.july'), t('months.august'), t('months.september'),
            t('months.october'), t('months.november'), t('months.december')
        ];

        /**
         * Carga los datos del dashboard filtrados por el año seleccionado.
         */
        const loadDashboardData = async () => {
            loading.value = true;
            error.value = null;

            try {
                // Modificar para enviar el año seleccionado
                const response = await dashboardService.getStats(selectedYear.value);

                if (response) {
                    dashboardData.value = response;
                }
            } catch (err) {
                console.error('Error al cargar datos del dashboard', err);
                error.value = err.response?.data || 'Error al cargar los datos del dashboard';
            } finally {
                loading.value = false;
            }
        };

        /**
         * Prepara los datos para el gráfico de visitas mensuales.
         * @returns Configuración formateada para el componente de gráfico
         */
        const prepareChartData = () => {
            // Verificar si hay datos para no causar errores
            if (!dashboardData.value || !dashboardData.value.visitasPorMes) {
                console.warn("No hay datos de visitas por mes disponibles");
                return {
                    labels: meses,
                    datasets: [{
                        label: t('dashboard.visits'),
                        data: Array(12).fill(0),
                        backgroundColor: 'rgba(79, 70, 229, 0.6)',
                        borderColor: 'rgba(79, 70, 229, 1)',
                        borderWidth: 1
                    }]
                };
            }

            const visitasPorMes = dashboardData.value.visitasPorMes;
            console.log("Datos recibidos visitasPorMes:", visitasPorMes); // Debugging

            // Crear un array con 12 posiciones (una para cada mes)
            const dataValues = Array(12).fill(0);

            // Procesar los datos según su formato
            if (typeof visitasPorMes === 'object' && !Array.isArray(visitasPorMes)) {
                // Si es un objeto con claves como {"1": 10, "2": 20}
                Object.keys(visitasPorMes).forEach(mes => {
                    const mesIndex = parseInt(mes) - 1;
                    if (mesIndex >= 0 && mesIndex < 12) {
                        dataValues[mesIndex] = visitasPorMes[mes];
                    }
                });
            } else if (Array.isArray(visitasPorMes)) {
                // Si es un array de objetos como [{mes: 1, cantidadVisitas: 10}]
                visitasPorMes.forEach(visita => {
                    if (visita.mes && visita.mes >= 1 && visita.mes <= 12) {
                        dataValues[visita.mes - 1] = visita.cantidadVisitas;
                    }
                });
            }

            return {
                labels: meses,
                datasets: [{
                    label: t('dashboard.visits'),
                    data: dataValues,
                    backgroundColor: 'rgba(79, 70, 229, 0.6)',
                    borderColor: 'rgba(79, 70, 229, 1)',
                    borderWidth: 1
                }]
            };
        };

        /**
         * Calcula el promedio mensual de visitas.
         * @returns Promedio mensual formateado
         */
        const calculateMonthlyAverage = () => {
            if (!dashboardData.value.visitasPorMes) return '0';

            const visitasPorMes = dashboardData.value.visitasPorMes;
            const mesesConVisitas = Object.values(visitasPorMes).filter(visitas => visitas > 0).length;

            if (mesesConVisitas === 0) return '0';

            const totalVisitas = Object.values(visitasPorMes).reduce((sum, visitas) => sum + visitas, 0);
            return (totalVisitas / mesesConVisitas).toFixed(1);
        };

        /**
         * Navega a la vista detallada de estadísticas de un producto específico.
         * @param productId ID del producto a visualizar
         */
        const goToProductDetail = (productId) => {
            router.push({ name: 'producto-estadisticas', params: { id: productId } });
        };

        /**
         * Actualiza los datos del dashboard.
         */
        const refreshDashboard = () => {
            loadDashboardData();
        };

        /**
         * Navega al perfil del usuario.
         */
        const goToProfile = () => {
            router.push({ name: 'profile' });
        };

        // Cargar datos al montar el componente
        onMounted(() => {
            loadDashboardData();
        });

        // Agregar esta parte después de onMounted
        watch(selectedYear, (newYear) => {
            loadDashboardData();
        });

        return {
            loading,
            error,
            dashboardData,
            selectedYear,
            availableYears,
            loadDashboardData,
            prepareChartData,
            calculateMonthlyAverage,
            goToProductDetail,
            refreshDashboard,
            goToProfile,
            t
        };
    }
};
</script>
