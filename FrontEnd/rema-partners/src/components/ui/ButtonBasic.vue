<template>
    <button :class="[
        'px-4 py-2 rounded-3xl transition-colors duration-200',
        'focus:outline-none focus:ring-2 focus:ring-offset-2',
        variant === 'primary' ? 'bg-gray-600 text-white hover:bg-gray-400 focus:ring-gray-500' :
            variant === 'secondary' ? 'border border-gray-500 text-gray-500 hover:bg-gray-400 hover:text-white focus:ring-gray-500' :
                variant === 'danger' ? 'bg-red-500 text-white hover:bg-red-600 focus:ring-red-500' :
                    'bg-gray-500 text-white hover:bg-gray-600 focus:ring-gray-500',
        disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer',
        fullWidth ? 'w-full' : '',
        size === 'sm' ? 'text-sm' : size === 'lg' ? 'text-lg px-6 py-3' : 'text-base'
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