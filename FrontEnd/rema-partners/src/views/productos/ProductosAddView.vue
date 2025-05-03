<template>
    <div class="max-w-[80%] mx-auto py-8 px-4">
        <!-- Header con paso actual -->
        <div class="flex items-center justify-between mb-8">
            <h1 class="text-3xl font-bold text-gray-800">
                {{ pageTitle }} - {{ t(`producto.add.step${currentStep}`) }}
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
                    {{ t('producto.titlePlaceholder') }}
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
            <div v-if="currentStep === 3">
                <ImageSelector v-model:images="producto.imagenes" />
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

                </div>

                <!-- Dirección con mapa -->
                <LocationSelector v-model="producto.direccion" />

                <!-- Descripción -->
                <div class="mb-4">
                    <label for="descripcion" class="block text-gray-700 font-semibold mb-2">
                        {{ t('producto.description') }}
                    </label>
                    <textarea id="descripcion" v-model="producto.descripcion" required rows="4"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200 resize-none"></textarea>
                </div>

                <!-- Campos dinámicos de la categoría -->
                <CategoryFields v-model:fields="producto.camposCategoria"
                    :category-title="getCategoriaSeleccionada()?.titulo || ''" />
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
                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                        </path>
                    </svg>
                    <span v-if="currentStep < 4">{{ t('common.next') }}</span>
                    <span v-else>{{ isEdit ? t('common.update') : t('common.save') }}</span>
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
import ImageSelector from '@/components/productos/ImageSelector.vue';
import LocationSelector from '@/components/productos/LocationSelector.vue';
import CategoryFields from '@/components/productos/CategoryFields.vue';
import { useUsers } from '@/composables/useUsers';

