<template>
    <div class="mb-6">
        <label :for="id" class="block text-gray-700 font-semibold mb-2">
            {{ t('producto.direccion') }}
        </label>

        <!-- Campo de búsqueda -->
        <div class="mb-3">
            <div class="relative w-full">
                <input :id="id" v-model="direccionBusqueda"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                    :placeholder="t('producto.direccionPlaceholder')" @blur="buscarDireccion" />
            </div>
        </div>

        <!-- Contenedor del mapa -->
        <div :id="`map-container-${id}`"
            class="w-full h-64 rounded-lg border border-gray-300 overflow-hidden shadow-sm relative"
            style="height: 300px; min-height: 300px; position: relative; z-index: 0;">
            <div v-if="!mapInitialized" class="absolute inset-0 flex items-center justify-center bg-gray-50">
                <svg class="animate-spin h-8 w-8 text-blue-500" xmlns="http://www.w3.org/2000/svg" fill="none"
                    viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                    </path>
                </svg>
            </div>
        </div>

        <!-- Dirección seleccionada -->
        <div v-if="modelValue" class="mt-3 p-3 bg-blue-50 rounded-lg border border-blue-100 text-blue-800">
            <div class="flex items-start">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 mt-0.5 text-blue-600 flex-shrink-0"
                    fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                <span>{{ modelValue }}</span>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, onUnmounted, ref, watch, computed } from 'vue';
import { useutf8Store } from '@/stores/counter';
import 'leaflet/dist/leaflet.css';
import * as L from 'leaflet';

// Importar imágenes de marcadores para Leaflet (necesarias en Vite)
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';

export default defineComponent({
    name: 'LocationSelector',
    props: {
        modelValue: {
            type: String,
            default: ''
        },
        id: {
            type: String,
            default: 'location-selector'
        }
    },
    emits: ['update:modelValue'],
    setup(props, { emit }) {
        const map = ref<L.Map | null>(null);
        const marker = ref<L.Marker | null>(null);
        const direccionBusqueda = ref(props.modelValue);
        const mapInitialized = ref(false);
        const coordenadas = ref({
            lat: 40.416775, // Coordenadas por defecto (Madrid)
            lng: -3.703790
        });

        // Cuando cambia el modelValue externo, actualizar el campo de búsqueda
        watch(() => props.modelValue, (newValue) => {
            direccionBusqueda.value = newValue;
        });

        // Cuando el usuario cambia el campo de búsqueda
        watch(direccionBusqueda, (newValue) => {
            if (newValue !== props.modelValue) {
                emit('update:modelValue', newValue);
            }
        });

        // Inicializar el mapa
        const initMap = () => {
            const container = document.getElementById(`map-container-${props.id}`);
            if (!container) return;

            // Configurar iconos personalizados para Leaflet
            delete (L.Icon.Default.prototype as any)._getIconUrl;
            L.Icon.Default.mergeOptions({
                iconRetinaUrl: markerIcon2x,
                iconUrl: markerIcon,
                shadowUrl: markerShadow,
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });

            // Crear mapa
            map.value = L.map(`map-container-${props.id}`, {
                center: [coordenadas.value.lat, coordenadas.value.lng],
                zoom: 10,
                zoomControl: true
            });

            // Añadir capa de mosaicos
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; OpenStreetMap contributors'
            }).addTo(map.value as L.Map);

            // Añadir marcador inicial si hay dirección
            if (props.modelValue) {
                buscarDireccion();
            } else {
                addOrMoveMarker(coordenadas.value.lat, coordenadas.value.lng);
            }

            // Configurar evento de clic
            map.value.on('click', handleMapClick);

            // Forzar actualización del tamaño del mapa
            setTimeout(() => {
                if (map.value) {
                    map.value.invalidateSize();
                    mapInitialized.value = true;
                }
            }, 300);
        };

        // Añadir o mover marcador
        const addOrMoveMarker = (lat: number, lng: number, popupText?: string) => {
            if (!map.value) return;

            if (marker.value) {
                marker.value.setLatLng([lat, lng]);
            } else {
                marker.value = L.marker([lat, lng]).addTo(map.value as L.Map);
            }

            if (popupText) {
                marker.value.bindPopup(popupText).openPopup();
            }
        };

        // Manejar clic en el mapa
        const handleMapClick = (e: L.LeafletMouseEvent) => {
            const { lat, lng } = e.latlng;
            addOrMoveMarker(lat, lng);
            obtenerDireccionDesdeLatLng(lat, lng);
        };

        // Buscar dirección desde texto
        const buscarDireccion = async () => {
            if (!direccionBusqueda.value.trim()) return;

            try {
                const response = await fetch(
                    `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(direccionBusqueda.value)}&limit=1`
                );
                const data = await response.json();

                if (data && data.length > 0) {
                    const { lat, lon } = data[0];
                    const latitude = parseFloat(lat);
                    const longitude = parseFloat(lon);

                    coordenadas.value = { lat: latitude, lng: longitude };

                    if (map.value) {
                        map.value.setView([latitude, longitude], 13);
                        addOrMoveMarker(latitude, longitude);
                    }

                    await obtenerDireccionDesdeLatLng(latitude, longitude);
                }
            } catch (error) {
                console.error('Error al buscar dirección:', error);
            }
        };

        // Obtener ciudad desde coordenadas
        const obtenerDireccionDesdeLatLng = async (lat: number, lng: number) => {
            try {
                const response = await fetch(
                    `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&zoom=10&addressdetails=1`
                );
                const data = await response.json();

                if (data && data.address) {
                    const address = data.address;
                    let ciudad = '';

                    // Extraer nombre de ciudad en orden de prioridad
                    if (address.city) ciudad = address.city;
                    else if (address.town) ciudad = address.town;
                    else if (address.village) ciudad = address.village;
                    else if (address.municipality) ciudad = address.municipality;
                    else if (address.county) ciudad = address.county;

                    // Si no hay ciudad, usar primera parte del nombre completo
                    if (!ciudad && data.display_name) {
                        ciudad = data.display_name.split(',')[0];
                    }

                    emit('update:modelValue', ciudad);
                    direccionBusqueda.value = ciudad;

                    if (marker.value) {
                        marker.value.bindPopup(ciudad).openPopup();
                    }
                }
            } catch (error) {
                console.error('Error al obtener dirección:', error);
            }
        };

        // Traducción mediante el store
        const t = (key: string): string => {
            const store = useutf8Store();
            return store.t(key);
        };

        // Ciclo de vida del componente
        onMounted(() => {
            // Inicializar mapa después de renderizar el componente
            setTimeout(initMap, 100);
        });

        onUnmounted(() => {
            // Limpiar mapa al destruir componente
            if (map.value) {
                map.value.off();
                map.value.remove();
                map.value = null;
            }
        });

        return {
            direccionBusqueda,
            mapInitialized,
            buscarDireccion,
            t
        };
    }
});
</script>

<style scoped>
.leaflet-marker-icon {
    width: 25px !important;
    height: 41px !important;
}

.leaflet-marker-shadow {
    width: 41px !important;
    height: 41px !important;
}
</style>