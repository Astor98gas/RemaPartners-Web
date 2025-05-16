import axios from "axios";
import Cookies from "js-cookie";
import type { Producto, ProductoModify } from "@/models/producto";

const API_BASE_URL = "http://localhost:8080";
const CATEGORIA_API_URL = `${API_BASE_URL}/vendedor/producto`;

export const productoService = {
    async toggleStatus(id: string) {
        return axios.post(`${CATEGORIA_API_URL}/toggleStatus/${id}`);
    },
    async createProducto(producto: ProductoModify) {
        return axios.post(`${CATEGORIA_API_URL}/create`, producto);
    },
    async getProductos() {
        return axios.get(`${CATEGORIA_API_URL}/getAll`);
    },
    async getProductoById(id: string) {
        return axios.get(`${CATEGORIA_API_URL}/getById/${id}`);
    },
    async updateProducto(id: string, producto: ProductoModify) {
        return axios.post(`${CATEGORIA_API_URL}/update/${id}`, producto);
    },
    async deleteProducto(id: string) {
        return axios.delete(`${CATEGORIA_API_URL}/delete/${id}`);
    },
    async getProductosByUsuario(id: string) {
        return axios.get(`${CATEGORIA_API_URL}/getByUserId/${id}`);
    },
    async getProductosByIdCategoria(id: string) {
        return axios.get(`${CATEGORIA_API_URL}/getByIdCategoria/${id}`);
    },
    async markAsSold(id: string, quantity: number) {
        const response = await axios.get(`${CATEGORIA_API_URL}/getById/${id}`);
        const productData = response.data;

        // Update the product stock by reducing the quantity sold
        productData.stock = Math.max(0, productData.stock - quantity);
        return axios.post(`${CATEGORIA_API_URL}/update/${id}`, productData);
    }
}