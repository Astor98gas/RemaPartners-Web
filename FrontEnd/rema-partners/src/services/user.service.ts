import axios from 'axios'
import type { User, UserFormData, UserLogin } from '@/models/user'
import Cookies from 'js-cookie'

const API_BASE_URL = 'http://localhost:8080'

export const userService = {
    // Método nuevo para configurar el token en los headers por defecto
    setAuthToken(token: string) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    },

    // Inicializa el token si existe en localStorage
    initializeAuth() {
        const token = Cookies.get('token');;
        if (token) {
            this.setAuthToken(token);
        }
    },

    async createUser(user: UserFormData) {
        return axios.post(`${API_BASE_URL}/createUser`, user)
    },

    async getUserByEmail(email: string) {
        return axios.get(`${API_BASE_URL}/getUserByEmail/${email}`)
    },

    async getUserById(id: string) {
        return axios.get(`${API_BASE_URL}/getUserById/${id}`)
    },

    async getUserByUsername(username: string) {
        return axios.get(`${API_BASE_URL}/getUserByUsername/${username}`)
    },

    async loginUser(user: UserLogin) {
        return axios.post(`${API_BASE_URL}/login`, user)
    },

    async getUsers() {
        return axios.get(`${API_BASE_URL}/getUsers`)
    },

    async updateUser(id: string, user: User) {
        return axios.post(`${API_BASE_URL}/updateUser/${id}`, user)
    },

    async deleteUser(user: User) {
        return axios.get(`${API_BASE_URL}/deleteUser/${user}`)
    },

    async getUserByToken(token: string) {
        return axios.get(`${API_BASE_URL}/getUserByToken/${token}`)
    },

    async isLoggedIn() {
        return axios.get(`${API_BASE_URL}/isLoggedIn`)
    },
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
}

// Inicializa la autenticación al importar el servicio
userService.initializeAuth();