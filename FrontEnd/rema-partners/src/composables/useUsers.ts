import { ref } from 'vue'
import { userService } from '@/services/user.service'
import type { User, UserFormData, UserLogin } from '@/models/user'
import Cookies from 'js-cookie'
import { useToast } from 'vue-toastification';

export function useUsers() {
    const users = ref<User[]>([])
    const error = ref<string | null>(null)
    const success = ref<string | null>(null)
    const loading = ref<boolean>(false)
    const currentUser = ref<User | null>(null)
    const toast = useToast()

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

            console.log('Login response:', response)

            if (response.data && response.data.token) {
                // Guardar el token en cookies
                Cookies.set('token', response.data.token, { expires: 7, path: '/' }) // Expira en 7 días

                // Configurar el token para futuras solicitudes
                userService.setAuthToken(response.data.token)

                // Actualizar el estado del usuario actual
                await isLoggedIn()

                success.value = 'Login successful!'
                error.value = null
                window.location.href = '/home'
                return response.data
            } else {
                error.value = 'Login failed: No token received'
                success.value = null
                throw new Error('No token received')
            }
        } catch (err: any) {
            console.error('Login error:', err)
            error.value = err.response?.data?.message || 'Error logging in'
            success.value = null
            throw err
        } finally {
            loading.value = false
        }
    }

    const isLoggedIn = async () => {
        try {
            // Verificar si hay token guardado en las cookies
            const token = Cookies.get('token')
            if (!token) {
                currentUser.value = null
                return false
            }
            loading.value = true
            userService.setAuthToken(token)
            const response = await userService.isLoggedIn()
            currentUser.value = response.data
            return response.data
        } catch (err: any) {
            console.error('Error checking login status:', err)
            error.value = err.response?.data?.message || 'Error checking login status'
            if (err.response && (err.response.status === 401 || err.response.status === 403)) {
                Cookies.remove('token') // Eliminar el token si no es válido
                currentUser.value = null
                window.location.href = '/login?error=session_expired';
            }
            return false
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
                Cookies.set('token', response.data.token, { expires: 7, path: '/' })
                success.value = 'Login successful!'
                error.value = null
                window.location.href = '/home'
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

    const logout = async () => {
        try {
            loading.value = true
            await userService.logout() // Llama al backend para invalidar el token
            Cookies.remove('token') // Elimina el token de las cookies
            currentUser.value = null // Resetea el usuario actual
            success.value = 'Logout successful!'
            error.value = null

            // Redirige al usuario a la página de login
            window.location.href = '/login'
        } catch (err: any) {
            console.error('Logout error:', err)
            error.value = err.response?.data?.message || 'Error logging out'
            success.value = null
        } finally {
            loading.value = false
        }
    }

    const checkUserIfExist = async (email: string, username: string) => {
        try {
            const response = await userService.getUserByEmail(email);
            if (response.data) {
                return { exists: true, reason: 'email' };
            }

            const usernameResponse = await userService.getUserByUsername(username);
            if (usernameResponse.data) {
                return { exists: true, reason: 'username' };
            }

            return { exists: false };
        } catch (err: any) {
            console.error('Error checking user existence:', err);
            throw err;
        }
    };

    return {
        users,
        error,
        success,
        loading,
        currentUser,
        getUsers,
        createUser,
        loginUser,
        isLoggedIn,
        logout,
        checkUserIfExist
    }
}