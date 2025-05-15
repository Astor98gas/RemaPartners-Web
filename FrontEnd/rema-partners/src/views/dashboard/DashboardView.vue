<template>
    <div class="min-h-screen bg-gray-100 py-6">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <!-- Encabezado del Dashboard -->
            <div class="pb-5 border-b border-gray-200 sm:flex sm:items-center sm:justify-between">
                <h3 class="text-2xl leading-6 font-medium text-gray-900">
                    Dashboard - Estadísticas de Visitas
                </h3>
                <div class="mt-3 sm:mt-0 sm:ml-4">
                    <select v-model="selectedYear" @change="loadDashboardData"
                        class="inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
                        <option v-for="year in availableYears" :key="year" :value="year">{{ year }}</option>
                    </select>
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
                    <EstadisticasCard titulo="Total de Productos" :valor="dashboardData.totalProductos || 0"
                        tipo="productos" />

                    <!-- Total de Visitas -->
                    <EstadisticasCard titulo="Total de Visitas" :valor="dashboardData.totalVisitas || 0"
                        tipo="visitas" />

                    <!-- Promedio de Visitas Mensual -->
                    <EstadisticasCard titulo="Promedio Mensual" :valor="calculateMonthlyAverage()" tipo="promedio" />
                </div>

                <!-- Gráfico de visitas mensuales -->
                <div class="mt-8">
                    <VisitasChart titulo="Visitas Mensuales ({{ selectedYear }})" :datos="prepareChartData()"
                        tipoGrafico="bar" :altura="300" />
                </div>

                <!-- Tabla de productos más visitados -->
                <div class="mt-8 bg-white shadow overflow-hidden sm:rounded-md">
                    <div class="px-4 py-5 border-b border-gray-200 sm:px-6">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">
                            Productos más visitados
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
                                        Ver Producto
                                    </button>
                                </div>
                            </div>
                        </li>
                        <li v-if="!dashboardData.productosTopVisitas || dashboardData.productosTopVisitas.length === 0"
                            class="px-4 py-4 sm:px-6">
                            <div class="text-center text-gray-500">No hay productos con visitas registradas</div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import dashboardService from '@/services/dashboardService';
import EstadisticasCard from '@/components/dashboard/EstadisticasCard.vue';
import VisitasChart from '@/components/dashboard/VisitasChart.vue';

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

        const selectedYear = ref(new Date().getFullYear());
        const availableYears = ref([]);

        // Generar años disponibles (actual y 2 anteriores)
        for (let i = 0; i < 3; i++) {
            availableYears.value.push(new Date().getFullYear() - i);
        }

        const meses = [
            'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
            'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
        ];

        // Función para cargar los datos del dashboard
        const loadDashboardData = async () => {
            loading.value = true;
            error.value = null;

            try {
                const response = await dashboardService.getStats();

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

        // Preparar datos para el gráfico
        const prepareChartData = () => {
            const visitasPorMes = dashboardData.value.visitasPorMes || {};
            const dataValues = meses.map((_, index) => {
                const mesKey = (index + 1).toString();
                return visitasPorMes[mesKey] || 0;
            });

            return {
                datasets: [{
                    label: 'Visitas',
                    data: dataValues,
                    backgroundColor: 'rgba(79, 70, 229, 0.6)',
                    borderColor: 'rgba(79, 70, 229, 1)',
                    borderWidth: 1
                }]
            };
        };

        const calculateMonthlyAverage = () => {
            if (!dashboardData.value.visitasPorMes) return '0';

            const visitasPorMes = dashboardData.value.visitasPorMes;
            const mesesConVisitas = Object.values(visitasPorMes).filter(visitas => visitas > 0).length;

            if (mesesConVisitas === 0) return '0';

            const totalVisitas = Object.values(visitasPorMes).reduce((sum, visitas) => sum + visitas, 0);
            return (totalVisitas / mesesConVisitas).toFixed(1);
        };

        const goToProductDetail = (productId) => {
            router.push({ name: 'producto-estadisticas', params: { id: productId } });
        };

        // Cargar datos al montar el componente
        onMounted(() => {
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
            goToProductDetail
        };
    }
};
</script>
