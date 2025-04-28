<template>
    <div class="bg-white shadow-md rounded-lg p-8 max-w-3xl mx-auto">
        <!-- Header con paso actual -->
        <div class="flex items-center mb-8">
            <h1 class="text-2xl font-bold text-gray-800">{{ t('producto.add.title') }} - {{
                t(`producto.add.step${currentStep}`) }}</h1>
            <router-link to="/productos"
                class="ml-auto bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-200">
                {{ t('producto.add.back') }}
            </router-link>
        </div>

        <!-- Indicador de progreso -->
        <div class="mb-8">
            <div class="flex items-center justify-between relative">
                <div v-for="step in 4" :key="step"
                    class="z-10 flex items-center justify-center w-10 h-10 rounded-full transition-colors duration-300"
                    :class="[currentStep >= step ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-500']">
                    {{ step }}
                </div>
                <!-- Línea de progreso -->
                <div class="absolute h-1 bg-gray-200 w-full top-1/2 transform -translate-y-1/2 -z-0">
                    <div class="h-full bg-blue-500 transition-all duration-300"
                        :style="{ width: `${(currentStep - 1) * 33.33}%` }"></div>
                </div>
            </div>
        </div>

        <!-- Formulario por pasos -->
        <form @submit.prevent="nextStep" class="space-y-6">
            <!-- Paso 1: Título -->
            <div v-if="currentStep === 1" class="space-y-4">
                <label class="block">
                    <span class="text-gray-700 font-medium">{{ t('producto.title') }}</span>
                    <input type="text" v-model="producto.titulo" required
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50"
                        :placeholder="t('producto.titlePlaceholder')" />
                </label>
            </div>

            <!-- Paso 2: Categoría -->
            <div v-if="currentStep === 2" class="space-y-4">
                <label class="block">
                    <span class="text-gray-700 font-medium">{{ t('producto.category') }}</span>
                    <select v-model="producto.idCategoria" required
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50">
                        <option value="">{{ t('producto.selectCategory') }}</option>
                        <option v-for="categoria in categorias" :key="categoria.id" :value="categoria.id">
                            {{ categoria.titulo }}
                        </option>
                    </select>
                </label>
            </div>

            <!-- Paso 3: Imágenes -->
            <div v-if="currentStep === 3" class="space-y-4">
                <div class="grid grid-cols-3 gap-4">
                    <div v-for="index in 7" :key="index"
                        class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center cursor-pointer hover:border-blue-500 transition-colors"
                        @click="triggerFileInput(index - 1)">
                        <div v-if="imagePreview[index - 1]" class="relative">
                            <img :src="imagePreview[index - 1]" class="w-full h-32 object-cover rounded" />
                            <button @click.stop="removeImage(index - 1)"
                                class="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1 hover:bg-red-600">
                                <span class="sr-only">Remove</span>
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>
                        <div v-else>
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
            <div v-if="currentStep === 4" class="space-y-4">
                <div class="grid grid-cols-2 gap-4">
                    <label class="block">
                        <span class="text-gray-700 font-medium">{{ t('producto.brand') }}</span>
                        <input type="text" v-model="producto.marca" required
                            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50" />
                    </label>
                    <label class="block">
                        <span class="text-gray-700 font-medium">{{ t('producto.price') }}</span>
                        <div class="mt-1 flex">
                            <input type="number" v-model="producto.precioCentimos" required min="0" step="1"
                                class="block w-full rounded-l-md border-gray-300 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50" />
                            <select v-model="producto.moneda"
                                class="block rounded-r-md border-l-0 border-gray-300 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50">
                                <option v-for="moneda in Object.values(EMoneda)" :key="moneda" :value="moneda">
                                    {{ moneda }}
                                </option>
                            </select>
                        </div>
                    </label>
                    <!-- Otros campos -->
                    <label class="block">
                        <span class="text-gray-700 font-medium">{{ t('producto.estado') }}</span>
                        <select v-model="producto.estado"
                            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50">
                            <option v-for="estado in Object.values(EEstado)" :key="estado" :value="estado">
                                {{ t(`producto.estados.${estado}`) }}
                            </option>
                        </select>
                    </label>
                    <label class="block">
                        <span class="text-gray-700 font-medium">{{ t('producto.stock') }}</span>
                        <input type="number" v-model="producto.stock" required min="0"
                            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50" />
                    </label>
                </div>
                <label class="block">
                    <span class="text-gray-700 font-medium">{{ t('producto.description') }}</span>
                    <textarea v-model="producto.descripcion" required rows="4"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50"></textarea>
                </label>

                <!-- Campos dinámicos de la categoría -->
                <div v-if="producto.camposCategoria.length > 0" class="mt-6">
                    <h3 class="text-lg font-medium text-gray-900 mb-4">
                        {{ getCategoriaSeleccionada()?.titulo }} - {{ t('producto.category.fields') }}
                    </h3>
                    <div class="grid grid-cols-2 gap-4">
                        <div v-for="(campo, index) in producto.camposCategoria" :key="index" class="block">
                            <label class="block">
                                <span class="text-gray-700 font-medium">{{ campo.nombreCampo }}</span>
                                <input type="text" v-model="campo.datos"
                                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-300 focus:ring focus:ring-blue-200 focus:ring-opacity-50" />
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Botones de navegación -->
            <div class="flex justify-between pt-4">
                <button type="button" @click="previousStep" v-if="currentStep > 1"
                    class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition duration-200">
                    {{ t('common.previous') }}
                </button>
                <button type="submit"
                    class="ml-auto px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition duration-200 disabled:bg-blue-300 disabled:cursor-not-allowed"
                    :disabled="loading">
                    <span v-if="loading && currentStep === 4" class="inline-block animate-spin mr-2">⟳</span>
                    {{ currentStep === 4 ? t('common.save') : t('common.next') }}
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