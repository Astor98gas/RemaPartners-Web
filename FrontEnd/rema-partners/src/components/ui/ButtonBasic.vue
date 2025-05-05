<template>
    <button :class="[
        'px-4 py-2 rounded-3xl transition-all duration-300 font-medium shadow-sm',
        'focus:outline-none focus:ring-2 focus:ring-offset-2 active:scale-95',
        variant === 'primary' ? 'bg-blue-600 text-white hover:bg-blue-500 focus:ring-blue-500 hover:shadow-md' :
            variant === 'secondary' ? 'border-2 border-gray-500 text-gray-700 hover:bg-gray-100 hover:border-gray-600 focus:ring-gray-500' :
                variant === 'danger' ? 'bg-red-600 text-white hover:bg-red-500 focus:ring-red-500 hover:shadow-md' :
                    'bg-gray-600 text-white hover:bg-gray-500 focus:ring-gray-500 hover:shadow-md',
        disabled ? 'opacity-60 cursor-not-allowed' : 'cursor-pointer',
        fullWidth ? 'w-full' : '',
        size === 'sm' ? 'text-sm py-1.5 px-3' : size === 'lg' ? 'text-lg px-6 py-3 font-semibold' : 'text-base'
    ]" :disabled="disabled" @click="$emit('click')">
        <slot></slot>
    </button>
</template>

<script lang="ts">
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
            validator: (value: string) => ['primary', 'secondary', 'danger', 'default'].includes(value)
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