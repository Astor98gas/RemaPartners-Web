<template>
    <div class="flex flex-col gap-1">
        <label v-if="label" :for="id" class="text-sm font-medium text-gray-700">
            {{ label }}
        </label>
        <input :id="id" :type="type" :value="modelValue" :placeholder="placeholder" :disabled="disabled" :class="[
            'px-3 py-2 rounded-md border border-gray-300 shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all duration-200',
            {
                'border-red-500 focus:ring-red-500 focus:border-red-500': error,
                'bg-gray-100 text-gray-500 cursor-not-allowed': disabled
            }
        ]" @input="handleInput" @blur="handleBlur" />
        <span v-if="error" class="text-sm text-red-600">
            {{ error }}
        </span>
        <span v-if="hint && !error" class="text-sm text-gray-500">
            {{ hint }}
        </span>
    </div>
</template>

<script lang="ts">
export default {
    name: 'InputText',
    props: {
        modelValue: {
            type: [String, Number],
            default: ''
        },
        type: {
            type: String,
            default: 'text',
            validator: (value: string) => ['text', 'password', 'email', 'number', 'tel', 'url'].includes(value)
        },
        label: {
            type: String,
            default: ''
        },
        placeholder: {
            type: String,
            default: ''
        },
        error: {
            type: String,
            default: ''
        },
        hint: {
            type: String,
            default: ''
        },
        disabled: {
            type: Boolean,
            default: false
        },
        id: {
            type: String,
            default: () => `input-${Math.random().toString(36).substr(2, 9)}`
        }
    },
    emits: ['update:modelValue', 'blur'],
    methods: {
        handleInput(event: InputEvent) {
            this.$emit('update:modelValue', (event.target as HTMLInputElement).value)
        },
        handleBlur(event: FocusEvent) {
            this.$emit('blur', event)
        }
    }
}
</script>