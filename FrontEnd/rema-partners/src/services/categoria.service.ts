import axios from "axios";
import Cookies from "js-cookie";
import type { Categoria } from "@/models/categoria";

const API_BASE_URL = "http://localhost:8080";
const CATEGORIA_API_URL = `${API_BASE_URL}/admin/categoria`;

export const categoriaService = {
    async createCategoria(categoria: Categoria) {
        return axios.post(`${CATEGORIA_API_URL}/create`, categoria);
    },

    async getCategorias() {
        return axios.get(`${CATEGORIA_API_URL}/getAll`);
    },

    async getCategoriaById(id: string) {
        return axios.get(`${CATEGORIA_API_URL}/getById/${id}`);
    },

    async updateCategoria(id: string, categoria: Categoria) {
        return axios.post(`${CATEGORIA_API_URL}/update/${id}`, categoria);
    },

    async deleteCategoria(id: string) {
        return axios.delete(`${CATEGORIA_API_URL}/delete/${id}`);
    },
};