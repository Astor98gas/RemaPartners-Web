import { ref } from 'vue'
import { userService } from '@/services/user.service'
import type { User, UserFormData } from '@/types/user'

export function useUsers() {
    const users = ref<User[]>([])
    const error = ref<string | null>(null)
    const success = ref<string | null>(null)
    const loading = ref<boolean>(false)

    const getUsers = async () => {
        try {
            loading.value = true
            const response = await userService.getUsers()
            users.value = response.data
        } catch (err: any) {
            error.value = err.response?.data?.message || 'Error fetching users'
        } finally {
            loading.value = false
        }
    }

    const createUser = async (formData: User) => {
        try {
            loading.value = true
            await userService.createUser(formData)
            success.value = 'User created successfully!'
            error.value = null
            await getUsers()
        } catch (err: any) {
            error.value = err.response?.data?.message || 'Error creating user'
            success.value = null
        } finally {
            loading.value = false
        }
    }

    const updateUser = async (user: User) => {
        try {
            loading.value = true
            await userService.updateUser(user.id, user)
            success.value = 'User updated successfully!'
            error.value = null
            await getUsers()
        } catch (err: any) {
            error.value = err.response?.data?.message || 'Error updating user'
            success.value = null
        } finally {
            loading.value = false
        }
    }

    const deleteUser = async (user: User) => {
        try {
            loading.value = true
            await userService.deleteUser(user)
            success.value = 'User deleted successfully!'
            error.value = null
            await getUsers()
        } catch (err: any) {
            error.value = err.response?.data?.message || 'Error deleting user'
            success.value = null
        } finally {
            loading.value = false
        }
    }

    return {
        users,
        error,
        success,
        loading,
        getUsers,
        createUser,
        updateUser,
        deleteUser
    }
}