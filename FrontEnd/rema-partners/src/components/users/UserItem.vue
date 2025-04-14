<template>
    <div class="bg-gray-50 p-4 rounded-lg mb-4 shadow">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
            <input v-model="user.id" type="hidden">
            <input v-model="user.username" type="text"
                class="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <input v-model="user.email" type="text"
                class="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
            <input v-model="user.password" type="password"
                class="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">

            <div class="flex space-x-2">
                <button @click="$emit('update', user)"
                    class="bg-yellow-500 hover:bg-yellow-600 text-white font-bold py-2 px-4 rounded-lg transition duration-200">
                    Update
                </button>
                <button @click="$emit('delete', user.username)"
                    class="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded-lg transition duration-200">
                    Delete
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import type { User } from '@/models/user'

defineProps<{
    user: User
}>()

defineEmits(['update', 'delete'])
</script>