import axios from "axios";
import type { FacturaEntity } from "@/models/factura";

const API_BASE_URL = "http://localhost:8080";
const FACTURA_API_URL = `${API_BASE_URL}/api/factura`;

export const facturaService = {
    /**
     * Crea una nueva factura.
     * @param {FacturaEntity} factura - Datos de la factura.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async createFactura(factura: FacturaEntity) {
        return axios.post(`${FACTURA_API_URL}/create`, factura);
    },

    /**
     * Crea una factura a partir de una venta de producto.
     * @param {string} idProducto - ID del producto.
     * @param {string} idComprador - ID del comprador.
     * @param {string} idVendedor - ID del vendedor.
     * @param {number} cantidad - Cantidad vendida.
     * @param {string} idChat - ID del chat asociado.
     * @returns {Promise} Promesa con la respuesta del servidor.
     */
    async createFromSale(idProducto: string, idComprador: string, idVendedor: string, cantidad: number, idChat: string) {
        return axios.post(`${FACTURA_API_URL}/createFromSale/${idProducto}/${idComprador}/${idVendedor}/${cantidad}/${idChat}`);
    },

    /**
     * Obtiene una factura por su ID.
     * @param {string} id - ID de la factura.
     * @returns {Promise} Promesa con los datos de la factura.
     */
    async getFacturaById(id: string) {
        return axios.get(`${FACTURA_API_URL}/getById/${id}`);
    },

    /**
     * Obtiene todas las facturas donde el usuario es el comprador.
     * @param {string} idComprador - ID del comprador.
     * @returns {Promise} Promesa con la lista de facturas.
     */
    async getFacturasByBuyerId(idComprador: string) {
        return axios.get(`${FACTURA_API_URL}/getByBuyerId/${idComprador}`);
    },

    /**
     * Obtiene todas las facturas donde el usuario es el vendedor.
     * @param {string} idVendedor - ID del vendedor.
     * @returns {Promise} Promesa con la lista de facturas.
     */
    async getFacturasBySellerId(idVendedor: string) {
        return axios.get(`${FACTURA_API_URL}/getBySellerId/${idVendedor}`);
    },

    /**
     * Obtiene todas las facturas de un producto específico.
     * @param {string} idProducto - ID del producto.
     * @returns {Promise} Promesa con la lista de facturas.
     */
    async getFacturasByProductId(idProducto: string) {
        return axios.get(`${FACTURA_API_URL}/getByProductId/${idProducto}`);
    },

    /**
     * Obtiene todas las facturas asociadas a un chat.
     * @param {string} idChat - ID del chat.
     * @returns {Promise} Promesa con la lista de facturas.
     */
    async getFacturasByChatId(idChat: string) {
        return axios.get(`${FACTURA_API_URL}/getByChatId/${idChat}`);
    }
};
