import type { User, UserFormData, UserLogin } from '@/models/user'
import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'

export const userService = {
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
}