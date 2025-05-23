import axios from 'axios'
import type { User, UserFormData, UserLogin } from '@/models/user'
import Cookies from 'js-cookie'

const API_BASE_URL = 'http://localhost:8080'

export const userService = {
    /**
     * Configura el token de autenticación en los headers por defecto.
     * @param {string} token - Token JWT.
     */
    setAuthToken(token: string) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    },

    /**
     * Inicializa el token de autenticación si existe en las cookies.
     */
    initializeAuth() {
        const token = Cookies.get('token');;
        if (token) {
            this.setAuthToken(token);
        }
    },

    /**
     * Crea un nuevo usuario.
     * @param {UserFormData} user - Datos del usuario a crear.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async createUser(user: UserFormData) {
        return axios.post(`${API_BASE_URL}/createUser`, user)
    },

    /**
     * Obtiene un usuario por su email.
     * @param {string} email - Email del usuario.
     * @returns {Promise} Promesa con los datos del usuario.
     */
    async getUserByEmail(email: string) {
        return axios.get(`${API_BASE_URL}/getUserByEmail/${email}`)
    },

    /**
     * Obtiene un usuario por su ID.
     * @param {string} id - ID del usuario.
     * @returns {Promise} Promesa con los datos del usuario.
     */
    async getUserById(id: string) {
        return axios.get(`${API_BASE_URL}/getUserById/${id}`)
    },

    /**
     * Obtiene un usuario por su nombre de usuario.
     * @param {string} username - Nombre de usuario.
     * @returns {Promise} Promesa con los datos del usuario.
     */
    async getUserByUsername(username: string) {
        return axios.get(`${API_BASE_URL}/getUserByUsername/${username}`)
    },

    /**
     * Inicia sesión de usuario.
     * @param {UserLogin} user - Credenciales de usuario.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async loginUser(user: UserLogin) {
        return axios.post(`${API_BASE_URL}/login`, user)
    },

    /**
     * Obtiene la lista de todos los usuarios.
     * @returns {Promise} Promesa con la lista de usuarios.
     */
    async getUsers() {
        return axios.get(`${API_BASE_URL}/getUsers`)
    },

    /**
     * Actualiza los datos de un usuario.
     * @param {string} id - ID del usuario.
     * @param {User} user - Datos actualizados del usuario.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async updateUser(id: string, user: User) {
        return axios.post(`${API_BASE_URL}/updateUser/${id}`, user)
    },

    /**
     * Elimina un usuario.
     * @param {User} user - Usuario a eliminar.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async deleteUser(user: User) {
        return axios.get(`${API_BASE_URL}/deleteUser/${user}`)
    },

    /**
     * Obtiene un usuario a partir de un token.
     * @param {string} token - Token JWT.
     * @returns {Promise} Promesa con los datos del usuario.
     */
    async getUserByToken(token: string) {
        return axios.get(`${API_BASE_URL}/getUserByToken/${token}`)
    },

    /**
     * Verifica si el usuario está autenticado.
     * @returns {Promise} Promesa con el estado de autenticación.
     */
    async isLoggedIn() {
        return axios.get(`${API_BASE_URL}/isLoggedIn`)
    },
    /**
     * Cierra la sesión del usuario.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async logout() {

        const token = Cookies.get('token');
        if (!token) {
            throw new Error('No token found');
        }

        return axios.get(`${API_BASE_URL}/log_out`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
            withCredentials: true, // Asegúrate de enviar las cookies si es necesario
        });
    },

    /**
     * Obtiene el perfil de usuario por su ID.
     * @param {string} id - ID del usuario.
     * @returns {Promise} Promesa con los datos del perfil.
     */
    async getUserProfileById(id: string) {
        return axios.get(`${API_BASE_URL}/getUserProfileById/${id}`)
    },
}

// Inicializa la autenticación al importar el servicio
userService.initializeAuth();