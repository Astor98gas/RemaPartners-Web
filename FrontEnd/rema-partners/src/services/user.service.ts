import type { User } from '@/types/user'
import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'

export const userService = {
    async createUser(user: User) {
        return axios.post(`${API_BASE_URL}/createUser`, user)
    },

    async getUsers() {
        return axios.get(`${API_BASE_URL}/getUsers`)
    },

    async updateUser(id: string, user: User) {
        return axios.post(`${API_BASE_URL}/updateUser/${id}`, user)
    },

    async deleteUser(user: User) {
        return axios.get(`${API_BASE_URL}/deleteUser/${user}`)
    }
}