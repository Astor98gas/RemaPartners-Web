import axios from "axios";
import Cookies from "js-cookie";
import type { Categoria, CategoriaModify } from "@/models/categoria";

const API_BASE_URL = "http://localhost:8080";
const CATEGORIA_API_URL = `${API_BASE_URL}/admin/categoria`;

export const categoriaService = {
    /**
     * Crea una nueva categoría.
     * @param {CategoriaModify} categoria - Datos de la categoría a crear.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async createCategoria(categoria: CategoriaModify) {
        return axios.post(`${CATEGORIA_API_URL}/create`, categoria);
    },

    /**
     * Obtiene todas las categorías.
     * @returns {Promise} Promesa con la lista de categorías.
     */
    async getCategorias() {
        return axios.get(`${CATEGORIA_API_URL}/getAll`);
    },

    /**
     * Obtiene una categoría por su ID.
     * @param {string} id - ID de la categoría.
     * @returns {Promise} Promesa con los datos de la categoría.
     */
    async getCategoriaById(id: string) {
        return axios.get(`${CATEGORIA_API_URL}/getById/${id}`);
    },

    /**
     * Actualiza una categoría existente.
     * @param {string} id - ID de la categoría a actualizar.
     * @param {CategoriaModify} categoria - Datos actualizados de la categoría.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async updateCategoria(id: string, categoria: CategoriaModify) {
        return axios.post(`${CATEGORIA_API_URL}/update/${id}`, categoria);
    },

    /**
     * Elimina una categoría por su ID.
     * @param {string} id - ID de la categoría a eliminar.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async deleteCategoria(id: string) {
        return axios.delete(`${CATEGORIA_API_URL}/delete/${id}`);
    },
};