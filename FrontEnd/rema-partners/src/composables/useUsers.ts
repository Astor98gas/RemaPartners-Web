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

    const checkUserIfExist = async (email: string, username: string) => {
        try {
            loading.value = true
            error.value = null

            try {
                const emailResponse = await userService.getUserByEmail(email)
                if (emailResponse.data && emailResponse.data.email) {
                    error.value = 'Email already exists'
                    return { exists: true, reason: 'email' }
                }
            } catch (emailErr: any) {
                if (emailErr.response?.status !== 404) {
                    throw emailErr
                }
            }

            try {
                const usernameResponse = await userService.getUserByUsername(username)
                if (usernameResponse.data && usernameResponse.data.username) {
                    error.value = 'Username already exists'
                    return { exists: true, reason: 'username' }
                }
            } catch (usernameErr: any) {
                if (usernameErr.response?.status !== 404) {
                    throw usernameErr
                }
            }

            return { exists: false }
        } catch (err: any) {
            error.value = err.response?.data?.message || 'Error checking user existence'
            return { exists: false, error: error.value }
        } finally {
            loading.value = false
        }
    }

    const createUser = async (formData: UserFormData) => {
        try {
            loading.value = true
            await userService.createUser(formData)
            success.value = 'User created successfully!'
            error.value = null
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
        deleteUser,
        checkUserIfExist,
    }
}