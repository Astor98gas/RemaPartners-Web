<template>
    <div class="max-w-[80%] mx-auto py-8 px-4">
        <div class="flex items-center justify-between mb-8">
            <h1 class="text-3xl font-bold text-gray-800">
                {{ isEdit ? t('categoria.edit') : t('categoria.title') }}
            </h1>
            <button type="button" @click="$router.go(-1)"
                class="flex items-center px-4 py-2 text-gray-600 hover:text-gray-800 hover:bg-white hover:shadow-2xl rounded-2xl transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                </svg>
                {{ t('categoria.form.back') }}
            </button>
        </div>

        <form @submit.prevent="saveCategoria" class="bg-white shadow-lg rounded-xl p-8 border border-gray-100">
            <!-- Título -->
            <div class="mb-8">
                <label for="titulo" class="block text-gray-700 font-semibold mb-2 text-lg">
                    {{ t('categoria.form.title') }}
                </label>
                <input type="text" id="titulo" v-model="newCategoria.titulo"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                    required />
            </div>

            <!-- Descripción -->
            <div class="mb-8">
                <label for="descripcion" class="block text-gray-700 font-semibold mb-2 text-lg">
                    {{ t('categoria.form.description') }}
                </label>
                <textarea id="descripcion" v-model="newCategoria.descripcion" rows="3"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all duration-200"
                    required></textarea>
            </div>

            <!-- Campos -->
            <div class="mb-8">
                <div class="flex items-center justify-between mb-4">
                    <label class="block text-gray-700 font-semibold text-lg">
                        {{ t('categoria.form.fields') }}
                    </label>
                    <span class="text-sm text-gray-500">{{ newCategoria.campos.length }} {{ newCategoria.campos.length
                        === 1 ? t('categoria.form.field') : t('categoria.form.fieldsPlural') }}</span>
                </div>

                <div class="space-y-3">
                    <div v-for="(campo, index) in newCategoria.campos" :key="index"
                        class="flex items-center group bg-gray-50 rounded-lg transition-all hover:bg-gray-100"
                        :class="{ 'border-l-4 border-blue-500': focusedIndex === index }">
                        <textarea v-model="newCategoria.campos[index]"
                            class="flex-grow px-4 py-3 bg-transparent border-0 focus:outline-none focus:ring-0 resize-none overflow-hidden"
                            :placeholder="t('categoria.form.fieldPlaceholder')" @focus="focusedIndex = index"
                            @blur="focusedIndex = -1" @input="autoResize($event)" rows="1" ref="textareas"></textarea>
                        <button type="button" @click="removeCampo(index)"
                            class="p-2 mr-2 rounded-full text-gray-400 hover:text-red-500 hover:bg-red-50 transition-colors">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                            </svg>
                        </button>
                    </div>
                </div>

                <button type="button" @click="addCampo"
                    class="flex items-center justify-center w-full mt-4 px-4 py-3 border-2 border-dashed border-gray-300 text-blue-600 rounded-lg hover:border-blue-500 hover:bg-blue-50 transition-colors">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                    </svg>
                    {{ t('categoria.form.addField') }}
                </button>
            </div>

            <!-- Botones -->
            <div class="flex justify-end space-x-4 pt-4 border-t border-gray-100">
                <button type="button" @click="$router.go(-1)"
                    class="px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors">
                    {{ t('categoria.form.cancel') }}
                </button>
                <button type="submit"
                    class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center">
                    <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                        xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4">
                        </circle>
                        <path class="opacity-75" fill="currentColor"
                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                        </path>
                    </svg>
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
            error: null,
            focusedIndex: -1
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

    updated() {
        this.$nextTick(() => {
            document.querySelectorAll('textarea').forEach(textarea => {
                textarea.style.height = 'auto';
                textarea.style.height = textarea.scrollHeight + 'px';
            });
        });
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
        },

        autoResize(event: Event): void {
            const textarea = event.target as HTMLTextAreaElement;

            // Reset height to calculate proper scrollHeight
            textarea.style.height = 'auto';

            // Set new height based on content
            const newHeight = textarea.scrollHeight;
            textarea.style.height = newHeight + 'px';
        }
    }
});
</script>