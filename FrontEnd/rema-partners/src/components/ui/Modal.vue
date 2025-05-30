<template>
    <teleport to="body">
        <transition name="modal-fade">
            <div v-if="modelValue" class="fixed inset-0 flex items-center justify-center z-40">
                <!-- Overlay modificado para ser más ligero y no bloquear completamente -->
                <div class="fixed inset-0 bg-black/30" @click="closeModal"></div>
                <!-- z-index ajustado y contraste mejorado -->
                <div class="bg-white rounded-lg shadow-xl border border-gray-200 p-6 w-auto max-w-2xl relative z-50">
                    <div class="flex justify-between items-center mb-4">
                        <h2 class="text-xl font-bold">{{ title }}</h2>
                        <button @click="closeModal" class="text-gray-500 hover:text-gray-700">
                            <span class="sr-only">{{ utf8.t('modal.close') }}</span>
                            <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>
                    <div class="modal-content">
                        <slot></slot>
                    </div>
                    <div class="mt-6 flex justify-end gap-2">
                        <slot name="footer">
                            <ButtonBasic @click="closeModal" variant="secondary">
                                {{ utf8.t('modal.close') }}
                            </ButtonBasic>
                            <slot name="actions"></slot>
                        </slot>
                    </div>
                </div>
            </div>
        </transition>
    </teleport>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ButtonBasic from '@/components/ui/ButtonBasic.vue'
import { useutf8Store } from '@/stores/counter'

/**
 * Componente Modal reutilizable.
 * 
 * Permite mostrar contenido en una ventana modal superpuesta.
 * Incluye título, botón de cierre y slots para contenido y acciones.
 * 
 * @component
 * @prop {Boolean} modelValue - Controla la visibilidad del modal.
 * @prop {String} title - Título del modal.
 * @prop {Boolean} hideCloseButton - Si se oculta el botón de cerrar.
 * 
 * @event update:modelValue - Se emite al cerrar el modal.
 * @event close - Se emite cuando se cierra el modal.
 * 
 * @method closeModal - Cierra el modal y emite los eventos correspondientes.
 */
export default defineComponent({
    name: 'Modal',
    components: {
        ButtonBasic
    },
    props: {
        modelValue: {
            type: Boolean,
            default: false
        },
        title: {
            type: String,
            required: true
        },
        hideCloseButton: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            utf8: useutf8Store()
        }
    },
    emits: ['update:modelValue', 'close'],
    methods: {
        closeModal() {
            this.$emit('update:modelValue', false)
            this.$emit('close')
        }
    }
})
</script>

<style scoped>
.modal-fade-enter-active,
.modal-fade-leave-active {
    transition: all 0.2s ease-out;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
    opacity: 0;
    transform: scale(0.95);
}

.modal-fade-enter-to,
.modal-fade-leave-from {
    opacity: 1;
    transform: scale(1);
}
</style>