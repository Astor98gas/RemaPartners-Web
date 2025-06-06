<template>
    <div class="space-y-4">
        <label class="block text-gray-700 font-semibold mb-4 text-lg">
            {{ t('producto.images') }}
        </label>
        <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
            <div v-for="index in maxImages" :key="index"
                class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center cursor-pointer hover:border-blue-500 transition-colors aspect-square flex flex-col justify-center items-center"
                @click="triggerFileInput(index - 1)">
                <div v-if="imagePreview[index - 1]" class="relative w-full h-full">
                    <img :src="t('link.servidor') + imagePreview[index - 1]"
                        class="w-full h-full object-cover rounded" />
                    <button @click.stop="removeImage(index - 1)"
                        class="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1.5 hover:bg-red-600 shadow-md transition-colors">
                        <span class="sr-only">Eliminar</span>
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>
                <div v-else class="flex flex-col items-center justify-center h-full">
                    <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                    </svg>
                    <span class="mt-2 block text-sm text-gray-600">{{ t('producto.addImage') }}</span>
                </div>
            </div>
        </div>
        <input type="file" ref="fileInput" class="hidden" accept="image/*" @change="onFileSelected" />
    </div>
</template>

<script>
import { useutf8Store } from '@/stores/counter';

/**
 * Componente ImageSelector
 * 
 * Permite seleccionar, previsualizar, subir y eliminar imágenes para un producto.
 * 
 * Props:
 * - images: Array de imágenes actuales.
 * - maxImages: Número máximo de imágenes permitidas.
 * 
 * Emits:
 * - update:images: Emite el array actualizado de imágenes al componente padre.
 */
export default {
    name: 'ImageSelector',
    props: {
        /**
         * Array de imágenes seleccionadas.
         * @type {Array}
         */
        images: {
            type: Array,
            required: true
        },
        /**
         * Número máximo de imágenes permitidas.
         * @type {Number}
         */
        maxImages: {
            type: Number,
            default: 8
        }
    },
    emits: ['update:images'],
    data() {
        return {
            currentImageIndex: 0,
            imagePreview: Array(this.maxImages).fill('') // Inicializar con array vacío del tamaño máximo
        };
    },
    created() {
        // Inicializar preview con las imágenes existentes
        this.initializePreview();
    },
    methods: {
        /**
         * Traduce una clave usando el store de traducciones.
         * @param {String} key Clave de traducción.
         * @returns {String}
         */
        t(key) {
            const store = useutf8Store();
            return store.t(key);
        },
        /**
         * Inicializa el array de previsualización con las imágenes existentes.
         */
        initializePreview() {
            // Llenar el array de preview con las imágenes existentes
            if (this.images && this.images.length > 0) {
                const newPreview = Array(this.maxImages).fill('');
                this.images.forEach((img, index) => {
                    if (index < this.maxImages) {
                        newPreview[index] = img;
                    }
                });
                this.imagePreview = newPreview;
            }
        },
        /**
         * Dispara el input de archivo para seleccionar una imagen.
         * @param {Number} index Índice de la imagen a actualizar.
         */
        triggerFileInput(index) {
            this.currentImageIndex = index;
            this.$refs.fileInput.click();
        },
        /**
         * Maneja la selección de un archivo, sube la imagen y actualiza el array.
         * @param {Event} event Evento de cambio del input file.
         */
        async onFileSelected(event) {
            const file = event.target.files?.[0];
            if (file) {
                try {
                    // Validar tamaño del archivo (5MB máximo)
                    if (file.size > 5 * 1024 * 1024) {
                        console.error('Archivo demasiado grande');
                        // Mostrar error al usuario
                        return;
                    }

                    // Validar tipo de archivo
                    if (!file.type.startsWith('image/')) {
                        console.error('Tipo de archivo inválido');
                        // Mostrar error al usuario
                        return;
                    }

                    // Mostrar preview temporal usando FileReader
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        if (e.target?.result) {
                            const newPreview = [...this.imagePreview];
                            newPreview[this.currentImageIndex] = e.target.result;
                            this.imagePreview = newPreview;
                        }
                    };
                    reader.readAsDataURL(file);

                    // Crear FormData
                    const formData = new FormData();
                    formData.append('file', file);

                    // Enviar al backend con timeout
                    const controller = new AbortController();
                    const timeoutId = setTimeout(() => controller.abort(), 30000); // 30 segundos timeout

                    const response = await fetch('http://localhost:8080/api/upload', {
                        method: 'POST',
                        body: formData,
                        signal: controller.signal
                    });

                    clearTimeout(timeoutId);

                    if (!response.ok) {
                        const errorText = await response.text();
                        throw new Error(`HTTP ${response.status}: ${errorText}`);
                    }

                    const data = await response.json();
                    const imageUrl = data.url;

                    // Actualizar array de imágenes con la URL
                    const updatedImagesArray = [...this.imagePreview];
                    updatedImagesArray[this.currentImageIndex] = imageUrl;

                    // Filtrar para enviar solo las imágenes no vacías al componente padre
                    const filteredImages = updatedImagesArray.filter(img => img !== '');
                    this.$emit('update:images', filteredImages);

                } catch (error) {
                    console.error('Error subiendo imagen:', error);

                    // Restaurar preview en caso de error
                    const newPreview = [...this.imagePreview];
                    newPreview[this.currentImageIndex] = '';
                    this.imagePreview = newPreview;

                    // Mostrar error específico al usuario
                    if (error.name === 'AbortError') {
                        alert('La subida de imagen se canceló por tiempo');
                    } else {
                        alert(`Error subiendo imagen: ${error.message}`);
                    }
                }
            }
        },
        /**
         * Elimina una imagen del array y actualiza la previsualización.
         * @param {Number} index Índice de la imagen a eliminar.
         */
        removeImage(index) {
            // Prevenir propagación del evento para evitar efectos secundarios
            event?.preventDefault();
            event?.stopPropagation();

            // Usar nextTick para asegurar que la operación se complete en el siguiente ciclo
            this.$nextTick(() => {
                // Crear copia del array actual
                const newPreview = [...this.imagePreview];

                // Limpiar la posición específica sin reorganizar
                newPreview[index] = '';

                // Actualizar la previsualización de manera inmutable
                this.imagePreview = newPreview;

                // Filtrar solo las imágenes válidas para emitir al padre
                const filteredImages = newPreview.filter(img => img !== '');

                // Emitir de manera síncrona para mantener consistencia
                this.$emit('update:images', filteredImages);
            });
        }
    },
    watch: {
        images: {
            /**
             * Observa cambios en el array de imágenes y actualiza la previsualización.
             */
            handler(newImages) {
                // Evitar actualizaciones innecesarias comparando el contenido
                const currentFiltered = this.imagePreview.filter(img => img !== '');
                const arraysEqual = newImages.length === currentFiltered.length &&
                    newImages.every((img, index) => img === currentFiltered[index]);

                if (!arraysEqual) {
                    // Si recibimos nuevas imágenes desde el componente padre, actualizar preview
                    const newPreview = Array(this.maxImages).fill('');
                    newImages.forEach((img, index) => {
                        if (index < this.maxImages) {
                            newPreview[index] = img;
                        }
                    });
                    this.imagePreview = newPreview;
                }
            },
            deep: true,
            immediate: true
        }
    }
};
</script>