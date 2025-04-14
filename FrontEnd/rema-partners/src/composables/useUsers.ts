import { ref } from 'vue'
import { userService } from '@/services/user.service'
import type { User, UserFormData, UserLogin } from '@/models/user'

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

    const loginUser = async (formData: UserLogin) => {
        try {
            loading.value = true
            error.value = null
            const response = await userService.loginUser(formData)

            console.log('Login response:', response); // Para debugging

            if (response.data && response.data.token) {
                // Guardar el token en localStorage
                localStorage.setItem('token', response.data.token);

                // También configurar el token para futuras solicitudes
                userService.setAuthToken(response.data.token);

                success.value = 'Login successful!'
                error.value = null
                return response.data;
            } else {
                error.value = 'Login failed: No token received'
                success.value = null
                throw new Error('No token received');
            }
        } catch (err: any) {
            console.error('Login error:', err);
            error.value = err.response?.data?.message || 'Error logging in'
            success.value = null
            throw err;
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
            const response = await userService.createUser(formData)
            success.value = 'User created successfully!'
            error.value = null
            if (response.data && response.data.token) {
                localStorage.setItem('token', response.data.token)
                success.value = 'Login successful!'
                error.value = null
            } else {
                error.value = 'Login failed'
                success.value = null
            }
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

    const isLoggedIn = async () => {
        try {
            // Verificar primero si hay token guardado
            const token = localStorage.getItem('token');
            if (!token) {
                return false;
            }

            loading.value = true;

            // Asegúrate de que el token está configurado en los headers
            userService.setAuthToken(token);

            const response = await userService.isLoggedIn();
            return response.data;
        }
        catch (err: any) {
            console.error('Error checking login status:', err);
            error.value = err.response?.data?.message || 'Error checking login status';

            // Si hay un error 401 o 403, remover el token porque no es válido
            if (err.response && (err.response.status === 401 || err.response.status === 403)) {
                localStorage.removeItem('token');
            }

            return false;
        } finally {
            loading.value = false;
        }
    }

    const getUserByToken = async (token: string) => {
        try {
            loading.value = true
            const response = await userService.getUserByToken(token)
            return response.data
        } catch (err: any) {
            error.value = err.response?.data?.message || 'Error fetching user by token'
            return null
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
        isLoggedIn,
        getUserByToken,
        loginUser,
    }
}