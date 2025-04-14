<template>
    <div class="space-y-4 mb-6">
        <input v-model="formData.username" placeholder="Username"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        <input v-model="formData.email" placeholder="Email"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        <input v-model="formData.password" type="password" placeholder="Password"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
        <button @click="onSubmit"
            class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200">
            Submit
        </button>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import type { UserFormData } from '@/models/user';

export default defineComponent({
    name: 'UserForm',
    emits: ['submit'],

    setup(props, { emit }) {
        const formData = ref<UserFormData>({
            username: '',
            email: '',
            password: ''
        });

        const onSubmit = () => {
            emit('submit', formData.value);
            formData.value = { username: '', email: '', password: '' };
        };

        return {
            formData,
            onSubmit
        };
    }
});
</script>