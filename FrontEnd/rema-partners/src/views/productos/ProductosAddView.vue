<template>
    <div class="max-w-[80%] mx-auto py-8 px-4">
        <!-- Header con paso actual -->
        <div class="flex items-center justify-between mb-8">
            <h1 class="text-3xl font-bold text-gray-800">
                {{ t('producto.add.title') }} - {{ t(`producto.add.step${currentStep}`) }}
                <span class="absolute bottom-0 left-0 w-24 h-1 bg-blue-500 rounded-full"></span>
            </h1>
            <router-link to="/"
                class="flex items-center px-4 py-2 text-gray-600 hover:text-gray-800 hover:bg-gray-100 hover:shadow-md rounded-lg transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                </svg>
                {{ t('producto.add.back') }}
            </router-link>
        </div>

        <!-- Indicador de progreso mejorado -->
        <div class="mb-8">
            <div class="flex items-center justify-between relative">
                <div v-for="step in 4" :key="step"
                    class="z-10 flex items-center justify-center w-12 h-12 rounded-full transition-colors duration-300 shadow-md"
                    :class="[currentStep >= step ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-500']">
                    {{ step }}
                </div>
                <!-- Línea de progreso -->
                <div class="absolute h-2 bg-gray-200 w-full top-1/2 transform -translate-y-1/2 -z-0 rounded-full">
                    <div class="h-full bg-blue-500 transition-all duration-300 rounded-full"
                        :style="{ width: `${(currentStep - 1) * 33.33}%` }"></div>
                </div>
            </div>
        </div>

        <!-- Formulario por pasos -->
        <form @submit.prevent="nextStep" class="bg-white shadow-lg rounded-xl p-8 border border-gray-100">
            <!-- Paso 1: Título -->
            <div v-if="currentStep === 1" class="space-y-4">
                <label for="titulo" class="block text-gray-700 font-semibold mb-2 text-lg">
                    {{ t('producto.title') }}
                </label>
                <input type="text" id="titulo" v-model="producto.titulo" required
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                    :placeholder="t('producto.titlePlaceholder')" />
            </div>

            <!-- Paso 2: Categoría -->
            <div v-if="currentStep === 2" class="space-y-4">
                <label for="categoria" class="block text-gray-700 font-semibold mb-2 text-lg">
                    {{ t('producto.category') }}
                </label>
                <select id="categoria" v-model="producto.idCategoria" required
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200">
                    <option value="">{{ t('producto.selectCategory') }}</option>
                    <option v-for="categoria in categorias" :key="categoria.id" :value="categoria.id">
                        {{ categoria.titulo }}
                    </option>
                </select>
            </div>

            <!-- Paso 3: Imágenes -->
            <div v-if="currentStep === 3" class="space-y-4">
                <label class="block text-gray-700 font-semibold mb-4 text-lg">
                    {{ t('producto.images') }}
                </label>
                <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
                    <div v-for="index in 8" :key="index"
                        class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center cursor-pointer hover:border-blue-500 transition-colors aspect-square flex flex-col justify-center items-center"
                        @click="triggerFileInput(index - 1)">
                        <div v-if="imagePreview[index - 1]" class="relative w-full h-full">
                            <img :src="imagePreview[index - 1]" class="w-full h-full object-cover rounded" />
                            <button @click.stop="removeImage(index - 1)"
                                class="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1.5 hover:bg-red-600 shadow-md transition-colors">
                                <span class="sr-only">Remove</span>
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>
                        <div v-else class="flex flex-col items-center justify-center h-full">
                            <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor"
                                viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 4v16m8-8H4" />
                            </svg>
                            <span class="mt-2 block text-sm text-gray-600">{{ t('producto.addImage') }}</span>
                        </div>
                    </div>
                </div>
                <input type="file" ref="fileInput" class="hidden" accept="image/*" @change="onFileSelected" />
            </div>

            <!-- Paso 4: Detalles finales -->
            <div v-if="currentStep === 4" class="space-y-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Marca -->
                    <div class="mb-4">
                        <label for="marca" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.brand') }}
                        </label>
                        <input type="text" id="marca" v-model="producto.marca" required
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
                    </div>

                    <!-- Precio -->
                    <div class="mb-4">
                        <label for="precio" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.price') }}
                        </label>
                        <div class="flex">
                            <input type="number" id="precio" v-model="producto.precioCentimos" required min="0" step="1"
                                class="w-3/4 px-4 py-3 border border-gray-300 rounded-l-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
                            <select v-model="producto.moneda"
                                class="w-1/4 px-4 py-3 border border-l-0 border-gray-300 rounded-r-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200">
                                <option v-for="moneda in Object.values(EMoneda)" :key="moneda" :value="moneda">
                                    {{ moneda }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Estado -->
                    <div class="mb-4">
                        <label for="estado" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.estado') }}
                        </label>
                        <select id="estado" v-model="producto.estado"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200">
                            <option v-for="estado in Object.values(EEstado)" :key="estado" :value="estado">
                                {{ t(`producto.estados.${estado}`) }}
                            </option>
                        </select>
                    </div>

                    <!-- Stock -->
                    <div class="mb-4">
                        <label for="stock" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.stock') }}
                        </label>
                        <input type="number" id="stock" v-model="producto.stock" required min="0"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
                    </div>

                    <!-- Dirección -->
                    <div class="mb-4">
                        <label for="direccion" class="block text-gray-700 font-semibold mb-2">
                            {{ t('producto.direccion') }}
                        </label>
                        <input type="text" id="direccion" v-model="producto.direccion"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
                    </div>

                    <!-- Opciones adicionales -->
                    <div class="mb-4 flex space-x-4 items-center">
                        <label class="inline-flex items-center">
                            <input type="checkbox" v-model="producto.activo"
                                class="form-checkbox h-5 w-5 text-blue-600">
                            <span class="ml-2 text-gray-700">{{ t('producto.activo') }}</span>
                        </label>
                        <label class="inline-flex items-center">
                            <input type="checkbox" v-model="producto.destacado"
                                class="form-checkbox h-5 w-5 text-blue-600">
                            <span class="ml-2 text-gray-700">{{ t('producto.destacado') }}</span>
                        </label>
                    </div>
                </div>

                <!-- Descripción -->
                <div class="mb-4">
                    <label for="descripcion" class="block text-gray-700 font-semibold mb-2">
                        {{ t('producto.description') }}
                    </label>
                    <textarea id="descripcion" v-model="producto.descripcion" required rows="4"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200 resize-none"></textarea>
                </div>

                <!-- Campos dinámicos de la categoría -->
                <div v-if="producto.camposCategoria.length > 0"
                    class="mt-6 p-6 bg-gray-50 rounded-xl border border-gray-200">
                    <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none"
                            viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                        </svg>
                        {{ getCategoriaSeleccionada()?.titulo }} - {{ t('producto.category.fields') }}
                    </h3>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div v-for="(campo, index) in producto.camposCategoria" :key="index" class="mb-4">
                            <label :for="`campo-${index}`" class="block text-gray-700 font-medium mb-2">
                                {{ campo.nombreCampo }}
                            </label>
                            <input type="text" :id="`campo-${index}`" v-model="campo.datos"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200" />
                        </div>
                    </div>
                </div>
            </div>

            <!-- Botones de navegación -->
            <div class="flex justify-between space-x-4 pt-6 mt-6 border-t border-gray-100">
                <button type="button" @click="previousStep" v-if="currentStep > 1"
                    class="px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                    </svg>
                    {{ t('common.previous') }}
                </button>
                <div class="flex-1"></div>
                <button type="submit"
                    class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center disabled:bg-blue-300 disabled:cursor-not-allowed"
                    :disabled="loading">
                    <svg v-if="loading && currentStep === 4" class="animate-spin -ml-1 mr-2 h-5 w-5 text-white"
                        xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4">
                        </circle>
                        <path class="opacity-75" fill="currentColor"
                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                        </path>
                    </svg>
                    <span v-if="currentStep < 4">{{ t('common.next') }}</span>
                    <span v-else>{{ t('common.save') }}</span>
                    <svg v-if="currentStep < 4" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 ml-1" fill="none"
                        viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                    </svg>
                </button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { EEstado } from '@/models/enums/EEstado';
import { EMoneda } from '@/models/enums/EMoneda';
import type { ProductoModify } from '@/models/producto';
import { useutf8Store } from '@/stores/counter';
import { useCategoria } from '@/composables/useCategoria';
import type { Categoria } from '@/models/categoria';
import type { CamposCategoria } from '@/models/camposCategoria';
import { useProducto } from '@/composables/useProducto';
import Swal from 'sweetalert2';

export default defineComponent({
    name: 'ProductosAddView',
    setup() {
        const categoriaService = useCategoria();
        const productoService = useProducto(); // Añadir el servicio de producto
        return {
            categoriaService,
            productoService
        };
    },
    data() {
        return {
            currentStep: 1,
            producto: {
                idUsuario: '',
                idCategoria: '',
                imagenes: [] as string[],
                marca: '',
                titulo: '',
                descripcion: '',
                estado: EEstado.NUEVO,
                precioCentimos: 0,
                moneda: EMoneda.EUR,
                stock: 0,
                fechaCreacion: new Date().toISOString(),
                fechaModificacion: new Date().toISOString(),
                fechaPublicacion: '',
                fechaBaja: '',
                direccion: '',
                activo: true,
                destacado: false,
                camposCategoria: [] as CamposCategoria[],
            } as ProductoModify,
            imagePreview: [] as string[],
            categorias: [] as Categoria[],
            currentImageIndex: 0,
            EEstado,
            EMoneda,
            loading: false,
        };
    },
    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },
        nextStep() {
            if (this.validateCurrentStep()) {
                if (this.currentStep === 2) {
                    this.initCamposCategoria();
                }
                if (this.currentStep === 4) {
                    this.saveProduct();
                } else {
                    this.currentStep++;
                }
            }
        },
        previousStep() {
            if (this.currentStep > 1) {
                this.currentStep--;
            }
        },
        validateCurrentStep(): boolean {
            switch (this.currentStep) {
                case 1:
                    if (!this.producto.titulo) {
                        Swal.fire({
                            icon: 'warning',
                            title: this.t('producto.validation.error'),
                            text: this.t('producto.validation.titleRequired'),
                            confirmButtonText: this.t('common.ok')
                        });
                        return false;
                    }
                    return true;

                case 2:
                    if (!this.producto.idCategoria) {
                        Swal.fire({
                            icon: 'warning',
                            title: this.t('producto.validation.error'),
                            text: this.t('producto.validation.categoryRequired'),
                            confirmButtonText: this.t('common.ok')
                        });
                        return false;
                    }
                    return true;

                case 3:
                    if (this.producto.imagenes.length === 0) {
                        Swal.fire({
                            icon: 'warning',
                            title: this.t('producto.validation.error'),
                            text: this.t('producto.validation.imageRequired'),
                            confirmButtonText: this.t('common.ok')
                        });
                        return false;
                    }
                    return true;

                case 4:
                    let errorMessage = '';

                    if (!this.producto.marca) {
                        errorMessage = this.t('producto.validation.brandRequired');
                    } else if (!this.producto.descripcion) {
                        errorMessage = this.t('producto.validation.descriptionRequired');
                    } else if (this.producto.precioCentimos <= 0) {
                        errorMessage = this.t('producto.validation.priceRequired');
                    } else if (this.producto.stock < 0) {
                        errorMessage = this.t('producto.validation.stockInvalid');
                    } else if (this.producto.camposCategoria &&
                        !this.producto.camposCategoria.every(campo => typeof campo.datos === 'string' && campo.datos.trim() !== '')) {
                        errorMessage = this.t('producto.validation.categoryFieldsRequired');
                    }

                    if (errorMessage) {
                        Swal.fire({
                            icon: 'warning',
                            title: this.t('producto.validation.error'),
                            text: errorMessage,
                            confirmButtonText: this.t('common.ok')
                        });
                        return false;
                    }

                    return true;

                default:
                    return false;
            }
        },
        triggerFileInput(index: number) {
            this.currentImageIndex = index;
            (this.$refs.fileInput as HTMLInputElement).click();
        },
        async onFileSelected(event: Event) {
            const file = (event.target as HTMLInputElement).files?.[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    if (e.target?.result) {
                        this.imagePreview[this.currentImageIndex] = e.target.result as string;
                        this.producto.imagenes[this.currentImageIndex] = e.target.result as string;
                    }
                };
                reader.readAsDataURL(file);
            }
        },
        removeImage(index: number) {
            this.imagePreview.splice(index, 1);
            this.producto.imagenes.splice(index, 1);
        },
        async saveProduct() {
            try {
                this.loading = true;

                this.producto.idUsuario = localStorage.getItem('userId') || '';

                await this.productoService.createProducto(this.producto);

                Swal.fire({
                    icon: 'success',
                    title: this.t('producto.add.success'),
                    text: this.t('producto.add.successMessage'),
                    confirmButtonText: this.t('common.ok')
                }).then(() => {
                    this.$router.push('/');
                });

            } catch (error: any) {
                console.error('Error al guardar el producto:', error);

                Swal.fire({
                    icon: 'error',
                    title: this.t('producto.add.error'),
                    text: error.response?.data?.message || this.t('producto.add.errorMessage'),
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },
        getCategoriaSeleccionada(): Categoria | undefined {
            return this.categorias.find(cat => cat.id === this.producto.idCategoria);
        },
        initCamposCategoria() {
            const categoriaSeleccionada = this.getCategoriaSeleccionada();
            if (categoriaSeleccionada) {
                this.producto.camposCategoria = categoriaSeleccionada.campos.map(campo => ({
                    nombreCampo: campo,
                    datos: ''
                }));
            }
        }
    },
    async mounted() {
        try {
            await this.categoriaService.getCategorias();
            this.categorias = this.categoriaService.categorias.value;
        } catch (error) {
            console.error('Error al cargar categorías:', error);
        }
    }
});
</script>