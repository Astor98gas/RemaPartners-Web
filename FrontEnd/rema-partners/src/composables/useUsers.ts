import { ref } from 'vue'
import { userService } from '@/services/user.service'
import type { User, UserFormData, UserLogin } from '@/models/user'
import Cookies from 'js-cookie'
import { useToast } from 'vue-toastification';

/**
 * Composable para gestionar usuarios y autenticación.
 * Proporciona métodos para login, logout, registro, actualización de perfil y verificación de sesión.
 */
export function useUsers() {
    const users = ref<User[]>([])
    const error = ref<string | null>(null)
    const success = ref<string | null>(null)
    const loading = ref<boolean>(false)
    const currentUser = ref<User | null>(null)
    const toast = useToast()

    /**
     * Obtiene todos los usuarios.
     * @returns {Promise<void>}
     */
    const getUsers = async () => {
        try {
            loading.value = true
            const response = await userService.getUsers()
            users.value = response.data
        } catch (err: any) {
            error.value = err.response?.data?.message || 'Error obteniendo usuarios'
        } finally {
            loading.value = false
        }
    }

    /**
     * Autentica a un usuario con sus credenciales y establece el token de sesión.
     * 
     * Este método realiza las siguientes operaciones:
     * - Envía las credenciales del usuario al servidor para su validación
     * - Almacena el token de sesión recibido en cookies
     * - Configura el token para futuras solicitudes HTTP
     * - Actualiza el estado del usuario actual
     * - Redirige a la página principal en caso de éxito
     * 
     * @param {UserLogin} formData - Datos de inicio de sesión (username/email y password)
     * @returns {Promise<any>} Los datos del usuario autenticado, incluyendo el token
     * @throws Error si no se puede autenticar al usuario
     */
    const loginUser = async (formData: UserLogin) => {
        try {
            loading.value = true
            error.value = null
            const response = await userService.loginUser(formData)

            // Comentado: console.log de respuesta de login
            // console.log('Login response:', response)

            if (response.data && response.data.token) {
                // Guardar el token en cookies
                Cookies.set('token', response.data.token, { expires: 7, path: '/' }) // Expira en 7 días

                // Configurar el token para futuras solicitudes
                userService.setAuthToken(response.data.token)

                // Actualizar el estado del usuario actual
                await isLoggedIn()

                success.value = 'Inicio de sesión exitoso!'
                error.value = null
                window.location.href = '/'
                return response.data
            } else {
                error.value = 'Error de inicio de sesión: No se recibió token'
                success.value = null
                throw new Error('No se recibió token')
            }
        } catch (err: any) {
            console.error('Error de inicio de sesión:', err)
            error.value = err.response?.data?.message || 'Error iniciando sesión'
            success.value = null
            throw err
        } finally {
            loading.value = false
        }
    }

    /**
     * Verifica si el usuario está autenticado.
     * @returns {Promise<any|boolean>} Datos del usuario autenticado o false.
     */
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
            console.error('Error verificando estado de sesión:', err)
            error.value = err.response?.data?.message || 'Error verificando estado de sesión'
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

    /**
     * Crea un nuevo usuario y realiza login automático si es exitoso.
     * @param {UserFormData} formData - Datos del usuario a crear.
     * @returns {Promise<void>}
     */
    const createUser = async (formData: UserFormData) => {
        try {
            loading.value = true
            const response = await userService.createUser(formData)
            success.value = 'Usuario creado exitosamente!'
            error.value = null
            if (response.data && response.data.token) {
                Cookies.set('token', response.data.token, { expires: 7, path: '/' })
                success.value = 'Inicio de sesión exitoso!'
                error.value = null
                window.location.href = '/'
            } else {
                error.value = 'Error de inicio de sesión'
                success.value = null
            }
        } catch (err: any) {
            error.value = err.response?.data?.message || 'Error creando usuario'
            success.value = null
        } finally {
            loading.value = false
        }
    }

    /**
     * Cierra la sesión del usuario.
     * @param {boolean} redirect - Si debe redirigir al login tras cerrar sesión.
     * @returns {Promise<void>}
     */
    const logout = async (redirect = true) => {
        try {
            loading.value = true
            await userService.logout() // Llama al backend para invalidar el token
            Cookies.remove('token') // Elimina el token de las cookies
            currentUser.value = null // Resetea el usuario actual
            success.value = 'Cierre de sesión exitoso!'
            error.value = null

            // Redirige al usuario a la página de login solo si redirect es true
            if (redirect) {
                window.location.href = '/login'
            }
        } catch (err: any) {
            console.error('Error cerrando sesión:', err)
            error.value = err.response?.data?.message || 'Error cerrando sesión'
            success.value = null
        } finally {
            loading.value = false
        }
    }

    /**
     * Verifica si existe un usuario por email o username.
     * @param {string} email - Email a verificar.
     * @param {string} username - Username a verificar.
     * @returns {Promise<{exists: boolean, reason?: string}>}
     */
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
            console.error('Error verificando existencia de usuario:', err);
            throw err;
        }
    };

    /**
     * Actualiza el perfil del usuario.
     * @param {string} userId - ID del usuario.
     * @param {Partial<User>} userData - Datos a actualizar.
     * @returns {Promise<any>} Respuesta del backend.
     * @throws Error si ocurre un problema al actualizar.
     */
    const updateProfile = async (userId: string, userData: Partial<User>) => {
        try {
            loading.value = true;
            error.value = null;

            // Si el usuario actual existe y también tenemos su ID
            if (currentUser.value && userId) {
                // Crear un objeto con los datos actualizados
                const updatedUser: User = {
                    ...currentUser.value,
                    ...userData,
                    id: userId  // Aseguramos que el ID esté incluido
                };

                // Llamar al servicio para actualizar el usuario
                const response = await userService.updateUser(userId, updatedUser);

                // Verificar si el nombre de usuario fue cambiado (respuesta especial del backend)
                if (response.data && response.data.usernameChanged) {
                    // Necesita cerrar sesión e iniciar sesión nuevamente
                    success.value = 'Perfil actualizado exitosamente. Por favor inicia sesión nuevamente con tu nuevo nombre de usuario.';
                    // Realizar logout sin redirección
                    await logout(false);
                    // Retornar bandera especial para indicar cambio de nombre de usuario
                    return { usernameChanged: true, message: response.data.message };
                }

                // Actualizar el estado local del usuario
                currentUser.value = {
                    ...currentUser.value,
                    ...userData
                };

                success.value = 'Perfil actualizado exitosamente!';
                return response.data;
            } else {
                throw new Error('Usuario no encontrado o no se proporcionó ID');
            }
        } catch (err: any) {
            console.error('Error actualizando perfil:', err);
            error.value = err.response?.data?.message || 'Error actualizando perfil';
            toast.error(error.value);
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Refresca la información del usuario actual desde el servidor.
     * @returns {Promise<User|null>} Usuario actualizado.
     * @throws Error si ocurre un problema al refrescar.
     */
    const refreshUser = async () => {
        try {
            loading.value = true;
            error.value = null;
            const response = await userService.isLoggedIn();
            currentUser.value = response.data;
            return currentUser.value;
        } catch (err: any) {
            console.error("Error refrescando usuario:", err);
            error.value = err.response?.data?.message || "Error refrescando datos de usuario";
            success.value = null;
            throw err;
        } finally {
            loading.value = false;
        }
    };

    /**
     * Obtiene el perfil público de un usuario por su ID.
     * @param {string} userId - ID del usuario.
     * @returns {Promise<any>} Perfil público del usuario.
     * @throws Error si ocurre un problema al obtener el perfil.
     */
    const getUserProfileById = async (userId: string) => {
        try {
            loading.value = true;
            error.value = null;

            const response = await userService.getUserProfileById(userId);
            return response.data;
        } catch (err: any) {
            console.error("Error obteniendo perfil de usuario:", err);
            error.value = err.response?.data?.message || "Error obteniendo perfil de usuario";
            throw err;
        } finally {
            loading.value = false;
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
        checkUserIfExist,
        updateProfile,
        refreshUser,
        getUserProfileById
    }
}