<template>
    <div>
        <h2 class="text-2xl font-bold mb-6 text-gray-800">User List</h2>
        <UserItem v-for="user in users" :key="user.id" :user="user" @update="$emit('update', $event)"
            @delete="$emit('delete', $event)" />
    </div>
</template>

<script setup lang="ts">
import UserItem from './UserItem.vue'
import type { User } from '@/types/user'

defineProps<{
    users: User[]
}>()

defineEmits(['update', 'delete'])
</script>