import axios from "axios";
import Cookies from "js-cookie";
import type { Producto, ProductoModify } from "@/models/producto";

const API_BASE_URL = "http://localhost:8080";
const CATEGORIA_API_URL = `${API_BASE_URL}/vendedor/producto`;

export const productoService = {
    /**
     * Cambia el estado (activo/inactivo) de un producto.
     * @param {string} id - ID del producto.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async toggleStatus(id: string) {
        return axios.post(`${CATEGORIA_API_URL}/toggleStatus/${id}`);
    },
    /**
     * Crea un nuevo producto.
     * @param {ProductoModify} producto - Datos del producto a crear.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async createProducto(producto: ProductoModify) {
        return axios.post(`${CATEGORIA_API_URL}/create`, producto);
    },
    /**
     * Obtiene todos los productos.
     * @returns {Promise} Promesa con la lista de productos.
     */
    async getProductos() {
        return axios.get(`${CATEGORIA_API_URL}/getAll`);
    },
    /**
     * Obtiene un producto por su ID.
     * @param {string} id - ID del producto.
     * @returns {Promise} Promesa con los datos del producto.
     */
    async getProductoById(id: string) {
        return axios.get(`${CATEGORIA_API_URL}/getById/${id}`);
    },
    /**
     * Actualiza un producto existente.
     * @param {string} id - ID del producto.
     * @param {ProductoModify} producto - Datos actualizados del producto.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async updateProducto(id: string, producto: ProductoModify) {
        return axios.post(`${CATEGORIA_API_URL}/update/${id}`, producto);
    },
    /**
     * Elimina un producto por su ID.
     * @param {string} id - ID del producto.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async deleteProducto(id: string) {
        return axios.delete(`${CATEGORIA_API_URL}/delete/${id}`);
    },
    /**
     * Obtiene los productos de un usuario por su ID.
     * @param {string} id - ID del usuario.
     * @returns {Promise} Promesa con la lista de productos.
     */
    async getProductosByUsuario(id: string) {
        return axios.get(`${CATEGORIA_API_URL}/getByUserId/${id}`);
    },
    /**
     * Obtiene los productos por ID de categoría.
     * @param {string} id - ID de la categoría.
     * @returns {Promise} Promesa con la lista de productos.
     */
    async getProductosByIdCategoria(id: string) {
        return axios.get(`${CATEGORIA_API_URL}/getByIdCategoria/${id}`);
    },
    /**
     * Marca un producto como vendido y actualiza el stock.
     * @param {string} id - ID del producto.
     * @param {number} quantity - Cantidad vendida.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async markAsSold(id: string, quantity: number) {
        const response = await axios.get(`${CATEGORIA_API_URL}/getById/${id}`);
        const productData = response.data;

        // Update the product stock by reducing the quantity sold
        productData.stock = Math.max(0, productData.stock - quantity);
        return axios.post(`${CATEGORIA_API_URL}/update/${id}`, productData);
    }
}