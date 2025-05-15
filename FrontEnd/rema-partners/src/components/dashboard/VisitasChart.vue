<template>
    <div class="bg-white shadow rounded-lg p-6">
        <h4 class="text-lg font-medium text-gray-900 mb-4">{{ titulo }}</h4>
        <div :class="containerClass">
            <canvas ref="chartCanvas"></canvas>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import Chart from 'chart.js/auto';

export default {
    name: 'VisitasChart',
    props: {
        titulo: {
            type: String,
            default: 'Visitas Mensuales'
        },
        datos: {
            type: Object,
            required: true
        },
        tipoGrafico: {
            type: String,
            default: 'bar',
            validator: (value) => ['bar', 'line', 'pie'].includes(value)
        },
        altura: {
            type: Number,
            default: 300
        },
        etiquetas: {
            type: Array,
            default: () => [
                'Enero', 'Febrero', 'Marzo', 'Abril',
                'Mayo', 'Junio', 'Julio', 'Agosto',
                'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
            ]
        }
    },
    setup(props) {
        const chartCanvas = ref(null);
        const chartInstance = ref(null);

        // Aseguramos una altura fija utilizando el valor proporcionado
        const containerClass = `h-[${props.altura}px] relative`;

        const createChart = () => {
            if (!chartCanvas.value) return;

            // Limpiar gráfico anterior si existe
            if (chartInstance.value) {
                chartInstance.value.destroy();
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
                                return `${label}: ${context.raw} visitas`;
                            }
                        }
                    }
                }
            };

            // Crear el gráfico
            chartInstance.value = new Chart(ctx, {
                type: props.tipoGrafico,
                data: {
                    labels: props.etiquetas,
                    datasets: props.datos.datasets || [{
                        label: 'Visitas',
                        data: Array.isArray(props.datos) ? props.datos : [props.datos],
                        backgroundColor: 'rgba(79, 70, 229, 0.6)',
                        borderColor: 'rgba(79, 70, 229, 1)',
                        borderWidth: 1,
                        ...configs[props.tipoGrafico]
                    }]
                },
                options: options
            });
        };

        // Recrear el gráfico cuando cambian los datos
        watch(() => props.datos, () => {
            createChart();
        }, { deep: true });

        // Inicializar gráfico al montar el componente
        onMounted(() => {
            createChart();
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
