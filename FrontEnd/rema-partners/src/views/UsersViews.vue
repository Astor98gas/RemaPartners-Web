<template>
    <div class="min-h-screen bg-gradient-to-r from-blue-500 to-purple-600 p-8">
        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-xl p-6 mb-8">
            <h1 class="text-3xl font-bold mb-6 text-gray-800">User Management</h1>

            <UserForm @submit="createUser" />

            <p v-if="error" class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-4">{{ error }}</p>
            <p v-if="success" class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-4">{{ success }}</p>
        </div>

        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-xl p-6">
            <UserList :users="users" @update="updateUser" @delete="deleteUser" />
        </div>
    </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import UserForm from '@/components/users/UserForm.vue'
import UserList from '@/components/users/UserList.vue'
import { useUsers } from '@/composables/useUsers'

const {
    users,
    error,
    success,
    getUsers,
    createUser,
    updateUser,
    deleteUser
} = useUsers()

onMounted(() => {
    getUsers()
})
</script>