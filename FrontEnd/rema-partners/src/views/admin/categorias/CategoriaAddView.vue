<template>
    <div class="max-w-3xl mx-auto py-8 px-4">
        <h1 class="text-3xl font-bold text-center mb-8">
            {{ isEdit ? t('categoria.edit') : t('categoria.title') }}
        </h1>

        <form @submit.prevent="saveCategoria" class="bg-white shadow-md rounded-lg p-6">
            <!-- Título -->
            <div class="mb-6">
                <label for="titulo" class="block text-gray-700 font-semibold mb-2">
                    {{ t('categoria.form.title') }}
                </label>
                <input type="text" id="titulo" v-model="newCategoria.titulo"
                    class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required />
            </div>

            <!-- Descripción -->
            <div class="mb-6">
                <label for="descripcion" class="block text-gray-700 font-semibold mb-2">
                    {{ t('categoria.form.description') }}
                </label>
                <textarea id="descripcion" v-model="newCategoria.descripcion" rows="3"
                    class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required></textarea>
            </div>

            <!-- Campos -->
            <div class="mb-6">
                <label class="block text-gray-700 font-semibold mb-2">
                    {{ t('categoria.form.fields') }}
                </label>
                <div v-for="(campo, index) in newCategoria.campos" :key="index" class="flex items-center mb-2">
                    <input type="text" v-model="newCategoria.campos[index]"
                        class="flex-grow px-4 py-2 border border-gray-300 rounded-l-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        :placeholder="t('categoria.form.fieldPlaceholder')" />
                    <button type="button" @click="removeCampo(index)"
                        class="px-4 py-2 bg-red-500 text-white rounded-r-md hover:bg-red-600">
                        <span class="font-bold">-</span>
                    </button>
                </div>

                <button type="button" @click="addCampo"
                    class="w-full mt-2 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">
                    {{ t('categoria.form.addField') }}
                </button>
            </div>

            <!-- Botones -->
            <div class="flex justify-end space-x-4">
                <button type="button" @click="$router.go(-1)"
                    class="px-6 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
                    {{ t('categoria.form.cancel') }}
                </button>
                <button type="submit" class="px-6 py-2 bg-green-500 text-white rounded-md hover:bg-green-600">
                    {{ isEdit ? t('categoria.form.update') : t('categoria.form.save') }}
                </button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { useCategoria } from '@/composables/useCategoria';
import { useRoute } from 'vue-router';
import { useToast } from 'vue-toastification';
import { useutf8Store } from '@/stores/counter';
import type { CategoriaModify } from '@/models/categoria';

export default defineComponent({
    name: 'CategoriaAddView',

    data() {
        return {
            newCategoria: {
                id: '',
                titulo: '',
                descripcion: '',
                campos: ['']
            } as CategoriaModify,
            loading: false,
            error: null
        };
    },

    computed: {
        isEdit(): boolean {
            const route = useRoute();
            return !!route.params.id;
        },
        categoriaId(): string {
            const route = useRoute();
            return route.params.id as string;
        }
    },

    async mounted() {
        // Cargar datos si estamos en modo edición
        if (this.isEdit) {
            try {
                const categoriaService = useCategoria();
                await categoriaService.getCategoriaById(this.categoriaId);
                const categoriaLoaded = categoriaService.currentCategoria.value;

                if (categoriaLoaded) {
                    this.newCategoria = { ...categoriaLoaded };
                }
            } catch (err) {
                const toast = useToast();
                toast.error(this.t('categoria.error.loading'));
                this.$router.push('/admin/categoria/list');
            }
        }
    },

    methods: {
        t(key: string): string {
            const store = useutf8Store();
            return store.t(key);
        },

        addCampo(): void {
            this.newCategoria.campos.push('');
        },

        removeCampo(index: number): void {
            this.newCategoria.campos.splice(index, 1);
        },

        async saveCategoria(): Promise<void> {
            const toast = useToast();

            try {
                const hasEmptyFields = this.newCategoria.campos.some(campo => campo.trim() === '');
                if (hasEmptyFields) {
                    toast.warning(this.t('categoria.validation.emptyFields'));
                    return;
                }

                const categoriaService = useCategoria();

                if (this.isEdit) {
                    await categoriaService.updateCategoria(this.categoriaId, this.newCategoria);
                    toast.success(this.t('categoria.success.updated'));
                } else {
                    const categoriaSinId = { ...this.newCategoria };
                    delete categoriaSinId.id;

                    await categoriaService.createCategoria(categoriaSinId);
                    toast.success(this.t('categoria.success.created'));
                }

                this.$router.push('/admin/categoria/list');
            } catch (err: any) {
                if (err.response?.status === 400) {
                    toast.error(err.response?.data || this.t('categoria.error.saving'));
                } else if (err.response?.status === 500) {
                    toast.error(this.t('categoria.error.server'));
                } else {
                    toast.error(this.t('categoria.error.unknown') + ': ' + (err.message || ''));
                    console.error('Error completo:', err);
                }
            }
        }
    }
});
</script>