export default defineComponent({
    name: 'ProductosAddView',
    components: {
        ImageSelector,
        LocationSelector,
        CategoryFields
    },
    setup() {
        const categoriaService = useCategoria();
        const productoService = useProducto();
        return {
            categoriaService,
            productoService
        };
    },
    data() {
        return {
            isEdit: false,
            productId: '',
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
                fechaPublicacion: new Date().toISOString(),
                fechaBaja: '',
                direccion: '',
                activo: true,
                destacado: false,
                camposCategoria: [] as CamposCategoria[],
            } as ProductoModify,
            categorias: [] as Categoria[],
            EEstado,
            EMoneda,
            loading: false,
        };
    },
    computed: {
        pageTitle(): string {
            return this.isEdit
                ? this.t('producto.edit.title')
                : this.t('producto.add.title');
        }
    },
    watch: {
        'producto.idCategoria': function (newVal, oldVal) {
            if (newVal !== oldVal) {
                this.initCamposCategoria();
            }
        }
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
        async loadProductData() {
            if (!this.isEdit || !this.productId) return;

            try {
                this.loading = true;
                await this.productoService.getProductoById(this.productId);
                const productData = this.productoService.currentProducto.value;

                if (productData) {
                    // Ensure we load the categorias first if not already loaded
                    if (this.categorias.length === 0) {
                        await this.categoriaService.getCategorias();
                        this.categorias = this.categoriaService.categorias.value;
                    }

                    // Populate the form with existing data
                    this.producto = {
                        id: productData.id,
                        idUsuario: productData.idUsuario,
                        idCategoria: productData.idCategoria,
                        imagenes: [...productData.imagenes],
                        marca: productData.marca,
                        titulo: productData.titulo,
                        descripcion: productData.descripcion,
                        estado: productData.estado,
                        precioCentimos: productData.precioCentimos,
                        moneda: productData.moneda,
                        stock: productData.stock,
                        fechaCreacion: productData.fechaCreacion,
                        fechaModificacion: new Date().toISOString(),
                        fechaPublicacion: productData.fechaPublicacion,
                        fechaBaja: productData.fechaBaja,
                        direccion: productData.direccion,
                        activo: productData.activo,
                        destacado: productData.destacado,
                        // Make sure we deeply clone the camposCategoria array
                        camposCategoria: productData.camposCategoria && productData.camposCategoria.length > 0
                            ? JSON.parse(JSON.stringify(productData.camposCategoria))
                            : []
                    };

                    // If we have a category but no category fields, initialize them
                    if (this.producto.idCategoria && (!this.producto.camposCategoria || this.producto.camposCategoria.length === 0)) {
                        this.initCamposCategoria();
                    }

                    // If we have category fields but they don't match the current category structure, reinitialize
                    const selectedCategoria = this.getCategoriaSeleccionada();
                    if (selectedCategoria && this.producto.camposCategoria.length > 0) {
                        const categoriaFieldNames = selectedCategoria.campos.map(c => c);
                        const productoFieldNames = this.producto.camposCategoria.map(c => c.nombreCampo);

                        // Check if field arrays have different lengths or different field names
                        const needsReinitialize = categoriaFieldNames.length !== productoFieldNames.length ||
                            !categoriaFieldNames.every(field => productoFieldNames.includes(field));

                        if (needsReinitialize) {
                            // Preserve existing field values where possible
                            const oldFieldValues = {
                                ...this.producto.camposCategoria.reduce((acc, curr) => {
                                    acc[curr.nombreCampo] = curr.datos;
                                    return acc;
                                }, {} as Record<string, string>)
                            };

                            // Initialize with structure from selected category
                            this.producto.camposCategoria = selectedCategoria.campos.map(campo => ({
                                nombreCampo: campo,
                                datos: oldFieldValues[campo] || '' // Use old value if exists
                            }));
                        }
                    }
                }
            } catch (error: any) {
                console.error('Error loading product for editing:', error);
                Swal.fire({
                    icon: 'error',
                    title: this.t('producto.edit.loadError'),
                    text: error.response?.data?.message || this.t('producto.edit.loadErrorMessage'),
                    confirmButtonText: this.t('common.ok')
                });
            } finally {
                this.loading = false;
            }
        },
        async saveProduct() {
            try {
                this.loading = true;

                if (this.isEdit) {
                    // Update existing product
                    await this.productoService.updateProducto(this.productId, this.producto);
                    Swal.fire({
                        icon: 'success',
                        title: this.t('producto.edit.success'),
                        text: this.t('producto.edit.successMessage'),
                        confirmButtonText: this.t('common.ok')
                    }).then(() => {
                        this.$router.push(`/producto/${this.productId}`);
                    });
                } else {
                    // Create new product - existing code
                    const usersComposable = useUsers();
                    const userData = await usersComposable.isLoggedIn();
                    this.producto.idUsuario = userData.id;

                    await this.productoService.createProducto(this.producto);

                    Swal.fire({
                        icon: 'success',
                        title: this.t('producto.add.success'),
                        text: this.t('producto.add.successMessage'),
                        confirmButtonText: this.t('common.ok')
                    }).then(() => {
                        this.$router.push('/');
                    });
                }
            } catch (error: any) {
                console.error('Error saving product:', error);

                const errorTitle = this.isEdit
                    ? this.t('producto.edit.error')
                    : this.t('producto.add.error');

                const errorMessage = this.isEdit
                    ? error.response?.data?.message || this.t('producto.edit.errorMessage')
                    : error.response?.data?.message || this.t('producto.add.errorMessage');

                Swal.fire({
                    icon: 'error',
                    title: errorTitle,
                    text: errorMessage,
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
                // Save existing values if any
                const existingValues = {};
                if (this.producto.camposCategoria && this.producto.camposCategoria.length > 0) {
                    this.producto.camposCategoria.forEach(campo => {
                        existingValues[campo.nombreCampo] = campo.datos;
                    });
                }

                // Create new campos array based on the selected category
                this.producto.camposCategoria = categoriaSeleccionada.campos.map(campo => ({
                    nombreCampo: campo,
                    datos: existingValues[campo] || '' // Use existing value if available
                }));
            }
        }
    },
    async mounted() {
        try {
            // Check if we're in edit mode by looking at the route parameters
            const route = this.$route;
            if (route.params.id && route.path.includes('/edit')) {
                this.isEdit = true;
                this.productId = route.params.id as string;
            }

            // Load categories first, always
            await this.categoriaService.getCategorias();
            this.categorias = this.categoriaService.categorias.value;

            // If in edit mode, load the product data after categories are loaded
            if (this.isEdit) {
                await this.loadProductData();
            }
        } catch (error) {
            console.error('Error in mounted:', error);
        }
    }
});
</script>
