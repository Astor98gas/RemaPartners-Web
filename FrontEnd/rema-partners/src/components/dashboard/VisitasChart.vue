<template>
    <div class="bg-white shadow rounded-lg p-6">
        <h4 class="text-lg font-medium text-gray-900 mb-4">{{ titulo }}</h4>
        <div :class="containerClass">
            <canvas ref="chartCanvas"></canvas>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount, watch, computed } from 'vue';
import Chart from 'chart.js/auto';
import { useutf8Store } from '@/stores/counter';

/**
 * Componente VisitasChart
 * Muestra un gráfico (barra, línea o pastel) de visitas mensuales.
 *
 * @component
 * @prop {String} [titulo='Visitas Mensuales'] - Título del gráfico.
 * @prop {Object} datos - Datos del gráfico (labels y datasets).
 * @prop {String} [tipoGrafico='bar'] - Tipo de gráfico ('bar', 'line', 'pie').
 * @prop {Number} [altura=300] - Altura del gráfico en píxeles.
 * @prop {Array} [etiquetas=null] - Etiquetas personalizadas para el eje X.
 */
export default {
    name: 'VisitasChart',
    props: {
        /**
         * Título del gráfico.
         */
        titulo: {
            type: String,
            default: 'Visitas Mensuales'
        },
        /**
         * Datos del gráfico (labels y datasets).
         */
        datos: {
            type: Object,
            required: true
        },
        /**
         * Tipo de gráfico a mostrar.
         */
        tipoGrafico: {
            type: String,
            default: 'bar',
            validator: (value) => ['bar', 'line', 'pie'].includes(value)
        },
        /**
         * Altura del gráfico en píxeles.
         */
        altura: {
            type: Number,
            default: 300
        },
        /**
         * Etiquetas personalizadas para el eje X.
         */
        etiquetas: {
            type: Array,
            default: null
        }
    },
    setup(props) {
        /**
         * Referencia al canvas del gráfico.
         * @type {Ref<HTMLCanvasElement>}
         */
        const chartCanvas = ref(null);
        /**
         * Instancia del gráfico Chart.js.
         * @type {Ref<Chart|null>}
         */
        const chartInstance = ref(null);
        const store = useutf8Store();
        const t = (key) => store.t(key);

        // Obtener las traducciones de los meses
        const mesesTraducidos = [
            t('months.january'), t('months.february'), t('months.march'),
            t('months.april'), t('months.may'), t('months.june'),
            t('months.july'), t('months.august'), t('months.september'),
            t('months.october'), t('months.november'), t('months.december')
        ];

        // Altura fija (corregida para que funcione con Tailwind)
        const containerClass = computed(() => `h-[${props.altura}px] relative`);

        /**
         * Crea o actualiza el gráfico con los datos actuales.
         */
        const createChart = () => {
            console.log("Datos recibidos en VisitasChart:", props.datos);

            if (!chartCanvas.value) {
                console.warn("Canvas no encontrado");
                return;
            }

            // Limpiar gráfico anterior si existe
            if (chartInstance.value) {
                chartInstance.value.destroy();
                chartInstance.value = null;
            }

            const ctx = chartCanvas.value.getContext('2d');

            // Configuración predeterminada por tipo de gráfico
            const configs = {
                bar: {
                    borderRadius: 4,
                    barThickness: 'flex',
                },
                line: {
                    tension: 0.4,
                    fill: false,
                    pointRadius: 4
                },
                pie: {
                    circumference: 360,
                    radius: '70%'
                }
            };

            // Opciones comunes
            const options = {
                responsive: true,
                maintainAspectRatio: false,
                scales: props.tipoGrafico !== 'pie' ? {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            precision: 0
                        }
                    }
                } : undefined,
                plugins: {
                    legend: {
                        display: props.datos.datasets && props.datos.datasets.length > 1
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                const label = context.dataset.label || '';
                                return `${label}: ${context.raw} ${t('dashboard.visits')}`;
                            }
                        }
                    }
                }
            };

            try {
                // Asegurarse de que tenemos datos válidos
                if (!props.datos || !props.datos.datasets || !Array.isArray(props.datos.datasets)) {
                    console.error("Formato de datos incorrecto:", props.datos);
                    // Crear un gráfico vacío para evitar errores
                    chartInstance.value = new Chart(ctx, {
                        type: props.tipoGrafico,
                        data: {
                            labels: props.etiquetas || mesesTraducidos,
                            datasets: [{
                                label: t('dashboard.visits'),
                                data: Array(12).fill(0),
                                backgroundColor: 'rgba(79, 70, 229, 0.6)',
                                borderColor: 'rgba(79, 70, 229, 1)',
                                borderWidth: 1,
                                ...configs[props.tipoGrafico]
                            }]
                        },
                        options: options
                    });
                    return;
                }

                // Crear el gráfico con los datos proporcionados
                chartInstance.value = new Chart(ctx, {
                    type: props.tipoGrafico,
                    data: {
                        labels: props.datos.labels || props.etiquetas || mesesTraducidos,
                        datasets: props.datos.datasets.map(dataset => ({
                            ...dataset,
                            ...configs[props.tipoGrafico]
                        }))
                    },
                    options: options
                });

                console.log("Gráfico creado exitosamente:", chartInstance.value);
            } catch (error) {
                console.error("Error al crear el gráfico:", error);
            }
        };

        // Recrear el gráfico cuando cambian los datos o el idioma
        watch([() => props.datos, () => store.getCurrentLanguage], () => {
            console.log("Datos o idioma actualizados, recreando gráfico");
            // Usar setTimeout para asegurar que el DOM esté actualizado
            setTimeout(() => {
                createChart();
            }, 0);
        }, { deep: true });

        // Inicializar gráfico al montar el componente
        onMounted(() => {
            console.log("Componente montado, creando gráfico inicial");
            // Usar setTimeout para asegurar que el canvas esté renderizado
            setTimeout(() => {
                createChart();
            }, 0);
        });

        // Limpiar gráfico al desmontar
        onBeforeUnmount(() => {
            if (chartInstance.value) {
                chartInstance.value.destroy();
            }
        });

        return {
            chartCanvas,
            containerClass
        };
    }
};
</script>
