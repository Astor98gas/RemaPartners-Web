<template>
    <button :class="[
        'px-4 py-2 rounded-lg transition-all duration-300 font-medium',
        'focus:outline-none focus:ring-2 focus:ring-offset-2 active:scale-95',
        variant === 'primary' ? ' shadow-sm bg-blue-600 text-white hover:bg-blue-500 focus:ring-blue-500 hover:shadow-md' :
            variant === 'secondary' ? 'text-gray-700 hover:bg-gray-200 hover:text-orange-500 focus:ring-gray-500' :
                variant === 'danger' ? 'bg-red-600 text-white hover:bg-red-500 focus:ring-red-500 hover:shadow-md' :
                    variant === 'success' ? 'bg-green-600 text-white hover:bg-green-500 focus:ring-green-500 hover:shadow-md' :
                        'bg-gray-600 text-white hover:bg-gray-500 focus:ring-gray-500 hover:shadow-md',
        disabled ? 'opacity-60 cursor-not-allowed' : 'cursor-pointer',
        fullWidth ? 'w-full' : '',
        size === 'sm' ? 'text-sm py-1.5 px-3' : size === 'lg' ? 'text-lg px-6 py-3 font-semibold' : 'text-base'
    ]" :disabled="disabled" @click="$emit('click')">
        <slot></slot>
    </button>
</template>

<script lang="ts">
/**
 * Componente de botón básico reutilizable.
 * 
 * Permite seleccionar variantes de estilo, tamaño, deshabilitar el botón y expandirlo a todo el ancho.
 * 
 * @component
 * @prop {String} to - (Opcional) Ruta de navegación, no utilizada actualmente.
 * @prop {'primary'|'secondary'|'danger'|'success'|'default'} variant - Variante de estilo del botón.
 * @prop {'sm'|'md'|'lg'} size - Tamaño del botón.
 * @prop {Boolean} disabled - Si el botón está deshabilitado.
 * @prop {Boolean} fullWidth - Si el botón ocupa el ancho completo del contenedor.
 * 
 * @event click - Se emite cuando el usuario hace clic en el botón.
 */
export default {
    name: 'ButtonBasic',
    props: {
        to: {
            type: String,
            default: ''
        },
        variant: {
            type: String,
            default: 'primary',
            validator: (value: string) => ['primary', 'secondary', 'danger', 'success', 'default'].includes(value)
        },
        size: {
            type: String,
            default: 'md',
            validator: (value: string) => ['sm', 'md', 'lg'].includes(value)
        },
        disabled: {
            type: Boolean,
            default: false
        },
        fullWidth: {
            type: Boolean,
            default: false
        }
    },
    emits: ['click']
}
</script>

<style scoped>
button {
    backface-visibility: hidden;
    transform: translateZ(0);
    -webkit-font-smoothing: subpixel-antialiased;
}
</style>